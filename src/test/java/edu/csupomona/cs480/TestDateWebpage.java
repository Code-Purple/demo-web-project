package edu.csupomona.cs480;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * For assignment 6
 * @author Chantal
 *
 */
public class TestDateWebpage 
{
	private WebDriver d;
	
	@Before
	public void setUp() 
	{
		// Create a new instance of the html unit driver
		d = new HtmlUnitDriver();

		//Navigate to desired web page
		d.get("http://localhost:8080/cs480/date");
	}
	
	@Test
	public void Test()
	{
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy");
	
		verifyDate(f.format(d));
	}
		
	public void verifyDate(String expectedDate)
	{
		String e = d.getPageSource();
		assertEquals("Date: ", expectedDate, e);
	}
}
