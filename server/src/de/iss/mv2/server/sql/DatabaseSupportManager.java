package de.iss.mv2.server.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A class to manage existing {@link DatabaseSupportProvider}s.
 * @author Marcel Singer
 *
 */
public class DatabaseSupportManager {

	/**
	 * Holds the providers.
	 */
	private static final Map<String, DatabaseSupportProvider> providers = new HashMap<String, DatabaseSupportProvider>();
	
	/**
	 * Disables the instantiation of this class.
	 */
	private DatabaseSupportManager() {
		
	}

	
	static{
		register(new PostgreSQLDatabaseSupportProvider());
		register(new MySQLDatabaseSupportProvider());
	}
	
	/**
	 * Registers the given provider. 
	 * @param provider The provider to register.
	 */
	public static void register(DatabaseSupportProvider provider){
		if(provider == null) return;
		if(provider.getDatabaseTypeIdentifier() == null) return;
		String identifier = provider.getDatabaseTypeIdentifier().trim().toLowerCase();
		if(providers.containsKey(identifier)) return;
		providers.put(identifier, provider);
	}
	
	/**
	 * Returns the provider with the given identifier.
	 * @param identifier The identifier of the provider to return. It must not be {@code null}.
	 * @return The provider with the given identifier.
	 * @throws NoSuchElementException Is thrown if there is no provider for the given identifier.
	 */
	public static DatabaseSupportProvider get(String identifier) throws NoSuchElementException{
		if(identifier == null) throw new IllegalArgumentException("The identifier must not be null.");
		identifier = identifier.toLowerCase().trim();
		if(!providers.containsKey(identifier)) throw new NoSuchElementException("There is no provider for the given identifier ('" + identifier + "').");
		return providers.get(identifier);
	}
	
	
}
