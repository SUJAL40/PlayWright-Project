package base;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.*;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("reports/AutomationReport.html");
            reporter.config().setTheme(Theme.DARK);
            reporter.config().setDocumentTitle("Playwright Cucumber Report");
            reporter.config().setReportName("VR QA Automation");

            extent = new ExtentReports();
            extent.attachReporter(reporter);

            extent.setSystemInfo("Browser", "Chromium");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Tester", "Venkat");
        }
        return extent;
    }
}
