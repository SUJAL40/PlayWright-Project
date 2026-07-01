package org.sujalkhot.day21;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

public class FailTraceViewer {

	public static void main(String[] args) {

		try (Playwright playwright = Playwright.create()) {

			Browser browser = playwright.chromium()
					.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));

			BrowserContext context = browser.newContext();

			Page page = context.newPage();

			page.setDefaultTimeout(5000); //we overrided default timeout to 5sec

			// START TRACE
			context.tracing()
					.start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));

			try {

				page.navigate("https://vrqaacademy.co.in/");

				page.click("text=Text fields, password, validation, forms");

				try {
					page.fill("#in-firstna", "Venkat"); // Wrong locator
				} catch (Exception e) {
					System.out.println("First name not found");
				}

				try {
					page.fill("#in-lastna", "Raghavan"); // Wrong locator
				} catch (Exception e) {
					System.out.println("Last name not found");
				}

				try {
					page.fill("#in-emailxxx", "test@test.com"); // Wrong locator
				} catch (Exception e) {
					System.out.println("Email not found");
				}

			} finally {

				// STOP TRACE
				context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
			}

			context.close();
			browser.close();
		}
	}
}
