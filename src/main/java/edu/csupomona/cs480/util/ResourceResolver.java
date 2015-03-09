package edu.csupomona.cs480.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This is an utility class to help file locating.
 */
public class ResourceResolver {

	/** The base folder to store all the data used by this project. */
    private static final String BASE_DIR = System.getProperty("user.home") + "/cs480";

    /**
     * Get the file used to store the user object JSON
     *
     * @param userId
     * @return
     */
    public static File getUserFile() {
        File file = new File(BASE_DIR + "/" + "user-map.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
    
    public static File getFileFromRelativePath(String filePath) {
    	//if(filePath.charAt(0) != '/')
    		//return null;
    	
        URL url = ResourceResolver.class.getClassLoader().getResource(filePath);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (Exception e) {
        	System.out.println("Resource Resolver: URI Syntax Issue");
        	e.printStackTrace();
            file = new File(url.getPath());
        }finally{
        	return file;
        }
    }
    
    public static File[] getAllFilesInFolder(String path){
    	
    	URL url = ResourceResolver.class.getClassLoader().getResource(path);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        
        if(file != null){
        	if(file.isDirectory()){
        		return file.listFiles();
        	}else{
        		return null;
        	}
        }else{
        	return null;
        }
    }
}
