package de.iss.mv2.extensions;

/**
 * A filter to skip classes that should not be loaded.
 * @author Marcel Singer
 *
 */
public interface ClassFilter {

	/**
	 * Tests if the given class should be skipped.
	 * @param className The name of the class to test.
	 * @param current The class to test.
	 * @return {@code false} if the class should be skipped.
	 */
	public boolean filterClass(String className, Class<?> current);

}