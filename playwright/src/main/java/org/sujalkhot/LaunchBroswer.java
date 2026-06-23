package org.sujalkhot;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class LaunchBroswer {

	public static void main(String[] args) {

		// create and launch playwrite
		//this is play write instance and entry point for all browser
		Playwright pw = Playwright.create();

		// by default browser open in headless mode we need to tell
		Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

		// create a tab
		Page page = browser.newPage();

		// launch a url
//		page.navigate("https://www.sahaasfoundation.com");
		page.navigate("https://www.saucedemo.com");

		System.out.println(page.title());

//		page.fill("input[id='user-name']", "standard_user");
		page.fill("#user-name", "standard_user");
		page.fill("#password", "secret_sauce");
		page.click("#login-button");

		browser.close();
		pw.close();	//here we need to close the browser

	}

}
