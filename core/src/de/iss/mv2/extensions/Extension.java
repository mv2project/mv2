package de.iss.mv2.extensions;

/**
 * An extension.
 * @author Marcel Singer
 * @param <T> The type of the provided extension object.
 *
 */
public interface Extension<T> {

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
	 * Returns the provided extension object.
	 * @return The provided extension object.
	 */
	public T getExtensionObject();
	
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