package edu.csupomona.cs480.auth;

import org.springframework.jdbc.core.JdbcTemplate;

public interface IAuthManager {
	Boolean checkAuth(JdbcTemplate jdbc, String username, String password);
}
