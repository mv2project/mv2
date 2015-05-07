package de.iss.mv2.io;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * A helper class to build file paths. 
 * @author Marcel Singer
 *
 */
public class PathBuilder {
	
	/**
	 * Holds the current location directory.
	 */
	private final File localDirectory;
	
	/**
	 * The systems separator.
	 */
	private final String separator = File.separator;
	
	/**
	 * Creates a new {@link PathBuilder} from the specified location.
	 * @param someFile A file providing the current location in the file system. The given instance can be either a directory or a file.
	 * @throws FileNotFoundException Is thrown if the given file does not exist.
	 * @throws IllegalArgumentException Is thrown if the given file is {@code null}.
	 */
	public PathBuilder(File someFile) throws FileNotFoundException, IllegalArgumentException{
		if(someFile == null) throw new IllegalArgumentException("The given file may not be null.");
		if(!someFile.exists()) throw new FileNotFoundException("The given file does not exist.");
		if(someFile.isFile()){
			localDirectory = new File(someFile.getParent());
		}else{
			localDirectory = new File(someFile.getAbsolutePath());
		}
	}
	
	/**
	 * Builds the path for a file located under the current directory.
	 * @param fileName The name of the file (including the extension). The name may or may not start with the file path separator.
	 * @return The absolute file path to the requested file.
	 * @throws IllegalArgumentException Is thrown if the given file name is {@code null} or empty.
	 */
	public String getChildFilePath(String fileName) throws IllegalArgumentException{
		if(fileName == null || fileName.isEmpty()) throw new IllegalArgumentException("The fileName may not be null or empty.");
		String file = localDirectory.getAbsolutePath();
		String sep = "";
		if(!file.endsWith(separator) && !fileName.startsWith(separator)) sep = separator;
		file = file + sep + fileName;
		return file;
	}
	
	/**
	 * Builds the path for a file located under the current directory.
	 * @param fileName The name of the file (including the extension). The name may or may not start with the file path separator.
	 * @return The file object created from the requested file path.
	 * @throws IllegalArgumentException Is thrown if the given file name is {@code null} or empty.
	 */
	public File getChildFile(String fileName) throws IllegalArgumentException{
		return new File(getChildFilePath(fileName));
	}
	
	
	

}
