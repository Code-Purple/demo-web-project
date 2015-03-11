package edu.csupomona.cs480.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class User extends DataModel{
	public static final String INSERT_SQL = "INSERT INTO UserTable (username, password) values (?, ?)";
	public String username;
	public String password;
	
	public User(ResultSet rs) throws SQLException{
		super(rs);
	}
	
	
	public User() {
		super();
	}


	@Override
	public PreparedStatement getInsertStatement(JdbcTemplate jdbc)
			throws SQLException {
		
		PreparedStatement pState = this.getBlankInsertStatement(jdbc);
		this.setInsertStatementParams(jdbc, pState);
		return pState;
	}
	@Override
	public void populateFromResultSet(ResultSet res) throws SQLException {
		this.id = res.getLong("UserID");
		this.username = res.getString("Username");
		this.password = res.getString("Password");
	}
	
	@Override
	public DataModel selectSingle(long id, JdbcTemplate jdbc) {
		String sql = "Select * from UserTable where UserID = ?;";
		
		List<User> result = jdbc.query(sql,
				new Object[] {id},
				new RowMapper<User>() {
					@Override
		            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
						User u = new User(rs);
						return u;
		            }
				});
		
		if(result.size() == 1){
			return result.get(0);
		}else if(result.size() < 1){
			System.out.println("Select Single User: None returned.");
			return null;
		}else{
			System.out.println("Select Single User: More than one returned.");
			return result.get(0);
		}
	}
	@Override
	public List<? extends DataModel> selectAll(JdbcTemplate jdbc) {
		String sql = "Select * from UserTable;";
		
		List<User> result = jdbc.query(sql,
				new RowMapper<User>() {
					@Override
		            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
						User u = new User(rs);
						return u;
		            }
				});
		
		return result;
	}
	
	public User selectByLogin(JdbcTemplate jdbc, String username,
			String password) {
		String sql = "Select * from UserTable where Username = ? and Password = ?;";
		
		List<User> result = jdbc.query(sql,
				new Object[] {username, password},
				new RowMapper<User>() {
					@Override
		            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
						User u = new User(rs);
						return u;
		            }
				});
		
		if(result.size() == 1){
			return result.get(0);
		}else if(result.size() < 1){
			System.out.println("Select Login User: None returned.");
			return null;
		}else{
			System.out.println("Select Login User: More than one returned.");
			return result.get(0);
		}
	}


	@Override
	public PreparedStatement getBlankInsertStatement(JdbcTemplate jdbc)
			throws SQLException {
		return jdbc.getDataSource().getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
		
	}


	@Override
	protected void setInsertStatementParams(JdbcTemplate jdbc,
			PreparedStatement pState) throws SQLException {
		// TODO Auto-generated method stub
		pState.setString(1, username);
		pState.setString(2, password);
		
	}
}