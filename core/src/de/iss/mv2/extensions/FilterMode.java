package de.iss.mv2.extensions;

/**
 * Defines the behavior when one or more filters are applied.
 * @author Marcel Singer
 *
 */
public enum FilterMode {

	/**
	 * The item should be selected if the file passed all filters.
	 */
	AND,
	/**
	 * The item should be selected if the file passed at leased one filter.
	 */
	OR
	
}