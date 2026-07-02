package base;

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

	public void setup() {
		playwright = Playwright.create();

		browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(2000));
		page = browser.newPage();

		context = browser.newContext();
	}

	public void tearDown() {
		context.close();
		browser.close();
		playwright.close();
	}
}
