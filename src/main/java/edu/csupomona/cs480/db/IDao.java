package edu.csupomona.cs480.db;

import java.util.List;

import javax.sql.DataSource;

import edu.csupomona.cs480.data.User;
import edu.csupomona.cs480.models.Song;
import edu.csupomona.cs480.models.SongNote;

public interface IDao {

  void setDataSource(DataSource ds);

  void createUser(String username, String password);

  List<Song> selectAllSongs();

  User selectUser(String username);
  
  List<SongNote> selectSongNotes(Integer SongID);

  void deleteAll();

  void delete(String firstName, String lastName);

} 
