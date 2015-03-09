package edu.csupomona.cs480.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.csupomona.cs480.db.IDao;

public interface IDataModel {
//	public boolean update(IDao dataAccess);
//	public boolean insert(IDao dataAccess);
//	public boolean delete(IDao dataAccess);
	public void populateFromResultSet(ResultSet res) throws SQLException;
	
}
