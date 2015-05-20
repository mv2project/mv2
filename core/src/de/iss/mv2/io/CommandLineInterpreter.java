package de.iss.mv2.io;

import java.util.ArrayList;
import java.util.List;

/**
 * An interpreter for command line arguments.
 * @author Marcel Singer
 *
 */
public class CommandLineInterpreter {

	/**
	 * Holds the command line arguments.
	 */
	private final String[] arguments;
	
	/**
	 * Determines if the options are case sensitive.
	 */
	private boolean caseSensitiveOptions;
	
	/**
	 * Creates a new instance of {@link CommandLineInterpreter}.
	 * @param args The current arguments to interpret.
	 * @param caseSensitiveOptions {@code true} if the options are case sensitive.
	 */
	public CommandLineInterpreter(String[] args, boolean caseSensitiveOptions) {
		this.arguments = args;
		this.caseSensitiveOptions = caseSensitiveOptions;
	}

	
	/**
	 * Returns {@code true} if the arguments contain the given option.
	 * @param option The option to search.
	 * @return {@code true} if the arguments contain the given option.
	 */
	public boolean hasOption(String option){
		if(option == null || option.isEmpty()) return false;
		if(!option.startsWith("-")) option = "-" + option;
		for(String arg : arguments){
			if(stringEquals(arg, option)) return true;
		}
		return false;
	}
	
	/**
	 * Returns {@code true} if the given option is specified and all excludes aren't. If the option is not specified, this method will also return {@code true}.
	 * @param option The allowed option.
	 * @param excludes The options that aren't allowed if the option is present.
	 * @return {@code true} if the given option is specified and all excludes aren't. If the option is not specified, this method will also return {@code true}.
	 */
	public boolean isExclusive(String option, String... excludes){
		if(!hasOption(option)) return true;
		for(String excl : excludes){
			if(hasOption(excl)) return false;
		}
		return true;
	}
	
	/**
	 * Returns the extras specified for an option. For example: "-e extraE1 extraE2 -f extraF1 extraF3 extraF4".
	 * @param option The option thats extra values should be returned.
	 * @return The extras specified for an option.
	 */
	public List<String> getExtras(String option){
		if(option == null || option.isEmpty()) return new ArrayList<String>();
		if(!option.startsWith("-")) option = "-" + option;
		boolean found = false;
		List<String> extras = new ArrayList<String>();
		for(String arg : arguments){
			if(found){
				if(arg.startsWith("-")) break;
				extras.add(arg);
			}else{
				found = stringEquals(arg, option);
			}
		}
		return extras;
	}
	
	/**
	 * Tests if both given strings are equal. Depending on the value of {@link CommandLineInterpreter#caseSensitiveOptions} the case is ignored.
	 * @param a The first string.
	 * @param b The second string.
	 * @return {@code true} if both strings are equal.
	 */
	private boolean stringEquals(String a, String b){
		if(caseSensitiveOptions) return a.equals(b); else return a.equalsIgnoreCase(b);
	}
	
}
