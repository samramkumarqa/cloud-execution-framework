package io.github.samramkumarqa.cloud.pages;

import io.github.samramkumarqa.cloud.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = testId("username");
    private static final By PASSWORD = testId("password");
    private static final By LOGIN = testId("login-button");
    private static final By ERROR = testId("error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(Config.get("ui.base.url", "https://www.saucedemo.com"));
        visible(LOGIN);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN);
        return new InventoryPage(driver);
    }

    public LoginPage loginExpectingFailure(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN);
        return this;
    }

    public String error() {
        return text(ERROR);
    }
}
