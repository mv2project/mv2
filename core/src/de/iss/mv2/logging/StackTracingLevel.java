package de.iss.mv2.logging;

/**
 * A level of stack tracing to perform by a {@link Logger}. 
 * @author Marcel Singer
 *
 */
public enum StackTracingLevel {

	/**
	 * Non stack tracing is performed.
	 */
	NONE,
	/**
	 * Only basic stack tracing informations are stored in a string.
	 */
	REDUCED,
	/**
	 * The full stack trace is stored.
	 */
	DETAILED;
	
}
