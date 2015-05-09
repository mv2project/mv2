package de.iss.mv2.logging;

/**
 * Manages the current logger.
 * @author Marcel Singer
 *
 */
public final class LoggerManager {

	/**
	 * Prevents this class from being instantiated.
	 */
	private LoggerManager() {
		
	}
	
	/**
	 * Holds the current logger.
	 */
	public static volatile Logger currentInstance = new ConsoleLogger();
	
	/**
	 * Returns the current logger to use.
	 * @return The current logger to use.
	 */
	public static synchronized Logger getCurrentLogger(){
		return currentInstance;
	}
	
	

}
