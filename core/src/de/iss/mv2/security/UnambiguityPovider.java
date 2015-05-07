package de.iss.mv2.security;

/**
 * A provider for checking if a value is unique in a certain context.
 * @author Marcel Singer
 *
 * @param <T> Specifies the type of value that can be checked by this provider.
 */
public interface UnambiguityPovider<T> {

	/**
	 * Checks if the given value is unique.
	 * @param value The value to check.
	 * @return {@code true} if the given value is unique.
	 */
	public boolean isUnambiguously(T value);
	
	
}
