package org.sujalkhot.day21;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class RecordVideo {

	public static void main(String[] args) {
		Playwright playwright = Playwright.create();

		Browser browser = playwright.chromium().launch();

		BrowserContext context = browser.newContext(
				new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")).setRecordVideoSize(1280, 720));

		Page page = context.newPage();

		page.navigate("https://vrqaacademy.co.in/");
		page.locator("text='Text fields, password, validation, forms'").click();

		// take screenshot
		page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/example.png")));

		// close context
		context.close();

		// browser close
		browser.close();

		// close playwright
		playwright.close();

	}

}
