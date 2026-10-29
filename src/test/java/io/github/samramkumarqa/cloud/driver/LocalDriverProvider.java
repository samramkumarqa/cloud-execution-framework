package io.github.samramkumarqa.cloud.driver;

import io.github.samramkumarqa.cloud.config.Config;
import io.github.samramkumarqa.cloud.config.RunContext;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** A browser on this machine. Selenium Manager resolves the driver binary, so nothing is committed or installed. */
class LocalDriverProvider implements DriverProvider {

    @Override
    public WebDriver create(RunContext ctx) {
        boolean headless = Config.getBool("headless", true);
        MutableCapabilities options = BrowserOptionsFactory.forContext(ctx, headless);
        if (ctx.isChrome()) {
            return new ChromeDriver((ChromeOptions) options);
        }
        if (ctx.isFirefox()) {
            return new FirefoxDriver((FirefoxOptions) options);
        }
        return new EdgeDriver((EdgeOptions) options);
    }
}
