package org.sujalkhot.day20;

import com.microsoft.playwright.Page;

public class JSPlaywright {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("chrome", false, true);
		page.navigate("https://swautomationtester-dot.github.io/isha-automation-ui2.0/");
		page.evaluate("document.querySelector(\"#readonly-input\").value = 'Sujal';");
		page.evaluate("document.querySelector('#datePicker').value = '2026-01-15'");
		page.evaluate("document.getElementById('in-firstname').value='admin';");
		page.locator("#validateInputs").evaluate("el=>el.style.border = '3px solid red'"); //here we use locator
		page.evaluate("document.getElementById('generateInputs').style.border = '3px solid red';"); //here we use DOM element
//		page.locator("text='Shadow Login'").scrollIntoViewIfNeeded();
		
		

	}

}
