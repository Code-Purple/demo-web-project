package edu.csupomona.cs480;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class parseTest {
	private JsonParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new JsonParser();
	  }
	  
	@Test
	public void testJsonObjects() {
		String someJSON = "{item:foo,value:5}";
		JsonElement elem = parser.parse(someJSON);
		assertTrue(elem.isJsonObject());
		assertEquals("foo", elem.getAsJsonObject().get("item").getAsString());
		assertEquals(5, elem.getAsJsonObject().get("value").getAsInt());
		//fail("JSON object not properly parsed");
	}

}
