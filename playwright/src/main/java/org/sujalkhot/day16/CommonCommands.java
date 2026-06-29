package org.sujalkhot.day16;

import java.nio.file.Paths;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CommonCommands {

	public static void main(String[] args) {

		Page page = BrowserFactory.initBroswer("edge", false, false);
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoV1/");
		System.out.println("**********************************************************************************");
		System.out.println(page.title());
		System.out.println("**********************************************************************************");
		System.out.println(page.url());
		System.out.println("**********************************************************************************");
		System.out.println(page.content());
		System.out.println("**********************************************************************************");
		System.out.println(page.context());	//com.microsoft.playwright.impl.BrowserContextImpl@3b938003 it may change
		System.out.println("**********************************************************************************");
		System.out.println(page.viewportSize());
		BrowserFactory.closeBrowser();

	}

}
