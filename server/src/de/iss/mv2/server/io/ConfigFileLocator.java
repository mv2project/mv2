package de.iss.mv2.server.io;

import java.io.File;

/**
 * A helper to find the location of the runtime configuration files.
 * 
 * @author Marcel Singer
 *
 */
public final class ConfigFileLocator {

	/**
	 * Prevents this class from being instantiated.
	 */
	private ConfigFileLocator(){
		
	}
	
	/**
	 * Returns the directory that should contain the configuration files.
	 * 
	 * @return The file object to the directory that should contain the
	 *         configuration files.
	 */
	public static File getConfigFileLocation() {
		return new File(ClassLoader.getSystemClassLoader().getResource(".")
				.getPath());
	}

}
