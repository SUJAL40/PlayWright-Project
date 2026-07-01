package org.sujalkhot.day21;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

public class TracingTest {

	public static void main(String[] args) {
		Playwright playwright = Playwright.create();

		Browser browser = playwright.chromium()
				.launch(new BrowserType.LaunchOptions().setHeadless(true).setSlowMo(1000));

		BrowserContext context = browser.newContext();

		Page page = context.newPage();

		// Start Trace Recording
		context.tracing().start(
				new Tracing.StartOptions()
				.setScreenshots(true)
				.setSnapshots(true)
				.setSources(true));

		// Test Steps
		page.navigate("https://vrqaacademy.co.in/");

		page.locator("text='Text fields, password, validation, forms'").click();
		page.fill("#in-firstname", "Sujal");
		page.fill("#in-lastname", "Khot");

		// Stop Trace & Save File
		context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("traces/Test_example_trace.zip")));

		// Close Resources
		context.close();
		browser.close();
		playwright.close();
		
		//After all done go to terminal -> click on zip file -> Show In -> Terminal 
		//add command :  npx playwright show-trace Name_Of_Zip_File
		// eg. npx playwright show-trace Test_example_trace.zip
		//It open trace viewer , there we can see all performed action

		System.out.println("Trace saved successfully.");

	}

}
