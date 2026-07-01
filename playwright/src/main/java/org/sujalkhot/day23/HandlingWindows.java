package org.sujalkhot.day23;

import org.sujalkhot.BrowserFactory;

import com.microsoft.playwright.Page;

public class HandlingWindows {

	public static void main(String[] args) {

		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://www.leafground.com/window.xhtml");

		Page parent = page;

		Page child = page.waitForPopup(() -> {
			page.click("(//*[@class='ui-button-text ui-c'])[1]");
		});

		child.waitForLoadState();
		System.out.println("Child title: " + child.title());

		// Do action in child
		child.locator("text='Edit Profile'").scrollIntoViewIfNeeded();

		// Switch back to parent
		parent.bringToFront();
	}

}
