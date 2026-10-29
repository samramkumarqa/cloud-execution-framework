package io.github.samramkumarqa.cloud.driver;

import io.github.samramkumarqa.cloud.config.RunContext;
import org.openqa.selenium.WebDriver;

/** Builds a WebDriver for one execution target. One implementation per place the suite can run. */
public interface DriverProvider {
    WebDriver create(RunContext ctx);

    /** Whether the provider can report a pass/fail verdict back to the platform's dashboard. */
    default boolean reportsResult() {
        return false;
    }
}
