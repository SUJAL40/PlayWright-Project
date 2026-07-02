package pages;

import com.microsoft.playwright.Page;

public class CartPage {
	
	private Page page;
	
	public CartPage(Page page) {
		this.page = page;
	}
	
	//Locator
	private String checkoutBtn = "#checkoutBtn";
	
	public void ClickCheckOut() {
		page.locator(checkoutBtn).click();
	}

}
