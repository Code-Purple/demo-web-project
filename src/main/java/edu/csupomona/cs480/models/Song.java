package edu.csupomona.cs480.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class Song implements IDataModel{
	public long songId;
	public String name;
	public String artist;
	public String mp3Url;
	public String videoUrl;
	public int BPM;	
	public int GAP;
	public String lyrics;
	
	public List<SongNote> notes;
	
	public Song(){
		
	}
	
	public Song(ResultSet rs) throws SQLException{
		this.populateFromResultSet(rs);
	}

	@Override
	public void populateFromResultSet(ResultSet res) throws SQLException{
		
		this.songId = res.getInt("SongID");
		this.name = res.getString("Name");
		this.artist = res.getString("Artist");
		this.mp3Url = res.getString("MP3Url");
		this.videoUrl = res.getString("VideoUrl");
		this.BPM = res.getInt("BPM");
		this.GAP = res.getInt("GAP");
		
		this.lyrics = res.getString("LyricText");
		
	}

	@Override
	public boolean insert(JdbcTemplate jdbc) {
		Integer status = jdbc.update("Insert Into Song (name, artist, bpm, gap) values (?, ?, ?, ?)", name, artist, BPM, GAP);
		System.out.println("Inserting: " + status);
		
		return status == 1;
	}

	@Override
	public Song selectSingle(int id, JdbcTemplate jdbc){
		List<Song> result = jdbc.query("Select * from Song Where SongID = ?;", 
				new Object[] {id}, 
				new RowMapper<Song>() {
					@Override
		            public Song mapRow(ResultSet rs, int rowNum) throws SQLException {
						Song s = new Song();
						
						s.populateFromResultSet(rs);
						
						return s;
		            }
				});
		
		//check for errors
		if(result.size() > 1){
			System.out.println("Select Single (Song): More than one selected.");
			return result.get(0);
		}else if(result.size() < 1){
			System.out.println("Select Single (Song): None selected.");
			return null;
		}else{
			//exactly 1
			return result.get(0);
		}
	}

	@Override
	public List<? extends IDataModel> selectAll(JdbcTemplate jdbc) {
		List<Song> result = jdbc.query("Select * from Song;",
				new RowMapper<Song>() {
					@Override
		            public Song mapRow(ResultSet rs, int rowNum) throws SQLException {
						Song s = new Song();
						
						s.populateFromResultSet(rs);
						
						return s;
		            }
				});
		
		return result;
	}
	
	@Override
	public String toString(){
		return String.format("Karaok.in Song\nName: %s\nArtist: %s\nBPM: %d\nGAP: %d", name, artist, BPM, GAP);
	}
}
