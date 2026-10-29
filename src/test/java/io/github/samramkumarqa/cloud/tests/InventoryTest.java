package io.github.samramkumarqa.cloud.tests;

import io.github.samramkumarqa.cloud.pages.InventoryPage;
import io.github.samramkumarqa.cloud.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void logIn() {
        inventory = new LoginPage(driver()).open().loginAs("standard_user", "secret_sauce");
    }

    @Test(groups = "smoke")
    public void showsSixProducts() {
        assertThat(inventory.itemCount()).isEqualTo(6);
    }

    @Test
    public void everyProductCanBeAddedToTheCart() {
        assertThat(inventory.addToCartButtonCount()).isEqualTo(6);
    }

    @Test
    public void sortsByPriceLowToHigh() {
        inventory.sortBy("Price (low to high)");
        List<BigDecimal> prices = inventory.prices();
        assertThat(prices).hasSize(6).isSortedAccordingTo(Comparator.naturalOrder());
    }
}
