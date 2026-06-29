package org.sujalkhot.day13;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TextContext {

	public static void main(String[] args) {
		Playwright pw = Playwright.create();

		Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

		// Context for Admin
		BrowserContext adminContext = browser.newContext();

		// Context for User
		BrowserContext userContext = browser.newContext();

		// Pages
		Page adminPage = adminContext.newPage();
		Page userPage = userContext.newPage();

		adminPage.navigate("https://amazon.com");
		userPage.navigate("https://flipkart.com");

		adminContext.close();
		userContext.close();
		browser.close();
	}

}
