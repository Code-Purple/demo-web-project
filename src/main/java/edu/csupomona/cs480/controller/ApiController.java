package edu.csupomona.cs480.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.csupomona.cs480.auth.AuthenticationManager;
import edu.csupomona.cs480.data.provider.UserManager;
import edu.csupomona.cs480.models.Song;
import edu.csupomona.cs480.models.User;

@RestController
public class ApiController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private UserManager userManager;
	
	@Autowired
	private AuthenticationManager authManager;

	/*
	 * Karaok.in Client API Section
	 * All methods which require Authorization will accept a standard Basic HTTP Authorization Header
	 * Or username=...&password=... in the appropriate form (GET vs. POST)
	 */
	
	//Search Songs
	@RequestMapping(value = "api/songs/search/{query}", method = RequestMethod.GET)
	public ResponseEntity<List<Song>> searchSongs(@PathVariable("query") String query, 
			@RequestHeader(value = "Authorization", required = false) String auth, @RequestParam(value="username", required = false) String username,  @RequestParam(value="password", required = false) String password) {
		
		if(auth == null){
			if(!authManager.checkAuth(jdbcTemplate, username, password))
				return new ResponseEntity<List<Song>>(HttpStatus.FORBIDDEN);
		}else{
			if(!authManager.basicHTTPCheckHeader(jdbcTemplate, auth))
				return new ResponseEntity<List<Song>>(HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<List<Song>>(new Song().search(query, jdbcTemplate), HttpStatus.OK);
	}
	
	//Get Song Including Notes
	@RequestMapping(value = "api/songs/{id}", method = RequestMethod.GET)
	public ResponseEntity<Song> getSong(@PathVariable("id") int id,
			@RequestHeader(value = "Authorization", required = false) String auth, @RequestParam(value="username", required = false) String username,  @RequestParam(value="password", required = false) String password) {
		
		if(auth == null){
			if(!authManager.checkAuth(jdbcTemplate, username, password))
				return new ResponseEntity<Song>(HttpStatus.FORBIDDEN);
		}else{
			if(!authManager.basicHTTPCheckHeader(jdbcTemplate, auth))
				return new ResponseEntity<Song>(HttpStatus.FORBIDDEN);
		}
		
		
		Song s = new Song().selectSingle(id, jdbcTemplate);
		if(s != null){
			s.loadNotes(jdbcTemplate);
			return new ResponseEntity<Song>(s, HttpStatus.OK);
		}else{
			return new ResponseEntity<Song>(HttpStatus.NOT_FOUND);
		}
	}
	
	//Get PlainText Song Lyrics
	@RequestMapping(value = "api/songs/{id}/lyrics", method = RequestMethod.GET)
	public ResponseEntity<String> getSongLyrics(@PathVariable("id") int id){
		Song s = new Song().selectSingle(id, jdbcTemplate);
		
		if(s != null){
			s.deriveLyricLines();
			StringBuilder b = new StringBuilder();
			for(String l : s.lyricLines){
				b.append(l);
			}
			
			String lyrics = b.toString();
			return new ResponseEntity<String>(lyrics, HttpStatus.OK);
		}else{
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	//Create User
	@RequestMapping(value = "api/users", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<User> createUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestHeader(value = "Authorization", required = false) String auth) {
		User u = new User();
		
		if(auth == null){
			u.username = username;
			u.password = password;
		}else{
			String[] parts = authManager.basicHTTPDecode(auth);
			if(parts == null){
				//Auth is improperly formatted
				
				return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
			}
			
			u.username = parts[0];
			u.password = parts[1];
		}
		
		int retries = 3;
		
		while(retries -- > 0){
			try{
				Long id = u.insert(jdbcTemplate);
				return new ResponseEntity<User>(u, HttpStatus.OK);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
	}
	
	//Check User login
	@RequestMapping(value = "api/login", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Boolean> loginUser(@RequestParam(value="username", required=false) String username, @RequestParam(value="password", required=false) String password, @RequestHeader(value="Authorization", required=false) String auth) {
		int retries = 3;
		
		while(retries -- > 0){
			try{
				if(auth == null)
					return new ResponseEntity<Boolean>(authManager.checkAuth(jdbcTemplate, username, password), HttpStatus.OK);
				else
					return new ResponseEntity<Boolean>(authManager.basicHTTPCheckHeader(jdbcTemplate, auth), HttpStatus.OK);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "api/login", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Boolean> loginUserGet(@RequestParam(value="username", required=false) String username, @RequestParam(value="password", required=false) String password, @RequestHeader(value="Authorization", required=false) String auth) {
		int retries = 3;
		
		while(retries -- > 0){
			try{
				if(auth == null)
					return new ResponseEntity<Boolean>(authManager.checkAuth(jdbcTemplate, username, password), HttpStatus.OK);
				else
					return new ResponseEntity<Boolean>(authManager.basicHTTPCheckHeader(jdbcTemplate, auth), HttpStatus.OK);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
	}
	
	//Submit User Score
	
	
	
}
