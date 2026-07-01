package org.sujalkhot.day18;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class FileChooser {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://www.leafground.com/file.xhtml;jsessionid=node010nx464jtc8vzxsmrg4fp0yu9138.node0");

		page.setInputFiles("[multiple='multiple']",
				new Path[] { Paths.get("src\\test\\resources\\files\\Playwright.png"),
						Paths.get("src\\test\\resources\\files\\Playwright.png") });
		
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload")).click();


	}

}
