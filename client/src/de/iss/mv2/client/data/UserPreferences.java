package de.iss.mv2.client.data;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Stores the preferences of the current user.
 * @author Marcel Singer
 *
 */
public class UserPreferences {
	
	/**
	 * Holds the current instance.
	 */
	private static final UserPreferences instance = new UserPreferences();
	
	/**
	 * The object to store the preferences.
	 */
	private final Preferences prefs;
	
	/**
	 * A constant defining the key of the server address preference.
	 */
	private static final String SERVER_ADDRESS_KEY = "SERVER_ADDRESS";
	/**
	 * A constant defining the key of the server port preference.
	 */
	private static final String SERVER_PORT_KEY = "SERVER_PORT";
	/**
	 * A constatnt defining the key of the local directory to store client data.
	 */
	private static final String STORE_ADDRESS = "STORE_ADDRESS";
	
	
	/**
	 * Creates a new instance of {@link UserPreferences}.
	 */
	private UserPreferences(){
		prefs = Preferences.userNodeForPackage(UserPreferences.class);
	}
	
	
	
	/**
	 * Returns the address of the server to connect to.
	 * @param defaultValue The default value that should be returned if no value was found.
	 * @return The address of the server to connect to.
	 * @deprecated To be able to manage more then one server and for security reasons this method is deprecated. A replacement of this functionality will be added soon.
	 */
	@Deprecated
	public String getServerAddress(String defaultValue){
		return prefs.get(SERVER_ADDRESS_KEY, defaultValue);
	}
	
	/**
	 * Sets the address of the server to connect to.
	 * @param address The address to set.
	 * @return {@code true} if the address was successfully set.
	 * @deprecated To be able to manage more then one server and for security reasons this method is deprecated. A replacement of this functionality will be added soon.
	 */
	@Deprecated
	public boolean setServerAddress(String address){
		prefs.put(SERVER_ADDRESS_KEY, address);
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Indicates if there is a defined server to connect to.
	 * @return {@code true} if there is a server to connect to.
	 * @deprecated To be able to manage more then one server and for security reasons this method is deprecated. A replacement of this functionality will be added soon.
	 */
	@Deprecated
	public boolean hasServerAddress(){
		return getServerAddress(null) != null;
	}
	
	/**
	 * Gets the port of the server to connect to.
	 * @param defaultValue The value that should be returned if no port was found.
	 * @return The port of the server to connect to.
	 * @deprecated To be able to manage more then one server and for security reasons this method is deprecated. A replacement of this functionality will be added soon.
	 */
	@Deprecated
	public int getServerPort(int defaultValue){
		return prefs.getInt(SERVER_PORT_KEY, defaultValue);
	}
	
	/**
	 * Sets the port of the server to connect to.
	 * @param port The port to set.
	 * @return {@code true} if the port was successfully set.
	 * @deprecated To be able to manage more then one server and for security reasons this method is deprecated. A replacement of this functionality will be added soon.
	 */
	@Deprecated
	public boolean setServerPort(int port){
		prefs.putInt (SERVER_PORT_KEY, port);
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Indicates if there is a defined port to connect to.
	 * @return {@code true} if there is a port to connect to.
	 * @deprecated To be able to manage more then one server and for security reasons this method is deprecated. A replacement of this functionality will be added soon.
	 */ 
	@Deprecated
	public boolean hasServerPort(){
		return getServerPort(-1) != -1;
	}
	
	/**
	 * Returns the current {@link UserPreferences}.
	 * @return The current preferences.
	 */
	public static UserPreferences getPreferences(){
		return instance;
	}
	
	/**
	 * Returns the address of the directory where the client data is or should be stored.
	 * @param defaultValue The value that should be returned if no address is found.
	 * @return The address of the directory where the client data is stored.
	 */
	public String getStoreAddress(String defaultValue){
		return prefs.get(STORE_ADDRESS, defaultValue);
	}
	
	/**
	 * Indicates if there is a defined directory to store the clients data.
	 * @return {@code true} if there is a defined directory to store the clients data.
	 */
	public boolean hasStoreAddress(){
		return getStoreAddress(null) != null;
	}
	
	/**
	 * Sets the address of the directory where the clients data is stored.
	 * @param storeAddress The address of the directory to set.
	 * @return {@code true} if the address was successfully set.
	 */
	public boolean setStoreAddress(String storeAddress){
		prefs.put(STORE_ADDRESS, storeAddress);
		try{
			prefs.sync();
		}catch(BackingStoreException ex){
			return false;
		}
		return true;
	}

}
