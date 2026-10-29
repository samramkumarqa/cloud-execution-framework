# Cloud Execution Framework

[![CI](https://github.com/samramkumarqa/cloud-execution-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/samramkumarqa/cloud-execution-framework/actions/workflows/ci.yml)

One Selenium + TestNG suite that runs unchanged on four execution targets, chosen at launch with `-Dtarget`:

| `-Dtarget` | Runs on | Covers |
|------------|---------|--------|
| `local`    | A browser on this machine (Selenium Manager) | Local dev, **GitHub Actions** runners |
| `grid`     | Any remote Selenium hub at `grid.url` | **AWS EC2 Grid**, **Google Cloud** Grid/Zalenium, Docker Grid |
| `saucelabs`| Sauce Labs | Cross-browser cloud |
| `browserstack` | BrowserStack | Cross-browser cloud |

**Stack:** Java 17 · Selenium 4 · TestNG · AssertJ · GitHub Actions

The test classes and page objects never know where they run. A `DriverProvider` per target builds the `WebDriver`; a
`DriverFactory` picks one from `-Dtarget` and hands out one driver per test thread, so the suite runs in parallel
everywhere. On Sauce Labs and BrowserStack it also reports each test's pass/fail back to the provider's dashboard.

## The suite

SauceDemo, as a Page Object Model: login (valid, locked-out, wrong password), the inventory (6 products, 6 add-to-cart
buttons, price sort), and a full two-item checkout with total/tax assertions.

## Running

Needs JDK 17+, Maven, and a local browser for the `local` target.

```bash
# Local (default target), headed if you like
mvn test
mvn test -Dbrowser=firefox -Dheadless=false

# A remote Selenium Grid (AWS EC2, Google Cloud, Docker, ...)
mvn test -Dtarget=grid -Dgrid.url=http://YOUR_GRID_HOST:4444/wd/hub

# Sauce Labs (credentials from the environment, never the repo)
export SAUCE_USERNAME=... SAUCE_ACCESS_KEY=...
mvn test -Dtarget=saucelabs

# BrowserStack
export BROWSERSTACK_USERNAME=... BROWSERSTACK_ACCESS_KEY=...
mvn test -Dtarget=browserstack

# Cross-browser matrix on any remote/cloud target
mvn test -Dtarget=saucelabs -Dsurefire.suiteXmlFiles=src/test/resources/suites/cross-browser.xml
```

Reports land in `target/surefire-reports/` (plus the live session in the Sauce/BrowserStack dashboard).

## Configuration

Values resolve `-Dkey=value` → environment variable (`grid.url` → `GRID_URL`) → `config.properties`.

| Key | Default | Notes |
|-----|---------|-------|
| `target` | `local` | `local`, `grid`, `saucelabs`, `browserstack` |
| `browser` | `chrome` | `chrome`, `firefox`, `edge` |
| `headless` | `true` | Local target |
| `grid.url` | — | Required for `grid` |
| `sauce.username` / `sauce.access.key` | — | `SAUCE_USERNAME` / `SAUCE_ACCESS_KEY` |
| `sauce.region` | `eu-central-1` | Sauce data centre |
| `browserstack.username` / `browserstack.access.key` | — | `BROWSERSTACK_USERNAME` / `BROWSERSTACK_ACCESS_KEY` |
| `build.name` | auto | Build label shown in the cloud dashboard (`BUILD_NAME`) |

**No credentials live in this repo.** Cloud keys are read only from environment variables / CI secrets.

## CI

`.github/workflows/ci.yml` runs the suite on every push and PR with `target=local`, across Chrome and Firefox. The
"Run workflow" button can instead launch the cross-browser matrix on `saucelabs` or `browserstack` once you add the
matching repo secrets (`SAUCE_USERNAME`/`SAUCE_ACCESS_KEY`, `BROWSERSTACK_USERNAME`/`BROWSERSTACK_ACCESS_KEY`).

## About the rework

This replaces five near-identical repos (AWS, Sauce Labs, BrowserStack, Google Cloud, GitHub Actions), each a copy of
the same TestNG test wired to one target. They are now one suite with a pluggable driver layer:

- **Four providers cover all five old platforms** — AWS EC2 Grid and Google Cloud Zalenium are both just "a remote hub
  URL", so they share the `grid` target.
- **No hardcoded credentials.** The originals committed live Sauce Labs and BrowserStack access keys and a Zalenium
  login straight into source; here everything sensitive comes from the environment.
- **No committed driver binaries or machine-specific paths** — Selenium Manager resolves drivers.
- **Real assertions and a real POM suite** instead of a single inventory count, with parallel execution and
  dashboard result reporting on the cloud targets.
