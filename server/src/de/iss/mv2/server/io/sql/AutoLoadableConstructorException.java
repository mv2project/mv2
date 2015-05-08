package de.iss.mv2.server.io.sql;

/**
 * An exception that is thrown if there is no load constructor inside an {@link AutoLoadable} class.
 * @author Marcel Singer
 * @deprecated See the documentation of {@link AutoLoadable}.
 */
@Deprecated
public class AutoLoadableConstructorException extends RuntimeException {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -3007452202879097189L;

	/**
	 * Creates a new instance of {@link AutoLoadableConstructorException}.
	 * @param refClass The affected class.
	 */
	public AutoLoadableConstructorException(
			Class<? extends AutoLoadable> refClass) {
		super("The class '" + refClass.getName()
				+ "' doesn't define the constructor neeeded for autoloading.");
	}

}
