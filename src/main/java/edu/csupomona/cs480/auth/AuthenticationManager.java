package edu.csupomona.cs480.auth;

import org.postgresql.util.Base64;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.csupomona.cs480.models.User;

/**
 * This class manages Authentication using different methods
 * @author Bryan Thornbury
 *
 */
public class AuthenticationManager implements IAuthManager, IBasicHTTPAuth{

	@Override
	public boolean basicHTTPCheckHeader(JdbcTemplate jdbc, String headerValue) {
		if(headerValue == null) return false;
		
		//vals[0] = Basic
		//vals[1] = Auth String
		String[] vals = headerValue.split(" ");
		
		if(vals.length != 2){
			System.out.println("Login Failed (Bad before split): " + headerValue);
			return false;
		}
		
		
		String authString = new String(Base64.decode(vals[1]));
		String[] authParts = authString.split(":");
		
		if(authParts.length != 2) {
			System.out.println("Login Failed (Bad After Decode): " + authString);
			return false;
		}
		
		
		String username = authParts[0];
		String password = authParts[1];
		
		return checkAuth(jdbc, username, password);
	}

	@Override
	public String[] basicHTTPDecode(String headerValue) {
		//vals[0] = Basic
				//vals[1] = Auth String
				String[] vals = headerValue.split(" ");
				
				if(vals.length != 2){
					System.out.println("Login Failed (Bad before split): " + headerValue);
					return null;
				}
				
				
				String authString = new String(Base64.decode(vals[1]));
				String[] authParts = authString.split(":");
				
				if(authParts.length != 2) {
					System.out.println("Login Failed (Bad After Decode): " + authString);
					return null;
				}
				
				return authParts;
	}

	@Override
	public Boolean checkAuth(JdbcTemplate jdbc, String username, String password) {
		if(username == null || password == null) return false;
		
		try{
			
			User u = new User().selectByLogin(jdbc, username, password);
			return u == null;
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	

}
