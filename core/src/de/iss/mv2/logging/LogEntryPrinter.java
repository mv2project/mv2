package de.iss.mv2.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Creates a string containing the available informations about a log entry. 
 * @author Marcel Singer
 *
 */
public class LogEntryPrinter {

	/**
	 * Holds the format for dates.
	 */
	private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	/**
	 * Specifies if the stack trace should be included.
	 */
	private boolean printStackTrace = true;
	
	/**
	 * Creates a new instance of {@link LogEntryPrinter}.
	 */
	public LogEntryPrinter() {
		
	}
	
	/**
	 * Returns the string representation of the given log entry.
	 * @param entry The log entry thats string representation should be generated.
	 * @return The generated string representation.
	 */
	public String getStringRepresentation(LogEntry entry){
		StringBuilder sb = new StringBuilder();
		sb.append(entry.getLevel() + "<" + entry.getComponent() + "> " + dateFormat.format(entry.getTimestamp())+ ": ");
		sb.append(entry.getMessage());
		if(entry.getStackTrace() != null && printStackTrace){
			sb.append("\n\n");
			sb.append("\t====== Stack Trace ======\n");
			String[] lines = entry.getStackTrace().split("\n");
			for(String line : lines){
				sb.append("\t");
				sb.append(line);
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}
