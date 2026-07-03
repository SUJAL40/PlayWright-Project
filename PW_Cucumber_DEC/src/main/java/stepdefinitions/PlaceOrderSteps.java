package stepdefinitions;

import base.BaseTest;
import hooks.Hooks;
import io.cucumber.java.en.*;
import pages.*;

public class PlaceOrderSteps {

    LoginPage loginPage = new LoginPage(BaseTest.page);
    ProductPage productPage = new ProductPage(BaseTest.page);
    CartPage cartPage = new CartPage(BaseTest.page);
    CheckoutPage checkoutPage = new CheckoutPage(BaseTest.page);

    @Given("user is on login page")
    public void user_is_on_login_page() {
        Hooks.test.get().info("Opening login page");
        loginPage.open();
    }

    @When("user logs in with valid credentials")
    public void user_logs_in() {
        Hooks.test.get().info("Logging in");
        loginPage.login("admin", "admin123");
    }

    @And("user adds product to cart")
    public void user_adds_product() {
        productPage.addFirstProductToCart();
        productPage.goToCart();
    }

    @And("user completes checkout")
    public void user_completes_checkout() {
        cartPage.clickCheckout();

        checkoutPage.enterPaymentDetails(
                "Ramesh",
                "1st Cross Street, Chennai",
                "1234 5647 4856 4656",
                "12/26",
                "123"
        );

        checkoutPage.payAndConfirm();
    }

    @Then("order should be placed successfully")
    public void order_should_be_placed() {
        String order = checkoutPage.getOrderDetails();
        System.out.println(order);
        Hooks.test.get().pass("Order placed successfully");
    }
}
