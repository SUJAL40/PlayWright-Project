package org.sujalkhot.day21;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ScreenShotTest {
	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
//		page.navigate("https://vrqaacademy.co.in/labs/inputs.html");

		// get full page screenshot
//		page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/fullpage.png")).setFullPage(true));
		// get view port page screenshot
//		page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/viewportpage.png")));

		// Screenshot of particular locator
//		page.navigate("https://vrqaacademy.co.in");
//		Locator icon = page.locator("[class='lab-icon']").first();
//		icon.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("screenshots/Element.png")));
		
		//Use Reusable methods
		page.navigate("https:vrqaacademy.co.in/labs/uploads.html");
		takeScreenshot(page, "Uploads_page", true);
		takeScreenshot(page, "Uploads_Page_ViewPort", false);
		takeScreenshotElement(page, "#chooseBtn", "Uploads_Page_Element");

		// close browser
		BrowserFactory.closeBrowser();
	}

	// create a reusable screenshot method
	public static void takeScreenshot(Page page, String name,boolean fullscreen) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		Path path = Paths.get("screenshots/" + name + "_" + timestamp + ".png");
		page.screenshot(new Page.ScreenshotOptions().setPath(path).setFullPage(fullscreen));
	}

	// create a reusable screenshot method
	public static void takeScreenshotElement(Page page, String locator, String name) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		Path path = Paths.get("screenshots/" + name + "_" + timestamp + ".png");
		Locator element = page.locator(locator);
		element.screenshot(new Locator.ScreenshotOptions().setPath(path));
	}

}
