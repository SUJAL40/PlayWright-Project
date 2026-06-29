package org.sujalkhot.day14;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class DemoBrowserFactory {

	private static Playwright playwright;
	private static Browser browser;

	public static Page initBrowser(String browserName, boolean headless, boolean fullScreen) {

		playwright = Playwright.create();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		BrowserType.LaunchOptions option = new BrowserType.LaunchOptions().setHeadless(headless);

		if (fullScreen && (browserName.equalsIgnoreCase("Chromium") || browserName.equalsIgnoreCase("Chrome")
				|| browserName.equalsIgnoreCase("edge"))) {
			option.setArgs(Arrays.asList("--start-maximized"));
		}

		switch (browserName.toLowerCase()) {

		case "chromium":
			browser = playwright.chromium().launch(option);
			break;

		case "chrome":
			browser = playwright.chromium().launch(option.setChannel("chrome"));
			break;

		case "edge":
			browser = playwright.chromium().launch(option.setChannel("msedge"));
			break;

		case "firefox":
			browser = playwright.firefox().launch(option);
			break;

		case "webkit":
			browser = playwright.webkit().launch(option);
			break;

		default:
			throw new IllegalArgumentException("Invalid browser name : " + browserName);
		}

		// step 3 : create context

		BrowserContext context;

		if (fullScreen && (browserName.equalsIgnoreCase("Chromium") || browserName.equalsIgnoreCase("Chrome")
				|| browserName.equalsIgnoreCase("edge"))) {

			context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

		} else if (fullScreen && (browserName.equalsIgnoreCase("Chromium") || browserName.equalsIgnoreCase("Chrome"))) {
			context = browser.newContext(new Browser.NewContextOptions().setViewportSize((int) width, (int) height));
		} else {
			context = browser.newContext(new Browser.NewContextOptions());
		}

		return context.newPage();

	}

	// close browser
	public static void closeBrowser() {
		if (playwright != null) {
			playwright.close();
		}
	}

}
