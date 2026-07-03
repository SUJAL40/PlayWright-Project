package pages;

import com.microsoft.playwright.Page;

public class CheckoutPage {

    private Page page;

    private String fullName = "#fullName";
    private String address = "#address";
    private String cardNumber = "#cardNumber";
    private String expiry = "#expiry";
    private String cvc = "#cvc";
    private String payBtn = "#payBtn";
    private String confirmBtn = "[onclick='confirmPayment()']";
    private String orderDetails = "#orderDetails";

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void enterPaymentDetails(String name, String addr, String card, String exp, String cvv) {
        page.fill(fullName, name);
        page.fill(address, addr);
        page.fill(cardNumber, card);
        page.fill(expiry, exp);
        page.fill(cvc, cvv);
    }

    public void payAndConfirm() {
        page.click(payBtn);
        page.click(confirmBtn);
    }

    public String getOrderDetails() {
        return page.locator(orderDetails).innerText();
    }
}
