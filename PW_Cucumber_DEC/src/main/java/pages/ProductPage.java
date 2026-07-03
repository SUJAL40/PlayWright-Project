package pages;

import com.microsoft.playwright.Page;

public class ProductPage {

    private Page page;

    private String firstProduct = "[data-id='p3']";
    private String cartBtn = "#cartBtn";

    public ProductPage(Page page) {
        this.page = page;
    }

    public void addFirstProductToCart() {
        page.locator(firstProduct).first().click();
    }

    public void goToCart() {
        page.click(cartBtn);
    }
}
