package edu.csupomona.cs480;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


/*
 * Assignment 6
 * Author: Marion Levy
 */

public class TestSortThree {
	
	private WebDriver driver;
	
	// Enter test numbers here
	private int num1 = 5;
	private int num2 = 9;
	private int num3 = 2;

	@Before
	public void setUp() {
		driver = new HtmlUnitDriver();
		driver.get("http://localhost:8080/sortThree/" + String.valueOf(num1) + "/" + String.valueOf(num2) + "/" + String.valueOf(num3));
	}
	
	@Test
	public void Test() {
		String sortedPage = driver.getPageSource();
		String sortedTest = sortThree(num1, num2, num3);
		assertEquals(sortedTest, sortedPage);
	}
	
	
	// This method swaps the values of each variable until they are listed in order
	// when printed as n1, n2, n3
	public String sortThree(int n1, int n2, int n3) {

		if (n1 > n2) {
			int temp = num1;
		    n1 = n2;
		    n2 = temp;
		}

		if (n2 > n3) {
			int temp = n2;
		    n2 = n3;
		    n3 = temp;
		}

		if (n1 > n2) {
			int temp = n1;
		    n1 = n2;
		    n2 = temp;
		}
		    
		return "[" + String.valueOf(n1) + ", " + String.valueOf(n2) + ", " + String.valueOf(n3) + "]";
		
	}
}
