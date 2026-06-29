package org.sujalkhot.day13;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class LauchChrome {

	public static void main(String[] args) {
		Playwright pw = Playwright.create();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		System.out.println("Height : " + height + " Widht : " + width);

		Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

		BrowserContext context = browser
				.newContext(new Browser.NewContextOptions().setViewportSize((int) width, (int) height)
				// view port means screen size
				);

		Page page = context.newPage();
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoSite/index.html");
		page.pause();

	}

}
