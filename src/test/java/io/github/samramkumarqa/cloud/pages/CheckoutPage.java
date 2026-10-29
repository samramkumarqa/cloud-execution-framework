package io.github.samramkumarqa.cloud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME = testId("firstName");
    private static final By LAST_NAME = testId("lastName");
    private static final By POSTAL_CODE = testId("postalCode");
    private static final By CONTINUE = testId("continue");
    private static final By ITEM_TOTAL = testId("subtotal-label");
    private static final By TAX = testId("tax-label");
    private static final By TOTAL = testId("total-label");
    private static final By FINISH = testId("finish");
    private static final By COMPLETE_HEADER = testId("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage enterDetails(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE);
        return this;
    }

    public BigDecimal itemTotal() {
        return amount(ITEM_TOTAL);
    }

    public BigDecimal tax() {
        return amount(TAX);
    }

    public BigDecimal total() {
        return amount(TOTAL);
    }

    public String finish() {
        click(FINISH);
        return text(COMPLETE_HEADER);
    }

    private BigDecimal amount(By label) {
        String t = text(label);
        return new BigDecimal(t.substring(t.indexOf('$') + 1).trim());
    }
}
