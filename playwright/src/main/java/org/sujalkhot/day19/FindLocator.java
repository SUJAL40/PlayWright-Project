package org.sujalkhot.day19;

import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class FindLocator {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoV1/");
		page.locator("#username").fill("admin");
		page.locator("#password").fill("admin123");
		page.locator(".btn.primary").click();

		// Getting all product title
		List<Locator> productTitle = page.locator(".prod-title").all();
		for (Locator title : productTitle) {
			System.out.println(title.textContent());
		}
		System.out.println("***************************************************");

		// Getting all product title one time
		List<String> titletext = page.locator(".prod-title").allTextContents();
		System.out.println(titletext);

		// close the browser
		BrowserFactory.closeBrowser();

	}

}
