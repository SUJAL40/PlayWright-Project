package org.sujalkhot.day23;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.sujalkhot.BrowserFactory;

import com.microsoft.playwright.Page;

public class ReadProperties {

	public static void main(String[] args) throws IOException {

		// Step 1 : setup the path of the properties file
		FileInputStream fis = new FileInputStream("src\\main\\resources\\properties\\config.properties");

		// Step 2 : Create Object for Properties class
		Properties prop = new Properties();

		// Step 3 : load the properties file
		prop.load(fis);

		// Step 4 : to read the property
		System.out.println(prop.getProperty("username"));
		System.out.println(prop.getProperty("password"));

		// Actual code
		Page page = BrowserFactory.initBroswer("chrome", false, true);

		page.navigate(prop.getProperty("url"));
		page.fill("#username", prop.getProperty("username"));
		page.fill("#password", prop.getProperty("password"));
		page.click("text='Login'");

		String id = "test";
		// Create output stream
		FileOutputStream fos = new FileOutputStream("src\\main\\resources\\properties\\config.properties");

		// Write data
		prop.setProperty("Password", id);

		// Save to file
		prop.store(fos, "Updating Password");

	}

}
