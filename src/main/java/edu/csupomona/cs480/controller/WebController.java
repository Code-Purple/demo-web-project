package edu.csupomona.cs480.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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

import singleton.SayHello;

import com.google.common.collect.Ordering;

import edu.csupomona.cs480.GsonExample;
import edu.csupomona.cs480.HTMLParser;
import edu.csupomona.cs480.auth.AuthenticationManager;
import edu.csupomona.cs480.data.provider.UserManager;
import edu.csupomona.cs480.models.Song;
import edu.csupomona.cs480.models.User;

/**
 * This is the controller used by Spring framework.
 * <p>
 * The basic function of this controller is to map
 * each HTTP API Path to the correspondent method.
 *
 */

@RestController
public class WebController {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private UserManager userManager;
	
	@Autowired
	private AuthenticationManager authManager;

	
	/*
	 * Assignments and other tests section.
	 */
	

	/**
	 * This is a simple example of how to use a data manager
	 * to retrieve the data and return it as an HTTP response.
	 * <p>
	 * Note, when it returns from the Spring, it will be
	 * automatically converted to JSON format.
	 * <p>
	 * Try it in your web browser:
	 * 	http://localhost:8080/cs480/user/user101
//	 */
//	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.GET)
//	User getUser(@PathVariable("userId") String userId) {
//		User user = userManager.getUser(userId);
//		return user;
//	}

	/**
	 * This is an example of sending an HTTP POST request to
	 * update a user's information (or create the user if not
	 * exists before).
	 *
	 * You can test this with a HTTP client by sending
	 *  http://localhost:8080/cs480/user/user101
	 *  	name=John major=CS
	 *
	 * Note, the URL will not work directly in browser, because
	 * it is not a GET request. You need to use a tool such as
	 * curl.
	 *
	 * @param id
	 * @param name
	 * @param major
	 * @return
	 */
//	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.POST)
//	User updateUser(
//			@PathVariable("userId") String id,
//			@RequestParam("name") String name,
//			@RequestParam(value = "major", required = false) String major) {
//		User user = new User();
//		user.setId(id);
//		user.setMajor(major);
//		user.setName(name);
//		userManager.updateUser(user);
//		return user;
//	}

	/**
	 * This API deletes the user. It uses HTTP DELETE method.
	 *
	 * @param userId
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.DELETE)
	void deleteUser(
			@PathVariable("userId") String userId) {
		userManager.deleteUser(userId);
	}

	/**
	 * This API lists all the users in the current database.
	 *
	 * @return
	 */
//	@RequestMapping(value = "/cs480/users/list", method = RequestMethod.GET)
//	List<User> listAllUsers() {
//		return userManager.listAllUsers();
//	}

	/*********** Web UI Test Utility **********/
	/**
	 * This method provide a simple web UI for you to test the different
	 * functionalities used in this web service.
	 */
//	@RequestMapping(value = "/cs480/home", method = RequestMethod.GET)
//	ModelAndView getUserHomepage() {
//		ModelAndView modelAndView = new ModelAndView("home");
//		modelAndView.addObject("users", listAllUsers());
//		return modelAndView;
//	}

	

	/********* Multiplication **********
	 * This method multiplies two numbers
	 * Author: Chantal
	 */
	@RequestMapping(value = "/cs480/mult/{num1}/{num2}", method = RequestMethod.GET)
	int multiplyNumbers(
			@PathVariable("num1") int num1,
			@PathVariable("num2") int num2) {    	
		return num1 * num2;
	}

	/********* Print hello **********
	 * This method simply prints "hello"
	 * Note: Modified to use a singleton class for assignment 9
	 * Author: Marion Levy
	 */
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	String sayHello() {
		SayHello hello = SayHello.getInstance();
		return hello.getHello();
	}
	
	/********* Sort 3 Numbers (Assignment 5) **********
	 * This method sorts and prints out 3 numbers using guava dependency
	 * Author: Marion Levy
	 */
	@RequestMapping(value = "/sortThree/{num1}/{num2}/{num3}", method = RequestMethod.GET)
	String sortThree(@PathVariable("num1") int num1, @PathVariable("num2") int num2, @PathVariable("num3") int num3) {
		List<Integer> toSort = Arrays.asList(num1, num2, num3);
		Collections.sort(toSort, Ordering.natural());
		return toSort.toString();
	}
	
	/******Parse webpage for links*****
	 * Assignment 5
	 * Scrapes google search results for top 10 links.
	 * http://localhost:8080/parse query="query"
	 * use postman to test
	 * Author: Chantal Fry
	 */
	@RequestMapping(value = "/parse", method = RequestMethod.POST)
	String parseForUrls(
			@RequestParam("query") String query
			) {
		HTMLParser p = new HTMLParser.HTMLParserBuilder()
											.setQuery(query)
											.createParser();
		p.ParseURL();
		return p.toString();
	}
	
	/*********Assignemnt 3: Addition **********
	 * This method adds two numbers
	 * Author: Ashley Barton
	 */
	@RequestMapping(value = "/cs480/{num1}/{num2}", method = RequestMethod.GET)
	int addNumbers(
			@PathVariable("num1") int num1,
			@PathVariable("num2") int num2) {    	
		return num1 + num2;
	}
	
	/****** Assignment 5: GSON Parsing *****
	 * Using GSON (Google Json Parser) to parse Java Objects
	 * http://localhost:8080/printingJson
	 * Author: Ashley Barton
	 * @return 
	 */
	@RequestMapping(value = "/printingJson", method = RequestMethod.GET)
	String printJson(){
		GsonExample ex = new GsonExample();
		return ex.getJSON();
	}
	
	/**
	 * Return the date
	 */
	@RequestMapping(value = "/cs480/date", method = RequestMethod.GET)
	String date()
	{
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy");
		return f.format(d);
	}
	
	/**
	 * Return the time is hh:mm:ss, 12 hour format
	 */
	@RequestMapping(value = "/cs480/time", method = RequestMethod.GET)
	String time()
	{
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat f = new SimpleDateFormat("hh:mm:ss a");
		return f.format(d);
	}
	
	/*@RequestMapping(value = "/index.html", method = RequestMethod.GET, params="search")
	public @ResponseBody void byParameter(@RequestParam("search") String search) 
	{
		System.out.println(search);
	}*/
}