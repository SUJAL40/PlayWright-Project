package org.sujalkhot;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class LaunchBrowser {

	public static void main(String[] args) {

		// Create Playwright instance
		// Playwright is the entry point for launching and controlling browsers
		Playwright pw = Playwright.create();

		// Launch Chromium browser
		// By default Playwright runs in headless mode (without UI)
		// setHeadless(false) opens the browser with UI (headed mode)
		Browser browser = pw.chromium()
				.launch(new BrowserType.LaunchOptions().setHeadless(false));

		// Create a new browser tab (Page)
		Page page = browser.newPage();

		// Navigate to the application URL
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoSite/index.html");
		page.pause();

		// Print the title of the current webpage in the console
		System.out.println(page.title());

		// Enter username in the username textbox
		// '#' represents ID selector in CSS
		page.fill("#user-name", "standard_user");

		// Enter password in the password textbox
		page.fill("#password", "secret_sauce");

		// Click on Login button
		page.click("#login-button");

		// Close the browser window
		browser.close();

		// Close Playwright instance and release resources
		pw.close();
	}
}