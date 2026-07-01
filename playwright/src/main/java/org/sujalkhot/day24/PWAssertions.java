package org.sujalkhot.day24;

import org.sujalkhot.BrowserFactory;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PWAssertions {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);

		page.navigate("https://vrqaacademy.co.in/");
		
		assertThat(page).hasTitle("VR QA Academy - Automation Practice – Labs Dashboard");
		System.out.println("Assert Succesfully..........");
		
		BrowserFactory.closeBrowser();

	}

}
