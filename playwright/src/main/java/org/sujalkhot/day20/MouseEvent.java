package org.sujalkhot.day20;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.MouseButton;

public class MouseEvent {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
//		//hover
//		page.navigate("https://vrqaacademy.co.in");
//		page.locator(".nav-dropbtn").hover();

		// Right Click
//		page.navigate("https://vrqaacademy.co.in/labs/right-click.html");
//		page.locator("text='Right Click Me'").click(
//		new Locator.ClickOptions().setButton(MouseButton.RIGHT));
		
		//double click
//		page.navigate("https://vrqaacademy.co.in/labs/double-click.html");
//		page.locator("text=Double Click Me").dblclick();
//		System.out.println("Double Click Perfomed...........");
		
		//drag and drop
//		page.navigate("https://vrqaacademy.co.in/labs/drag-drop.html");
//		page.dragAndDrop("#src", "#tgt");

//		page.waitForTimeout(2000);
//
//		BrowserFactory.closeBrowser();

	}

}
