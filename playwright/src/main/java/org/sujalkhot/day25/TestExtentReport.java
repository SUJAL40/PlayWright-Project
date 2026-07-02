package org.sujalkhot.day25;

import java.nio.file.Paths;

import org.sujalkhot.BrowserFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;

public class TestExtentReport {

	public static void main(String[] args) {

		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("http://vrqaacademy.co.in/labs/tables.html");

		// 1. Setup Extent
		ExtentSparkReporter reporter = new ExtentSparkReporter("ExtentReports//report.html");
		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);

		// 2. Create test
		ExtentTest test = extent.createTest("Login Test").assignAuthor("Sujal Khot");
		test.assignCategory("Smoke");
		
		reporter.config().setDocumentTitle("Extent ReportDemo");
		reporter.config().setReportName("Swaglabs Login test");
		reporter.config().setTheme(Theme.DARK);

		extent.setSystemInfo("Application", "Swag Labs");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("Browser", "Chrome");
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Tester", "Venkat");

		// 3. Test steps
		page.navigate("https://www.saucedemo.com/v1/index.html");
		test.info("Navigate to site");

		page.locator("input[type='text']").fill("standard_user");
		test.info("Entered username");

		page.locator("#password").fill("secret_sauce");
		test.info("Enterted Password");

		page.locator("#login-button").click();
		test.info("Clicked login");

		// 4. Screenshot
		String path = "screenshots//ExtentReportScreenshot//screenshot.png";
		page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
		test.addScreenCaptureFromPath(path);

		// 5. Log results
		test.pass("Login Succesfully");

		// 6. Flush report -- it is main thing
		extent.flush();

		BrowserFactory.closeBrowser();
	}

}
