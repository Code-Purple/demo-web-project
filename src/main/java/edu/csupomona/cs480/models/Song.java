package edu.csupomona.cs480.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class Song extends DataModel{
	public static final String INSERT_SQL = "Insert Into Song (name, artist, bpm, gap) values (?, ?, ?, ?)";
	
	/** Data Attributes **/
	public String name;
	public String artist;
	public String mp3Url;
	public String videoUrl;
	public int BPM;	
	public int GAP;
	public String lyrics;
	
	/** Custom Attributes (Ease of Use) **/
	public List<SongNote> notes;
	
	public List<String> lyricLines;
	
	public Song(){
		lyricLines = new ArrayList<String>();
	}
	
	public Song(ResultSet rs) throws SQLException{
		this();
		this.populateFromResultSet(rs);
	}
	
	public List<Song> search(String query, JdbcTemplate jdbc){
		String paddedQuery = "%" + query + "%";
		List<Song> result = jdbc.query("Select * from Song where Name like ? or Artist like ?;",
				new Object[] {paddedQuery, paddedQuery},
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
	
	public void deriveLyricLines(){
		if(this.notes != null){
			String line ="";
			
			for(SongNote note : notes){
				if(note.noteTypeID != SongNoteType.LineBreak.ordinal()){
					line += note.text.replace("~", "");
				}else{
					this.lyricLines.add(line);
					System.out.println(line);
					line = "";
				}
			}
			this.lyricLines.add(line); //may add empty last line when the last is a line break; shouldn't matter
		}else{
			System.out.println("Failed to derive Lyric lines. Note list is null.");
		}
	}

	@Override
	public void populateFromResultSet(ResultSet res) throws SQLException{
		
		this.id = res.getLong("SongID");
		this.name = res.getString("Name");
		this.artist = res.getString("Artist");
		this.mp3Url = res.getString("MP3Url");
		this.videoUrl = res.getString("VideoUrl");
		this.BPM = res.getInt("BPM");
		this.GAP = res.getInt("GAP");
		
		this.lyrics = res.getString("LyricText");
		
	}

	@Override
	public Song selectSingle(long id, JdbcTemplate jdbc){
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
	public List<? extends DataModel> selectAll(JdbcTemplate jdbc) {
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
	
	public void printLyrics(){
		if(this.lyricLines.size() == 0 && this.notes != null)
			this.deriveLyricLines();
		
		System.out.println("Printing Lyrics: " + name);
		for(String line : lyricLines)
			System.out.println(line);
	}

	@Override
	public PreparedStatement getInsertStatement(JdbcTemplate jdbc)
			throws SQLException {
		
		PreparedStatement pState = jdbc.getDataSource().getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
		
		pState.setString(1, name);
		pState.setString(2, artist);
		pState.setInt(3, BPM);
		pState.setInt(4, GAP);
		return pState;
	}
}
