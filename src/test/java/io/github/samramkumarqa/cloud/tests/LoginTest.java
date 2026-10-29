package io.github.samramkumarqa.cloud.tests;

import io.github.samramkumarqa.cloud.pages.InventoryPage;
import io.github.samramkumarqa.cloud.pages.LoginPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    @Test(groups = "smoke")
    public void standardUserCanLogIn() {
        InventoryPage inventory = new LoginPage(driver()).open().loginAs("standard_user", "secret_sauce");
        assertThat(inventory.isLoaded()).as("products page shown after login").isTrue();
    }

    @Test(groups = "negative")
    public void lockedOutUserIsRejected() {
        LoginPage login = new LoginPage(driver()).open().loginExpectingFailure("locked_out_user", "secret_sauce");
        assertThat(login.error()).contains("locked out");
    }

    @Test(groups = "negative")
    public void wrongPasswordIsRejected() {
        LoginPage login = new LoginPage(driver()).open().loginExpectingFailure("standard_user", "wrong");
        assertThat(login.error()).contains("do not match");
    }
}
