package io.github.samramkumarqa.cloud.driver;

import io.github.samramkumarqa.cloud.config.Config;
import io.github.samramkumarqa.cloud.config.RunContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Chooses a {@link DriverProvider} from -Dtarget and hands out one WebDriver per test thread, so the suite can run
 * in parallel. The test code never knows which target it is on.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<Target> ACTIVE_TARGET = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver start(RunContext ctx) {
        Target target = Target.from(Config.get("target", "local"));
        WebDriver driver = providerFor(target).create(ctx);
        DRIVER.set(driver);
        ACTIVE_TARGET.set(target);
        return driver;
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No driver for this thread; DriverFactory.start() was not called.");
        }
        return driver;
    }

    /** Reports the verdict to Sauce Labs / BrowserStack when the active target supports it, then quits the driver. */
    public static void stop(boolean passed) {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            Target target = ACTIVE_TARGET.get();
            if (target != null && providerFor(target).reportsResult()) {
                reportResult(driver, target, passed);
            }
        } catch (RuntimeException ignored) {
            // reporting must never fail the run
        } finally {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
                ACTIVE_TARGET.remove();
            }
        }
    }

    private static void reportResult(WebDriver driver, Target target, boolean passed) {
        String status = passed ? "passed" : "failed";
        String script = switch (target) {
            case SAUCELABS -> "sauce:job-result=" + status;
            case BROWSERSTACK -> "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": "
                    + "{\"status\":\"" + status + "\", \"reason\": \"TestNG result\"}}";
            default -> null;
        };
        if (script != null) {
            ((JavascriptExecutor) driver).executeScript(script);
        }
    }

    private static DriverProvider providerFor(Target target) {
        return switch (target) {
            case LOCAL -> new LocalDriverProvider();
            case GRID -> new GridDriverProvider();
            case SAUCELABS -> new SauceLabsDriverProvider();
            case BROWSERSTACK -> new BrowserStackDriverProvider();
        };
    }
}
