package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BaseTest {

	protected static Playwright playwright;
	protected static Browser browser;
	protected BrowserContext context;
	protected Page page;

	@BeforeMethod
	public void setup() {
		playwright = Playwright.create();

		browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(2000));
		page = browser.newPage();

		context = browser.newContext();
	}

	@AfterMethod
	public void tearDown() {
		context.close();
		browser.close();
		playwright.close();
	}
}
