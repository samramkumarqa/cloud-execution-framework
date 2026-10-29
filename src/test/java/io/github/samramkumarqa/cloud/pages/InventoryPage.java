package io.github.samramkumarqa.cloud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.List;

public class InventoryPage extends BasePage {

    private static final By TITLE = testId("title");
    private static final By ITEM = testId("inventory-item");
    private static final By ITEM_NAME = testId("inventory-item-name");
    private static final By ITEM_PRICE = testId("inventory-item-price");
    private static final By ADD_BUTTON = By.cssSelector("[data-test^='add-to-cart']");
    private static final By SORT = testId("product-sort-container");
    private static final By CART_BADGE = testId("shopping-cart-badge");
    private static final By CART_LINK = testId("shopping-cart-link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().endsWith("/inventory.html") && "Products".equals(text(TITLE));
    }

    public int itemCount() {
        visible(ITEM);
        return all(ITEM).size();
    }

    public int addToCartButtonCount() {
        visible(ITEM);
        return all(ADD_BUTTON).size();
    }

    public void addToCart(String productName) {
        product(productName).findElement(By.xpath(".//button[normalize-space()='Add to cart']")).click();
    }

    public BigDecimal priceOf(String productName) {
        return money(product(productName).findElement(ITEM_PRICE).getText());
    }

    public int cartBadgeCount() {
        List<WebElement> badge = all(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText().trim());
    }

    public void waitForCartBadge(int expected) {
        wait.until(ExpectedConditions.textToBe(CART_BADGE, String.valueOf(expected)));
    }

    public void sortBy(String visibleOption) {
        new Select(visible(SORT)).selectByVisibleText(visibleOption);
    }

    public List<BigDecimal> prices() {
        return all(ITEM_PRICE).stream().map(e -> money(e.getText())).toList();
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    private WebElement product(String name) {
        visible(ITEM);
        return all(ITEM).stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().trim().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No product named '" + name + "'"));
    }

    static BigDecimal money(String label) {
        return new BigDecimal(label.replace("$", "").trim());
    }
}
