package edu.csupomona.cs480.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class DataModel {
	public Long id;
	
	protected PreparedStatement batch;
	
	public DataModel() {
	};
	
	public DataModel(ResultSet rs) throws SQLException {
		this();
		this.populateFromResultSet(rs);
	}
	
	protected void flushBatch() throws SQLException{
		if(batch != null){
			batch.executeBatch();
		}else{
			throw new IllegalStateException("DataModel: Batch is null");
		}
	}
	
	
//	public boolean update(JdbcTemplate jdbc);
//	public boolean delete(JdbcTemplate jdbc);
	public abstract PreparedStatement getInsertStatement(JdbcTemplate jdbc) throws SQLException; //returns new id
	public abstract PreparedStatement getBlankInsertStatement(JdbcTemplate jdbc) throws SQLException;
	protected abstract void setInsertStatementParams(JdbcTemplate jdbc, PreparedStatement p) throws SQLException; //returns new id
	public abstract void populateFromResultSet(ResultSet res) throws SQLException;
	public abstract DataModel selectSingle(long id, JdbcTemplate jdbc);
	public abstract List<? extends DataModel> selectAll(JdbcTemplate jdbc);
	
	public void insertBatch(JdbcTemplate jdbc, PreparedStatement batch) throws SQLException {
		//jdbc.getDataSource().getConnection().setAutoCommit(false);
		
		setInsertStatementParams(jdbc,batch);
		batch.addBatch();
	}
	
	public long insert(JdbcTemplate jdbc) throws SQLException{
		jdbc.getDataSource().getConnection().setAutoCommit(true);
		
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
