package edu.csupomona.cs480.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.csupomona.cs480.auth.AuthenticationManager;
import edu.csupomona.cs480.models.Song;

@Controller
public class AController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private AuthenticationManager authManager;

	@RequestMapping(value="/sing/{id}", method=RequestMethod.GET)
	public String sing(ModelMap map, @PathVariable("id") Integer id){
		System.out.println("Calling mv controller.");
		
		Song s = new Song().selectSingle(id, jdbcTemplate);
		System.out.println(s.toString());
		s.loadNotes(jdbcTemplate);
		
		if(s != null){
			map.addAttribute("id", s.id);
			map.addAttribute("name", s.name);
			map.addAttribute("mp3url", s.mp3Url);
			
			return "sing";
		}else{
			//deal with this later, song not found
			System.out.println("s is null");
			return null;
		}
	}

}
