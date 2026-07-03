package tests;

import org.testng.TestListenerAdapter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.CheckOutPage;
import pages.LoginPage;
import pages.ProductPage;
import listeners.TestListeners;

@Listeners(TestListeners.class)
public class PlaceOrderTestParameter extends BaseTest {

	@DataProvider(name = "credentials")
	public static Object[][] data() {
		return new Object[][] { { "admin", "admin123" }, { "Sujal", "SK123" } };
	}

	@Test(dataProvider = "credentials")
	@Parameters({ "UserName", "Password" })
	public void PlaceOrderTestParameter(String user, String pass) {

//		PlaceOrderTestParameter test = new PlaceOrderTestParameter();
//		test.setup();	//here we used @BeforeTest

		// Create Page Objects
		LoginPage loginPage = new LoginPage(page);
		ProductPage productPage = new ProductPage(page);
		CartPage cartPage = new CartPage(page);
		CheckOutPage checkoutPage = new CheckOutPage(page);

		// Test flow
		loginPage.launchURL("https://login.vrqaacademy.co.in/index.html");
		loginPage.login(user, pass);

		productPage.addFirstProductToCart();
		productPage.goToCart();

		cartPage.ClickCheckOut();

		checkoutPage.enterPaymentDetails("Sujal Khot", "lane 1, Ichal", "1234 5678 8901 1233", "12/12", "123");
		checkoutPage.payAndConfirm();

		String orderDetails = checkoutPage.getOrderDetails();
		System.out.println("Order Details:\n" + orderDetails);

//		test.tearDown();		//here we used @AfterTest
	}

}
