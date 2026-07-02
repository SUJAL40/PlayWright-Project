package tests;

import base.BaseTest;
import pages.CartPage;
import pages.CheckOutPage;
import pages.LoginPage;
import pages.ProductPage;

public class PlaceOrderTest extends BaseTest {

	public static void main(String[] args) {

		PlaceOrderTest test = new PlaceOrderTest();
		test.setup();

		// Create Page Objects
		LoginPage loginPage = new LoginPage(test.page);
		ProductPage productPage = new ProductPage(test.page);
		CartPage cartPage = new CartPage(test.page);
		CheckOutPage checkoutPage = new CheckOutPage(test.page);

		// Test flow
		loginPage.launchURL("https://login.vrqaacademy.co.in/index.html");
		loginPage.login("admin", "admin123");

		productPage.addFirstProductToCart();
		productPage.goToCart();

		cartPage.ClickCheckOut();

		checkoutPage.enterPaymentDetails("Sujal Khot", "lane 1, Ichal", "1234 5678 8901 1233", "12/12", "123");
		checkoutPage.payAndConfirm();

		String orderDetails = checkoutPage.getOrderDetails();
		System.out.println("Order Details:\n" + orderDetails);

		test.tearDown();
	}

}
