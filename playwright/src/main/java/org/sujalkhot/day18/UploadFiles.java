package org.sujalkhot.day18;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.playwright.Page;

public class UploadFiles {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
//		page.navigate("https://the-internet.herokuapp.com/upload");
		page.navigate("https://rahulshettyacademy.com/upload-download-test/");

		// upload file
		page.locator("input[type='file']")
				.setInputFiles(new Path[] { Paths.get("src\\test\\resources\\files\\Playwright.png"),
						Paths.get("src\\test\\resources\\files\\Playwright.png") });
		System.out.println("File Uploaded Successfully.....!");

		BrowserFactory.closeBrowser();

	}

}
