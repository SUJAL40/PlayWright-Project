package org.sujalkhot.day16;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {

	// encapsulation
	private static Playwright playwright;
	private static Browser browser;

	public static Page initBroswer(String browserName, boolean headless, boolean fullScreen) {

		playwright = Playwright.create();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));

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
			option.setChannel("chrome");
			browser = playwright.chromium().launch(option);
			break;

		case "edge":
			option.setChannel("msedge");
			browser = playwright.chromium().launch(option);
			break;

		case "firefox":
			browser = playwright.firefox().launch(option);
			break;

		case "webkit":
			browser = playwright.webkit().launch(option);
			break;

		default:
			throw new IllegalArgumentException("Invalid Browser : " + browserName);

		}

		BrowserContext context;

		if (fullScreen && (browserName.equalsIgnoreCase("Chromium") || browserName.equalsIgnoreCase("Chrome")
				|| browserName.equalsIgnoreCase("edge"))) {
			context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
		} else if (fullScreen && (browserName.equalsIgnoreCase("firefox") || browserName.equalsIgnoreCase("webkit"))) {
			context = browser.newContext(new Browser.NewContextOptions().setViewportSize((int) width, (int) height));
		} else {
			context = browser.newContext(new Browser.NewContextOptions());
		}

		return context.newPage();
	}

	public static void closeBrowser() {
		
		if (playwright != null) {
			playwright.close();
		}
	}

}
