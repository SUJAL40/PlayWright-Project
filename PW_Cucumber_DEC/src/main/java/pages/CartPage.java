package pages;

import com.microsoft.playwright.Page;

public class CartPage {

    private Page page;

    private String checkoutBtn = "#checkoutBtn";

    public CartPage(Page page) {
        this.page = page;
    }

    public void clickCheckout() {
        page.click(checkoutBtn);
    }
}
