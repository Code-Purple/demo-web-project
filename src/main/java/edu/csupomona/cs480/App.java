package edu.csupomona.cs480;

import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.csupomona.cs480.data.provider.FSUserManager;
import edu.csupomona.cs480.data.provider.UserManager;
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
    	//Create DB and Tables
    	System.out.println("Initializing DB!");
    	File creationScript = ResourceResolver.getFileFromRelativePath("sql/DB_CREATE.sql");
    	String sql = new String(Files.readAllBytes(creationScript.toPath()));
    	
    	jdbcTemplate.execute(sql);
    	
    	System.out.println("DB Initialized.");
    	
    	//
    	
    }
    
    
}
