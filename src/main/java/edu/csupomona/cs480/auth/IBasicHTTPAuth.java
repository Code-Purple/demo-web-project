package edu.csupomona.cs480.auth;

import org.springframework.jdbc.core.JdbcTemplate;

public interface IBasicHTTPAuth {
	boolean basicHTTPCheckHeader(JdbcTemplate jdbc, String headerValue);
	
	/**
	 * 
	 * @return String[] results. 
	 * results[0] = username;
	 * results[1] = password
	 * 
	 * Returns null if it's a bad string.
	 */
	String[] basicHTTPDecode(String headerValue);
}
