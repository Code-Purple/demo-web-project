package edu.csupomona.cs480.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class DataModel {
	public Long id;
	
//	public boolean update(JdbcTemplate jdbc);
//	public boolean delete(JdbcTemplate jdbc);
	public abstract PreparedStatement getInsertStatement(JdbcTemplate jdbc) throws SQLException; //returns new id
	public abstract void populateFromResultSet(ResultSet res) throws SQLException;
	public abstract DataModel selectSingle(long id, JdbcTemplate jdbc);
	public abstract List<? extends DataModel> selectAll(JdbcTemplate jdbc);
	
	public long insert(JdbcTemplate jdbc) throws SQLException{
		
		PreparedStatement pState = getInsertStatement(jdbc);
		
		int numAffected = pState.executeUpdate();
		
		if(numAffected < 1){
			throw new SQLException("Insert: No rows affected on insert.");
		}else if(numAffected > 1){
			throw new SQLException("Insert: Insert resulted in more than one row update.");
		}
		else{
			ResultSet keys = pState.getGeneratedKeys();
			if(keys.next()){
				this.id = keys.getLong(1);
			}else{
				throw new SQLException("Insert: Inserting failed, no ID obtained");
			}
		}
		return this.id;
	}
}
