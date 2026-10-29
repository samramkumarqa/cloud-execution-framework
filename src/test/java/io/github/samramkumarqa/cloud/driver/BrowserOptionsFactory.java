package io.github.samramkumarqa.cloud.driver;

import io.github.samramkumarqa.cloud.config.RunContext;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Browser options shared by every target, so local and cloud launch the same browser the same way. */
final class BrowserOptionsFactory {

    private BrowserOptionsFactory() {
    }

    static MutableCapabilities forContext(RunContext ctx, boolean headless) {
        if (ctx.isChrome()) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications", "--disable-search-engine-choice-screen");
            // Chrome's leak-detection prompt pops up on demo passwords
            options.addArguments("--disable-features=PasswordLeakDetection");
            if (headless) {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
            }
            applyVersionPlatform(options, ctx);
            return options;
        }
        if (ctx.isFirefox()) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            applyVersionPlatform(options, ctx);
            return options;
        }
        if (ctx.isEdge()) {
            EdgeOptions options = new EdgeOptions();
            if (headless) {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
            }
            applyVersionPlatform(options, ctx);
            return options;
        }
        throw new IllegalArgumentException("Unsupported browser: " + ctx.browser());
    }

    /** A remote cloud/grid honours browserVersion and platformName; a local browser ignores them. */
    private static void applyVersionPlatform(MutableCapabilities options, RunContext ctx) {
        if (ctx.browserVersion() != null && !ctx.browserVersion().isBlank() && !ctx.browserVersion().equalsIgnoreCase("latest")) {
            options.setCapability("browserVersion", ctx.browserVersion());
        } else if (ctx.browserVersion() != null && ctx.browserVersion().equalsIgnoreCase("latest")) {
            options.setCapability("browserVersion", "latest");
        }
        if (ctx.platform() != null && !ctx.platform().isBlank()) {
            options.setCapability("platformName", ctx.platform());
        }
    }
}
