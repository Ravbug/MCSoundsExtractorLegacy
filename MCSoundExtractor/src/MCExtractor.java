import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * MCExtractor.java
 * @author Ravbug (https://twitter.com/ravbug)
 * @version 1.0
 * 
 * This file contains the code for the backend. It handles all the copying
 * and reading the indexes information. This class uses Google Gson to convert
 * a JSON string to a Java Map Object
 */

/**
 * Main class, whose sole purpose is to start the UI
 * @author Ravbug (https://twitter.com/ravbug)
 * @version 1.0
 */
public class MCExtractor {
	public static void main(String[] args) {
		//new main().run();
		//Engine e = new Engine("/Users/main/Library/Application Support/minecraft","/Users/main/Desktop","1.13.1");
		//e.run();
		new UI();
	}
}

/**
 * An interface which is used for callbacks from the background thread
 * @author Ravbug (https://twitter.com/ravbug)
 */
interface EngineCallback{
	void successEvent(String message);
	void progressEvent(int progress);
}

/**
 * Engine class
 * 
 * @author Ravbug (https://twitter.com/ravbug)
 * 
 * Does the work of copying the files to the new folder. 
 * Runs as a separate thread to avoid tying up the UI, and allows for progress updates
 */
class Engine extends Thread{
	
	private String mcDir;
	private String outDir;
	private String mcVer;
	private EngineCallback ecbStatus;
	
	/**
	 * Main constructor for the Engine
	 * 
	 * @param dir: String representing the fully-qualified path to the Minecraft installation
	 * @param out: String representing the fully-qualified path to the output directory
	 * @param ver: String representing the Minecraft version to use. Ex: "1.13.1"
	 */
	Engine(String dir, String out, String ver){
		mcDir = dir;
		outDir = out;
		mcVer = ver;
	}
	
	/**
	 * Registers the callback method for future use 
	 * @param ecbs: the EngineCallback class which has the methods to call
	 */
	public void registerCallbacks(EngineCallback ecbs) {
		ecbStatus = ecbs;
	}
	
	/**
	 * Does the work of copying the files from the minecraft folder to the destination
	 */
	public void run() {
		//open the file at assets/indexes/mcver.json
		Scanner assetScanner = OpenFile.openToRead(mcDir + "/assets/indexes/"+mcVer+".json").useDelimiter("\\A");
		String assetsString = assetScanner.next();
	
		//convert it to a map
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Object>>(){}.getType();
		Map<String, Object> indexesMap = gson.fromJson(assetsString, type);
		
		
		//build map out of directory
		File f = new File(mcDir+"/assets/objects/");
		Map<String,String> assetsMap = createDirectoryMap(f);
		
		//Make a "minecraft" folder in the output directory
		File dir = new File(outDir + "/");
		if (dir.exists()) {
			dir.mkdir();
		}
		
		
		indexesMap = ((Map<String, Object>) indexesMap.get("objects"));
		//count the number of sound files to do (is there a way to get the count without doing this?)
		int count = 0;
		for (String path :indexesMap.keySet()) {
			if (path.contains("minecraft/sounds/")){
				count++;
			}
		}
		//copy the files if they are sounds
		int current = 0;
		for (String path :indexesMap.keySet()) {
			if (path.contains("minecraft/sounds/")){
				//String sourcePath = assetsMap.get(indexesMap.get(path));
				Map<String,String> pathMap = (Map<String, String>)  indexesMap.get(path);
				
				//get the path using the hash
				String source = assetsMap.get(pathMap.get("hash"));
				String destination = dir.getAbsolutePath();
				destination += "/" + path;
					
				//set up File objects
				File sourceFile = new File(source);
				File destFile = new File(destination);
				//make necessary folders for copy
				destFile.mkdirs();
				
				//Attempt to copy
				try {			
					Files.copy( sourceFile.toPath(), destFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
					
					//notify caller of progress update
					ecbStatus.progressEvent((int)((float)current/count*100));
					
					current++;
				} catch (Exception e) {
					//turn exception data into a string
					
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					
					//notify caller
					ecbStatus.successEvent(sw.toString());
					
					//terminate
					return;
				}
			}
		}	
		//notify caller of success
		ecbStatus.progressEvent(100);
		ecbStatus.successEvent(null);
	}
	
	/**
	 * Recursively creates a map of all the items in a folder, in the structure filename:absolute_path
	 * @param base: the Directory or file to serve as the root directory
	 * @return: A map of all the files in the directory, including subdirectories
	 */
	private Map<String,String> createDirectoryMap(File base){
		//create the main map
		Map<String,String> dirMap = new HashMap<String,String>();
		
		//get all the filenames in the folder
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(base.listFiles()));
		for (File f:files) {
			if (f.isDirectory()) {
				//if is a directory, recurse into the directory
				dirMap.putAll(createDirectoryMap(f));
			}
			else {
				//add the file to the map
				dirMap.put(f.getName(), f.getAbsolutePath());
			}
		}
		
		return dirMap;	
	}
	
}


