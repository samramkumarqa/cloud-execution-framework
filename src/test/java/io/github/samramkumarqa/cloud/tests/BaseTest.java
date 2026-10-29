package io.github.samramkumarqa.cloud.tests;

import io.github.samramkumarqa.cloud.config.Config;
import io.github.samramkumarqa.cloud.config.RunContext;
import io.github.samramkumarqa.cloud.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

/**
 * Starts a driver for the chosen target before each test and quits it after, reporting the verdict to the cloud
 * dashboard where the target supports it. Browser / version / platform come from the suite XML and can be overridden
 * on the command line (e.g. -Dbrowser=firefox). The same test classes run unchanged on every target.
 */
public abstract class BaseTest {

    @Parameters({"browser", "browserVersion", "platform"})
    @BeforeMethod(alwaysRun = true)
    public void startDriver(@Optional String browser, @Optional String browserVersion,
                            @Optional String platform, Method method) {
        RunContext ctx = new RunContext(
                Config.get("browser", browser == null ? "chrome" : browser),
                Config.get("browserVersion", browserVersion == null ? "latest" : browserVersion),
                Config.get("platform", platform),
                Config.get("build.name", "cloud-execution local #" + System.getProperty("user.name", "dev")),
                method.getDeclaringClass().getSimpleName() + "." + method.getName());
        DriverFactory.start(ctx);
    }

    @AfterMethod(alwaysRun = true)
    public void stopDriver(ITestResult result) {
        DriverFactory.stop(result.isSuccess());
    }

    protected WebDriver driver() {
        return DriverFactory.get();
    }
}
