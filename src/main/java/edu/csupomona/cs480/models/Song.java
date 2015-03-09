package edu.csupomona.cs480.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
}
