package nonPOM;

import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TestAutomation {

	public static void main(String[] args) throws InterruptedException {
		Playwright playwright = Playwright.create();
		BrowserType chromium = playwright.chromium();

		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		Page page = browser.newPage();

		BrowserContext context = browser.newContext();

		page.navigate("https://login.vrqaacademy.co.in/index.html");

		page.fill("#loginUsername", "admin");
		page.fill("#loginPassword", "admin123");
		page.click("[type='submit']");

		page.locator("[data-id='p3']").first().click();
		page.click("#cartBtn");
		
		page.click("#checkoutBtn");
		page.fill("#fullName", "Sujal");
		page.fill("#address", "sdfja;s skdfj");
		page.fill("#cardNumber", "1234567890123456");
		page.fill("#expiry", "12/23");
		page.fill("#cvc", "123");

		page.click(".btn.primary");
		page.click("[onclick='confirmPayment()']");

		String orderdetails = page.locator("#orderDetails").innerText();
		System.out.println(orderdetails);

		Thread.sleep(5000);

		browser.close();
		playwright.close();

		System.out.println("Execution is over ....................");
	}
}
