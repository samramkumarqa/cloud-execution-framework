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
 * BrowserStack Automate. Credentials come from BROWSERSTACK_USERNAME / BROWSERSTACK_ACCESS_KEY (never the repo).
 */
class BrowserStackDriverProvider implements DriverProvider {

    @Override
    public WebDriver create(RunContext ctx) {
        String username = Config.require("browserstack.username");
        String accessKey = Config.require("browserstack.access.key");

        MutableCapabilities options = BrowserOptionsFactory.forContext(ctx, false);

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", username);
        bstackOptions.put("accessKey", accessKey);
        bstackOptions.put("sessionName", ctx.testName());
        bstackOptions.put("buildName", ctx.build());
        if (ctx.platform() != null && !ctx.platform().isBlank()) {
            bstackOptions.put("os", ctx.platform());
        }
        options.setCapability("bstack:options", bstackOptions);

        String url = "https://hub-cloud.browserstack.com/wd/hub";
        try {
            return new RemoteWebDriver(new URL(url), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Bad BrowserStack URL: " + url, e);
        }
    }

    @Override
    public boolean reportsResult() {
        return true;
    }
}
