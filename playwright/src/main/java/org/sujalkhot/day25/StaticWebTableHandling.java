package org.sujalkhot.day25;

import org.sujalkhot.BrowserFactory;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class StaticWebTableHandling {

	public static void main(String[] args) throws InterruptedException {
		Page page = BrowserFactory.initBroswer("chrome", false, true);

		page.navigate("http://vrqaacademy.co.in/labs/tables.html");

		// Row Count
//		Locator rows = page.locator("#staticTable tbody tr");
		Locator rows = page.locator("//*[@id='staticTable']/tbody/tr");
		int rowCount = rows.count();
		System.out.println(rowCount);

		// Column Count
		Locator col = page.locator("#staticTable thead th");
		int colCount = col.count();
		System.out.println(colCount);

		// Read All Data from table
		Locator row = page.locator("#staticTable tbody tr");
		for (int i = 0; i < row.count(); i++) {
			Locator cols = row.nth(i).locator("td");

			for (int j = 0; j < cols.count(); j++) {
				System.out.print(cols.nth(j).textContent() + " | ");		//*[@id='staticTable']/tbody/tr[1]/td[0] 
			}
			System.out.println();
		}
		
		//above loop visualization //*[@id='staticTable']/tbody/tr[1]/td[0] 
		
		System.out.println(page.locator("//*[@id='staticTable']/tbody/tr[1]/td[2]").textContent());

		Thread.sleep(3000);

		BrowserFactory.closeBrowser();

	}

}
