package org.sujalkhot.day20;

import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Keyboard.PressOptions;
import com.microsoft.playwright.Keyboard.TypeOptions;

public class ActionsTest {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoSite/");
//		page.type("#username", "venkat"); //this type() is deprecated

		page.locator("#username").click();

		page.keyboard().type("Hello World", new Keyboard.TypeOptions().setDelay(300));
		page.keyboard().type("Sujal");

		page.waitForTimeout(2000);

		page.locator("#username").press("Control+A");

		page.waitForTimeout(2000);

		page.locator("#username").press("Delete");

		page.waitForTimeout(5000);

	}

}
