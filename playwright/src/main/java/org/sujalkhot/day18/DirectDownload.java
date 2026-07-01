package org.sujalkhot.day18;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;

public class DirectDownload {

	public static void main(String[] args) {

		Page page = BrowserFactory.initBroswer("chrome", false, true);

		// Download File
		Download download = page.waitForDownload(() -> {
			page.evaluate("window.open('https://the-internet.herokuapp.com/download/test.txt')");
		});

		// save file
		download.saveAs(Path.of("src/test/resources/file.pdf"));

	
	}

}
