package org.sujalkhot.day28;

import org.testng.annotations.Test;

public class TestNGAnnotation {

	// test case 1
	@Test(groups = "Smoke")
	public void testCase1() {
		System.out.println("Test case 1");
	}

	// test case 2
	@Test
	public void testCase2() {
		System.out.println("Test case 2");
	}

	// test case 2
//	@Test(dependsOnMethods = "testCase2")
	@Test(groups = "Smoke")
	public void Alpha() {
		System.out.println("Alpha.....");
	}

}
