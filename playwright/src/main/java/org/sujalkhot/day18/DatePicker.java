package org.sujalkhot.day18;

import com.microsoft.playwright.Page;

public class DatePicker {
	
	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://swautomationtester-dot.github.io/isha-automation-ui2.0/");
		page.fill("#datePicker","2005-06-07");
		
		
	}

}
