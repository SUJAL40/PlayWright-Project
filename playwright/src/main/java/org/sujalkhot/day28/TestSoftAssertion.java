package org.sujalkhot.day28;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class TestSoftAssertion {

	public static void main(String[] args) {

		SoftAssert sa = new SoftAssert();
		
		Assert.assertEquals("Dashboard", "Dashboard123");

		sa.assertEquals("Login", "Login");
		System.out.println("Execute 1 is over");
		
		sa.assertEquals("Login12", "Login");
		System.out.println("Execute 2 is over");
		
		sa.assertEquals("Login12", "12Login");
		System.out.println("Execute 3 is over");
		
//		sa.assertAll();						//Mandatory
		
		System.out.println("All assertion passed.........");

	}

}
