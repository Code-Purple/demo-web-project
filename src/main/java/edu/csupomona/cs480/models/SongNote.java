package edu.csupomona.cs480.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class SongNote extends DataModel{
	public static final String INSERT_SQL = "Insert Into SongNote (SequenceNum, StartingBeat, Duration, Pitch, SongID, NoteText, TypeID) values (?, ?, ?, ?, ?, ?, ?)";
	
	/* ATTRIBUTES */
	public int sequenceNum;
	public int startBeat;
	public int duration;
	public int pitch;
	public long songId;
	public String text;
	public long noteTypeID;
	
	public SongNote(){}
	public SongNote(ResultSet rs) throws SQLException{
		this.populateFromResultSet(rs);
	}
	
	@Override
	public void populateFromResultSet(ResultSet res) throws SQLException{
		this.id = res.getLong("SongNoteID");
		this.sequenceNum = res.getInt("SequenceNum");
		this.startBeat = res.getInt("StartingBeat");
		this.duration = res.getInt("Duration");
		this.pitch = res.getInt("Pitch");
		this.songId = res.getLong("SongID");
		this.text = res.getString("NoteText");
		this.noteTypeID = res.getLong("TypeID");
		
	}


	@Override
	public SongNote selectSingle(long id, JdbcTemplate jdbc){
		List<SongNote> result = jdbc.query("Select * from SongNote Where SongNoteID = ?;", 
				new Object[] {id}, 
				new RowMapper<SongNote>() {
					@Override
		            public SongNote mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new SongNote(rs);
		            }
				});
		
		//check for errors
		if(result.size() > 1){
			System.out.println("Select Single (SongNote): More than one selected.");
			return result.get(0);
		}else if(result.size() < 1){
			System.out.println("Select Single (SongNote): None selected.");
			return null;
		}else{
			//exactly 1
			return result.get(0);
		}
	}
	
	public List<SongNote> selectBySongOrdered(long songid, JdbcTemplate jdbc){
		List<SongNote> result = jdbc.query("Select * from SongNote Where SongID = ? Order By SequenceNum;", 
				new Object[] {songid}, 
				new RowMapper<SongNote>() {
					@Override
		            public SongNote mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new SongNote(rs);
		            }
				});
		
		//check for errors
		if(result.size() < 1){
			System.out.println("Select Single (SongNote): None selected.");
			return null;
		}else {
			return result;
		}
	}

	@Override
	public List<? extends DataModel> selectAll(JdbcTemplate jdbc) {
		List<SongNote> result = jdbc.query("Select * from SongNote;",
				new RowMapper<SongNote>() {
					@Override
		            public SongNote mapRow(ResultSet rs, int rowNum) throws SQLException {
						SongNote s = new SongNote(rs);
						return s;
		            }
				});
		
		return result;
	}
	
	@Override
	public String toString(){
		return String.format("---Song Note---\nSong ID: %d\nSequence:%d\nStart:%d\nDuration:%d\nPitch:%d\nText:%s", songId, sequenceNum, startBeat, duration, pitch, text);
	}
	
	@Override
	public PreparedStatement getInsertStatement(JdbcTemplate jdbc)
			throws SQLException {
		
		PreparedStatement pState = this.getBlankInsertStatement(jdbc);
		setInsertStatementParams(jdbc,pState);
		
		return pState;
	}
	@Override
	public PreparedStatement getBlankInsertStatement(JdbcTemplate jdbc)
			throws SQLException {
		return jdbc.getDataSource().getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
	}
	
	@Override
	protected void setInsertStatementParams(JdbcTemplate jdbc,
			PreparedStatement pState) throws SQLException {
		pState.setInt(1, sequenceNum);
		pState.setInt(2, startBeat);
		pState.setInt(3, duration);
		pState.setInt(4, pitch);
		pState.setLong(5, songId);
		pState.setString(6, text);
		pState.setLong(7, noteTypeID);
		
	}
}