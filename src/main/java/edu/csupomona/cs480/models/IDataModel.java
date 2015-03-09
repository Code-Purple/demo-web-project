package edu.csupomona.cs480.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public interface IDataModel {
//	public boolean update(JdbcTemplate jdbc);
//	public boolean delete(JdbcTemplate jdbc);
	public boolean insert(JdbcTemplate jdbc);
	public void populateFromResultSet(ResultSet res) throws SQLException;
	public IDataModel selectSingle(int id, JdbcTemplate jdbc);
	public List<? extends IDataModel> selectAll(JdbcTemplate jdbc);
}
