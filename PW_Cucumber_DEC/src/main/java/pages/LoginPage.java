package pages;

import com.microsoft.playwright.Page;

public class LoginPage {

    private Page page;

    // Locators
    private String username = "#loginUsername";
    private String password = "#loginPassword";
    private String loginBtn = "[type='submit']";

    public LoginPage(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate("https://login.vrqaacademy.co.in/index.html");
    }

    public void login(String user, String pass) {
        page.fill(username, user);
        page.fill(password, pass);
        page.click(loginBtn);
    }
}
