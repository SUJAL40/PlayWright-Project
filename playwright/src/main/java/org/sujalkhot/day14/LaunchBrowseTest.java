package org.sujalkhot.day14;

import java.nio.file.Paths;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LaunchBrowseTest {

	public static void main(String[] args) {

		Page page = BrowserFactory.initBroswer("edge", false, false);
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoV1/");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill("admin");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("admin123");
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();

		System.out.println(page.title());
		// after running we got navigation full page screenshot
		// page.screenshot(new
		// Page.ScreenshotOptions().setPath(Paths.get("screenshots/fullpage.png")).setFullPage(true));

		// if you don't want full page
//		page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/fullpage.png")));
		page.waitForTimeout(2000);
		BrowserFactory.closeBrowser();
	}

}
