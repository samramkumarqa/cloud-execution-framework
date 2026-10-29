package io.github.samramkumarqa.cloud.tests;

import io.github.samramkumarqa.cloud.pages.CartPage;
import io.github.samramkumarqa.cloud.pages.CheckoutPage;
import io.github.samramkumarqa.cloud.pages.InventoryPage;
import io.github.samramkumarqa.cloud.pages.LoginPage;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutTest extends BaseTest {

    @Test(groups = "smoke")
    public void buysTwoProductsEndToEnd() {
        InventoryPage inventory = new LoginPage(driver()).open().loginAs("standard_user", "secret_sauce");

        inventory.addToCart("Sauce Labs Backpack");
        inventory.addToCart("Sauce Labs Fleece Jacket");
        inventory.waitForCartBadge(2);
        BigDecimal expectedItemTotal = inventory.priceOf("Sauce Labs Backpack")
                .add(inventory.priceOf("Sauce Labs Fleece Jacket"));

        CartPage cart = inventory.openCart();
        assertThat(cart.itemNames()).containsExactlyInAnyOrder("Sauce Labs Backpack", "Sauce Labs Fleece Jacket");

        CheckoutPage checkout = cart.checkout().enterDetails("Ada", "Lovelace", "10115");
        assertThat(checkout.itemTotal()).isEqualByComparingTo(expectedItemTotal);
        assertThat(checkout.total()).isEqualByComparingTo(checkout.itemTotal().add(checkout.tax()));
        assertThat(checkout.finish()).isEqualTo("Thank you for your order!");
    }
}
