package io.github.samramkumarqa.cloud.driver;

import io.github.samramkumarqa.cloud.config.Config;
import io.github.samramkumarqa.cloud.config.RunContext;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Any remote Selenium hub addressed by URL (grid.url). This one target covers a self-hosted Grid on AWS EC2,
 * a Grid/Zalenium on Google Cloud, or a local Docker Grid, since to the client they are all just a hub URL.
 */
class GridDriverProvider implements DriverProvider {

    @Override
    public WebDriver create(RunContext ctx) {
        String hubUrl = Config.require("grid.url");
        MutableCapabilities options = BrowserOptionsFactory.forContext(ctx, Config.getBool("headless", true));
        try {
            return new RemoteWebDriver(new URL(hubUrl), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("grid.url is not a valid URL: " + hubUrl, e);
        }
    }
}
