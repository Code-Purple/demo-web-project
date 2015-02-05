package edu.csupomona.cs480;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHtml 
{
	public String ParseURL(String query) 
	{
		Document doc;
		String returnText = "***TOP GOOGLE SEARCH RESULTS FOR QUERY***<br><br>";

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

					returnText += text + "<br>";
				}
			}
		} catch (IOException e) 
		{
			return "Error";
		}

		return returnText;
	}

}
