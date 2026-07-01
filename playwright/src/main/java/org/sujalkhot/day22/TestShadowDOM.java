package org.sujalkhot.day22;

import org.sujalkhot.BrowserFactory;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;

public class TestShadowDOM {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://vrqaacademy.co.in/labs/shadow.html");
		page.setDefaultTimeout(5000);
//		page.locator("[id='user']").first().fill("Sujal");
//		page.locator("[id='btn']").first().click();

		// nested shadow DOM
//		page.locator("[id='childInput']").first().fill("Sujal");
//		page.locator("[id='childBtn']").first().click();

		// nested shadow DOM using >> operator
//		page.locator("shadow-parent >> shadow-child >> #childInput").fill("Playwright");
//		page.locator("shadow-parent >> shadow-child >> #childBtn").click();

		// Using JS Evaluate
		// =========================================== BASIC ==========================
//		page.evaluate("""
//				() => {
//				    const host = document.querySelector("shadow-login");
//				    const root = host.shadowRoot;
//
//				    root.querySelector("#user").value = "Sujal";
//				    root.querySelector("#btn").click();
//				}
//				""");

//		SHADOW DOM INSIDE IFRAME - iframe
		FrameLocator frame = page.frameLocator("#shadowIframe");

		frame.locator("[id='user']").last().fill("Sujal");
		frame.locator("[id='btn']").last().click();

	}

}
