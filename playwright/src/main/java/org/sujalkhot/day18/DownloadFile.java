package org.sujalkhot.day18;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;

public class DownloadFile {

	public static void main(String[] args) {

		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://rahulshettyacademy.com/upload-download-test");

		// Download File
		Download download = page.waitForDownload(() -> {
			page.locator("text=Download").click();
		});

		// save file
		Path downloadedFile = Paths.get("downloads", download.suggestedFilename());
		download.saveAs(downloadedFile);

		System.out.println("Downloaded File to : " + downloadedFile.toAbsolutePath());
		//Absolute path give exact path of our file
		//Downloaded File to : C:\Users\sujal\OneDrive\Documents\PlayWright Project\PlayWright-Project\playwright\downloads\download.xlsx

	}

}
