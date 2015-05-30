package de.iss.mv2.extensions;

/**
 * An extension.
 * @author Marcel Singer
 *
 */
public interface Extension {

	/**
	 * Returns the user friendly name of this extension.
	 * @return The user friendly name of this extension.
	 */
	public String getName();
	/**
	 * Returns the identifier of this extension.
	 * @return The identifier of this extension.
	 */
	public String getIdentifier();
	/**
	 * Returns the version of this extension.
	 * @return The version of this extension.
	 */
	public Iterable<Object> getExtensionObjects();
	
	/**
	 * Is invoked at the startup of this extension.
	 * @return {@code true}, if the startup was successful.
	 */
	public boolean startup();
	/**
	 * Is invoked at the teardown of this extension.
	 * @return {@code true}, if the teardown was successful.
	 */
	public boolean teardown(); 
	
	
}