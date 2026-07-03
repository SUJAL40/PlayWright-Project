package base;

import com.microsoft.playwright.*;

public class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    public static Page page;

    public static void initBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000)
        );
        context = browser.newContext();
        page = context.newPage();
    }

    public static void closeBrowser() {
        context.close();
        browser.close();
        playwright.close();
    }
}
