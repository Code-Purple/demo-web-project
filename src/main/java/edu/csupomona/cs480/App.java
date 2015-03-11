package edu.csupomona.cs480;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import edu.csupomona.cs480.auth.AuthenticationManager;
import edu.csupomona.cs480.config.Config;
import edu.csupomona.cs480.data.provider.FSUserManager;
import edu.csupomona.cs480.data.provider.UserManager;
import edu.csupomona.cs480.models.Song;
import edu.csupomona.cs480.models.SongNote;
import edu.csupomona.cs480.songs.SongParser;
import edu.csupomona.cs480.util.ResourceResolver;

@SpringBootApplication
public class App implements CommandLineRunner{
	
	@Autowired
	JdbcTemplate jdbcTemplate;

    /**
     * This is a good example of how Spring instantiates
     * objects. The instances generated from this method
     * will be used in this project, where the Autowired
     * annotation is applied.
     */
    @Bean
    public UserManager userManager() {
        UserManager userManager = new FSUserManager();
        return userManager;
    }
    
    @Bean
    public AuthenticationManager authManager() {
    	AuthenticationManager auth = new AuthenticationManager();
        return auth;
    }
    
    @Bean
    public JdbcTemplate jdbdProvider() {
    	DataSource source = new DriverManagerDataSource(Config.DB_CONNECTION_STRING);
    	JdbcTemplate jdbc = new JdbcTemplate(source);
        return jdbc;
    }

    /**
     * This is the running main method for the web application.
     * Please note that Spring requires that there is one and
     * ONLY one main method in your whole program. You can create
     * other main methods for testing or debugging purposes, but
     * you cannot put extra main method when building your project.
     */
    public static void main(String[] args) throws Exception {
        // Run Spring Boot
        SpringApplication.run(App.class, args);
    }
    
    @Override
    public void run(String... strings) throws Exception {
    	
    	//Jar Status
    	System.out.println("Running In JAR: " + ResourceResolver.IsJar());
    	
    	//Create DB and Tables
    	System.out.println("Initializing DB!");
//    	File creationScript = ResourceResolver.getFileFromRelativePath("sql/DB_CREATE.sql");
//    	String sql = new String(Files.readAllBytes(creationScript.toPath()));
    	
    	boolean first = false;
    	try{
    		first = new Song().selectAll(jdbcTemplate).size() == 0;
    	}catch(Exception e){
    		e.printStackTrace();
    		first=true;
    	}
    	
    	if(first){
	    	InputStream creationScriptStream = ResourceResolver.getStreamFromRelativePath("sql/DB_CREATE.sql");
	    	String sql = new String(IOUtils.toByteArray(creationScriptStream));
	    	
	    	jdbcTemplate.execute(sql);
	    	
	    	System.out.println("DB Initialized.");
	    	
	    	//Populate DB with Parsed Values of each song...
	//    	File[] songFiles = ResourceResolver.getAllFilesInFolder("static/lyrics");
	//    	List<Song> songList = SongParser.parseFiles(songFiles);
	//    	
	    	List<InputStream> streams = ResourceResolver.getAllStreamsInFolder("static/lyrics");
	    	List<Song> songList = SongParser.parseAll(streams);
	    	
	    	PreparedStatement batch = new SongNote().getBlankInsertStatement(jdbcTemplate);
	    	batch.getConnection().setAutoCommit(false);
	    	//Insert Each Song
	    	for(Song s:songList){
	    		long sId = s.insert(jdbcTemplate);
	    		
	    		//Update Reference in SongNote + Insert Notes
	    		for(SongNote n : s.notes){
	    			n.songId = sId;
	    			n.insertBatch(jdbcTemplate, batch);
	    		}
	    		try{
	    			batch.executeBatch();
	    		}catch(Exception e){
	    			
	    		}
	    		batch.getConnection().commit();
	    		
	    		//s.printLyrics();
	    	}
    	}
    	
    	//Test!
    	System.out.println("Testing Song DB. Printing All Songs.");
    	List<Song> all = (List<Song>) new Song().selectAll(jdbcTemplate);
    	for(Song s: all){
    		System.out.println(s.toString());
//    		for(SongNote n: new SongNote().selectBySongOrdered(s.id, jdbcTemplate))
//    			System.out.println(n.toString());
    	}
    }
    
    
}
