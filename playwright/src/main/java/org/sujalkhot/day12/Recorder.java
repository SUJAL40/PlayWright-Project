package org.sujalkhot.day12;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;


public class Recorder {

	public static void main(String[] args) {

		Playwright pw = Playwright.create();
		Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
		Page page = browser.newPage();
		page.navigate("https://swautomationtester-dot.github.io/IshaDemoSite/index.html");
		assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username"))).isVisible();
		assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"))).isVisible();
		assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"))).isVisible();
		assertThat(page.getByText("Demo credentials:")).isVisible();
		assertThat(page.getByText("admin", new Page.GetByTextOptions().setExact(true))).isVisible();
		assertThat(page.getByText("admin123")).isVisible();
		assertThat(page.getByText("This is a static demo for")).isVisible();
		assertThat(page.getByRole(AriaRole.HEADING)).containsText("ISHA Automation");
		assertThat(page.locator("#loginForm")).containsText("Username");
		assertThat(page.locator("#loginForm")).containsText("Password");
		assertThat(page.getByRole(AriaRole.BUTTON)).containsText("Login");
		assertThat(page.getByRole(AriaRole.STRONG)).containsText("Demo credentials:");
		assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username"))).isEmpty();
		assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"))).isEmpty();
		assertThat(page.getByRole(AriaRole.BUTTON)).matchesAriaSnapshot("- button \"Login\"");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill("admin");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("admin123");
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
		assertThat(page.getByText("Isha Backpack")).isVisible();
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).first().click();
		page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart:")).click();
		assertThat(page.getByText("Isha Backpack")).isVisible();
		assertThat(page.locator("#cartContainer").getByText("₹")).isVisible();
		assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed to Checkout")))
				.isVisible();
		page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed to Checkout")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Full name")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Full name")).fill("test");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Address")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Address")).fill("testset");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Card Number")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Card Number")).fill("123456789");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Expiry (MM/YY)")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Expiry (MM/YY)")).fill("12/43");
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("CVC")).click();
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("CVC")).fill("432");
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Pay Now")).click();
		assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Thank you — your order is")))
				.isVisible();
		assertThat(page.locator("h2")).containsText("Thank you — your order is placed!");
		assertThat(page.getByRole(AriaRole.LISTITEM)).containsText("Isha Backpack × 1 — ₹29.99");
		assertThat(page.locator("#orderDetails")).containsText("Total: ₹31.49");
		page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Shop more")).click();
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();
		System.out.println("Execution is completed...........");

	}
}
