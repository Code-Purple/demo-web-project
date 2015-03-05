package edu.csupomona.cs480;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParser
{
	String parsedText;
	private final String query;
	
	public static class HTMLParserBuilder 
	{	
		private String query;

		// constructor
		public HTMLParserBuilder()
		{

		}

		public HTMLParserBuilder setQuery(final String query)
		{
			this.query = query;
			return this;
		}

		public HTMLParser createParser()
		{
			return new HTMLParser(query);
		}
	}

	// private constructor for builder
	private HTMLParser(final String query) 
	{
		this.query = query;
	}

	public void ParseURL() 
	{
		Document doc;
		parsedText += "***TOP GOOGLE SEARCH RESULTS FOR QUERY***<br><br>";

		try 
		{
			doc = Jsoup.connect("https://www.google.com/search?q=" + query).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();

			Elements links = doc.select("h3[class=r]");
			for (Element link : links)
			{
				String text = link.html();
				if (text.contains("<a href=\"/url?q="))
				{
					text = text.replace("<a href=\"/url?q=", "");
					text = text.replaceAll("(&amp;sa=U&amp;ei=).*", "");

					parsedText += text + "<br>";
				}
			}
		} catch (IOException e) 
		{
		}
	}
	
	public String toString()
	{
		return parsedText;
	}

}
