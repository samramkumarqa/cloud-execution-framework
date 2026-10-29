package io.github.samramkumarqa.cloud.config;

/** What to run and where, assembled per test method from suite parameters and -D overrides. */
public record RunContext(String browser, String browserVersion, String platform, String build, String testName) {

    public boolean isChrome() {
        return browser.equalsIgnoreCase("chrome");
    }

    public boolean isFirefox() {
        return browser.equalsIgnoreCase("firefox");
    }

    public boolean isEdge() {
        return browser.equalsIgnoreCase("edge");
    }
}
