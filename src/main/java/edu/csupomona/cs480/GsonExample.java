package edu.csupomona.cs480;
import java.util.ArrayList;
import com.google.gson.Gson;

public class GsonExample{
	
	public String getJSON(){
		Data names = new Data();
		Gson gson = new Gson();
		return gson.toJson(names);
			
	}	

public class Data {
	private String category = "Animals in Pixar Movies";
	ArrayList<String> names = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("Dug");
			add("Slinky");
			add("Dory");
			add("Rex");
			add("Crush");
			add("Heimlich");
			add("Bullseye");
			add("Kevin");
		}
	};

	public String toString(){
		return category + ": " + names;
	}
}
}