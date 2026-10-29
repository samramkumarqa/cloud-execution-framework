package io.github.samramkumarqa.cloud.driver;

import io.github.samramkumarqa.cloud.config.Config;
import io.github.samramkumarqa.cloud.config.RunContext;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Sauce Labs. Credentials come from SAUCE_USERNAME / SAUCE_ACCESS_KEY (never the repo); the data centre is
 * configurable via sauce.region (default eu-central-1).
 */
class SauceLabsDriverProvider implements DriverProvider {

    @Override
    public WebDriver create(RunContext ctx) {
        String username = Config.require("sauce.username");
        String accessKey = Config.require("sauce.access.key");
        String region = Config.get("sauce.region", "eu-central-1");

        MutableCapabilities options = BrowserOptionsFactory.forContext(ctx, false);

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", username);
        sauceOptions.put("accessKey", accessKey);
        sauceOptions.put("name", ctx.testName());
        sauceOptions.put("build", ctx.build());
        options.setCapability("sauce:options", sauceOptions);

        String url = "https://ondemand." + region + ".saucelabs.com:443/wd/hub";
        try {
            return new RemoteWebDriver(new URL(url), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Bad Sauce Labs URL: " + url, e);
        }
    }

    @Override
    public boolean reportsResult() {
        return true;
    }
}
