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
	 * Is invoked at the startup of this extension.
	 * @return {@code true}, if the startup was successful.
	 */
	public boolean startup();
	/**
	 * Is invoked at the teardown of this extension.
	 * @return {@code true}, if the teardown was successful.
	 */
	public boolean teardown();
	
	
	/**
	 * Returns the available extension objects that can be used.
	 * @param type The type of extension objects to be returned.
	 * @param matchExact {@code false} if objects that inherit from the given type should be included.
	 * @return The available extension objects.
	 */
	public <T> Iterable<T> getExtensionObjects(Class<? extends T> type, boolean matchExact);
	
	
}