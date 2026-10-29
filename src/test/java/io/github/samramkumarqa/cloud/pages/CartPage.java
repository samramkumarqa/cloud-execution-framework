package io.github.samramkumarqa.cloud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private static final By ITEM_NAME = testId("inventory-item-name");
    private static final By CHECKOUT = testId("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> itemNames() {
        return all(ITEM_NAME).stream().map(e -> e.getText().trim()).toList();
    }

    public CheckoutPage checkout() {
        click(CHECKOUT);
        return new CheckoutPage(driver);
    }
}
