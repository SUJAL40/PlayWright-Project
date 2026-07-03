package hooks;

import base.BaseTest;
import base.ExtentManager;
import com.aventstack.extentreports.*;
import com.microsoft.playwright.Page;
import io.cucumber.java.*;
import java.nio.file.Paths;

public class Hooks {

    private static ExtentReports extent = ExtentManager.getInstance();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Before
    public void beforeScenario(Scenario scenario) {
        BaseTest.initBrowser();
        ExtentTest extentTest = extent.createTest(scenario.getName());
        test.set(extentTest);
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                String path = "reports/screenshots/" + scenario.getName().replace(" ", "_") + ".png";
                BaseTest.page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
                test.get().fail("Scenario Failed").addScreenCaptureFromPath(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            test.get().pass("Scenario Passed");
        }

        BaseTest.closeBrowser();
        extent.flush();
    }
}
