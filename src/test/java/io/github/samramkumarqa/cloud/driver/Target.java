package io.github.samramkumarqa.cloud.driver;

/** Where the suite runs. One suite, selected at launch with -Dtarget=... */
public enum Target {
    /** Browser on this machine via Selenium Manager (also how GitHub Actions runs it). */
    LOCAL,
    /** Any remote Selenium hub by URL: a self-hosted Grid on AWS EC2, a Zalenium/Grid on Google Cloud, Docker, etc. */
    GRID,
    SAUCELABS,
    BROWSERSTACK;

    public static Target from(String value) {
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown target '" + value + "'. Use one of: local, grid, saucelabs, browserstack");
        }
    }
}
