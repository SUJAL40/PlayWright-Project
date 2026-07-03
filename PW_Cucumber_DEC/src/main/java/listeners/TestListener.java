package listeners;

import base.ExtentManager;
import com.aventstack.extentreports.*;
import com.microsoft.playwright.Page;

import java.nio.file.Paths;

import org.testng.*;

public class TestListener implements ITestListener {

	private static ExtentReports extent = ExtentManager.getInstance();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	public static ExtentTest getTest() {
		return test.get();
	}

	@Override
	public void onTestStart(ITestResult result) {
		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
		test.set(extentTest);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.get().pass("Test Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		test.get().fail(result.getThrowable());

		try {
			Object obj = result.getInstance();
			Page page = (Page) obj.getClass().getField("page").get(obj);

			String path = "reports/screenshots/" + result.getMethod().getMethodName() + ".png";
			page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));

			test.get().addScreenCaptureFromPath(path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}
}
