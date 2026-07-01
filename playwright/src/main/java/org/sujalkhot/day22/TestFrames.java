package org.sujalkhot.day22;

import org.sujalkhot.BrowserFactory;

import com.microsoft.playwright.Page;

public class TestFrames {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://www.leafground.com/frame.xhtml");

		page.setDefaultTimeout(5000);

		page.frameLocator("[src=\"default.xhtml\"]").locator("[id='Click']").first().click();
		page.frameLocator("[src='page.xhtml']").frameLocator("#frame2").locator("[onclick='change()']").last().click();

	}

}
