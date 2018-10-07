import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

/** OpenFile.java
 *  Open Files for reading and writing
 * @author Ravbug (https://twitter.com/ravbug)
 * @version 8-25-2016
 */

public class OpenFile {

	/**
	 * Opens a file to read using the scanner class.
	 * @param fileName path to the file to open
	 * @return the scanner object to the file.
	 */
	 public static Scanner openToRead(String fileName){
		Scanner input = null;
		try{
			input = new Scanner(new File(fileName));
		}
		catch(FileNotFoundException e){
			System.err.println("ERROR: Cannot open " + fileName + " for reading: File does not exist");
			System.exit(-1);
		}

		return input;
	 }
}

