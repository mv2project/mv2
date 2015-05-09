package de.iss.mv2.logging;

import java.util.Arrays;

/**
 * A helper class to retrieve the current stack trace. 
 * @author Marcel Singer
 *
 */
public final class StackTracer {

	/**
	 * Prevents this class from being instantiated.
	 */
	private StackTracer() {
	}
	
	
	/**
	 * Retrieves the current stack trace.
	 * @return The retrieved stack trace. 
	 */
	public static StackTraceElement[] getStackTrace(){
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length == 0) return elements;
		return Arrays.copyOfRange(elements, 2, elements.length);
	}

}
