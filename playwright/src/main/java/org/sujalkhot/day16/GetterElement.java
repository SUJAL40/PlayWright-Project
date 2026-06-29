package org.sujalkhot.day16;

import com.microsoft.playwright.Page;

public class GetterElement {

	public static void main(String[] args) {
		Page page = BrowserFactory.initBroswer("edge", true, false);
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoV1/");
		System.out.println("**********************************************************************************");
		System.out.println(page.title());
		System.out.println(page.locator("//*[@id='loginForm']/label[1]").textContent().trim());
		System.out.println(page.locator("//*[@id='loginForm']/label[1]").innerText().trim());
		System.out.println(page.locator("//*[@id='loginForm']/label[1]").innerHTML());
		System.out.println(page.locator("//*[@id='loginForm']/label[1]/input[1]").getAttribute("id"));
		page.getByPlaceholder("username").fill("admin");
		System.out.println(page.getByPlaceholder("username").inputValue());
		System.out.println(page.locator("//*[@id='loginForm']/label").count());
		BrowserFactory.closeBrowser();
	}

}
