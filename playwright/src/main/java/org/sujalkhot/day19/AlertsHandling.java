package org.sujalkhot.day19;

import com.microsoft.playwright.Page;

public class AlertsHandling {
	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://swautomationtester-dot.github.io/VRQA/labs/alerts.html");

		// OK Alert
//		page.onceDialog(dialog -> {
//			System.out.println("Alert message: " + dialog.message());
//			dialog.accept();
//		});
//		page.locator("#nativeAlert").click();

//		// Confirmation Alert - Accept
//		page.onceDialog(dialog -> {
//			System.out.println(dialog.message());
//			dialog.accept(); // OK
//		});
//		page.click("#nativeConfirm");
//		System.out.println("Accept");
//
//		// Confirmation Alert - Dismiss
//		page.onceDialog(dialog -> {
//			System.out.println(dialog.message());
//			dialog.dismiss(); // Cancel
//		});
//		page.click("#nativeConfirm");
//		System.out.println("Dismiss");

//		page.onDialog(dialog -> {
//			System.out.println(dialog.message());
//			dialog.accept("Playwright Java");
//		});
//		
//		page.click("#nativePrompt");
		
		//Modern Model/Toast
		page.locator("#modernConfirm").click();
		page.locator("#modalCancel").click();
		
		String toastMessage = page.locator("//*[@id='toastContainer']/div").innerText();
		System.out.println("Toast Message : "+toastMessage);

//		BrowserFactory.closeBrowser();
	}

}
