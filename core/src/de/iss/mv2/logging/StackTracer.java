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
	 * @param ignore the amount of stack levels to ignore.
	 * @return The retrieved stack trace. 
	 * @throws IllegalArgumentException Is thrown if the given amount of stack levels to ignore is negative.
	 */
	public static StackTraceElement[] getStackTrace(int ignore) throws IllegalArgumentException{
		if(ignore < 0) throw new IllegalArgumentException("The amount of levels to must not be negative.");
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length == 0) return elements;
		return Arrays.copyOfRange(elements, 2 + ignore, elements.length);
	}
	
	/**
	 * Retrieves the current stack trace.
	 * @return The retrieved stack trace.
	 */
	public static StackTraceElement[] getStackTrace() {
		return getStackTrace(1);
	}
	
	

}
