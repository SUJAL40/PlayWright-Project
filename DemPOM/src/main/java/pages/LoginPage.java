package pages;

import com.microsoft.playwright.Page;

public class LoginPage {

	private Page page;

	public LoginPage(Page page) {
		this.page = page;
	}

	// Locator
	private String username = "#loginUsername";
	private String password = "#loginPassword";
	private String loginBtn = "[type='submit']";

	public void launchURL(String URL) {
		page.navigate(URL);
	}

	public void login(String user, String pass) {
		page.fill(username, user);
		page.fill(password, pass);
		page.click(loginBtn);
	}

}
