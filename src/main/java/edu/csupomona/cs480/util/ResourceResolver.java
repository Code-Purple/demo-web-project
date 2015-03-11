package edu.csupomona.cs480.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This is an utility class to help file locating.
 */
public class ResourceResolver {
	
	public static final Boolean IsJar(){
//    	final File jarFile = new File(ResourceResolver.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//		return jarFile.isFile();
		 String className = ResourceResolver.class.getName().replace('.', '/');
		 String classJar =
				 ResourceResolver.class.getResource("/" + className + ".class").toString();
		 return classJar.startsWith("jar:");
	}

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
    
    public static InputStream getStreamFromRelativePath(String filePath) {
    	//if(filePath.charAt(0) != '/')
    		//return null;
    	InputStream stream = ClassLoader.getSystemResourceAsStream(filePath);
        return stream;
    }
    
    public static File getFileFromRelativePath(String filePath) {
    	//if(filePath.charAt(0) != '/')
    		//return null;
    	
        URL url = ClassLoader.getSystemResource(filePath);
        File file = null;
        try {
            file = new File(url.getFile());
        } catch (Exception e) {
        	System.out.println("Resource Resolver: URI Syntax Issue");
        	e.printStackTrace();
            file = new File(url.getPath());
        }finally{
        	return file;
        }
    }
    
    public static List<InputStream> getAllStreamsInFolder(String path) throws IOException{
    	List<String> names = getAllFileNamesInFolder(path);
    	List<InputStream> streams = new ArrayList<InputStream>();
    	for(String name: names){
    		InputStream s = ResourceResolver.getStreamFromRelativePath(name);
    		if(s != null){
    			streams.add(s);
    		}else{
    			System.out.println("Failed to get Stream at: " + name);
    		}
    	}
    	return streams;
    	
    	
    }
    
    public static List<String> getAllFileNamesInFolder(String path) throws IOException{
    	if(IsJar()) {  // Run with JAR file
    		CodeSource src = ResourceResolver.class.getProtectionDomain().getCodeSource();
    		List<String> list = new ArrayList<String>();

    		if( src != null ) {
    		    URL jar = src.getLocation();
    		    ZipInputStream zip = new ZipInputStream( jar.openStream());
    		    ZipEntry ze = null;

    		    while( ( ze = zip.getNextEntry() ) != null ) {
    		        String entryName = ze.getName();
    		        if( entryName.startsWith(path + '/')) {
    		            list.add( entryName  );
    		        }
    		    }

    		 }else{
    			 System.out.format("FileNamesInFolder (JAR): src is null\nPath: %s \n", path);
    		 }
    		 return list;
    	}else{
    	
			URL url = ClassLoader.getSystemResource(path);
	        File file = null;
	//        try {
	            file = new File(url.getFile());
	//        } 
	//        catch (URISyntaxException e) {
	//            file = new File(url.getPath());
	//        }
	        
	        if(file != null){
	        	if(file.isDirectory()){
	        		List<String> list = Arrays.asList(file.list());
	        		for(int i = 0; i<list.size(); ++i){
	        			list.set(i, path + "/" + list.get(i));
	        		}
	        		return list;
	        	}else{
	        		System.out.format("FileNamesInFolder: Path File Not identified as a directory: %s\nResource URL: %s\n", path, url.toString());;
	        		return null;
	        	}
	        }else{
	        	System.out.format("FileNamesInFolder: Path File is null: %s\n Resource URL: %s\n", path,url.toString());
	        	return null;
	        }
    	}
    }
    
    public static List<File> getAllFilesInFolder(String path) throws IOException{
    	List<String> names = ResourceResolver.getAllFileNamesInFolder(path);
    	List<File> files = new ArrayList<File>();
    	for(String n : names){
    		files.add(ResourceResolver.getFileFromRelativePath(n));
    	}
    	
    	return files;
    	
    }

	
}
