package de.iss.mv2.server.data;

import java.security.PublicKey;

import de.iss.mv2.data.Certificate;
import de.iss.mv2.security.UnambiguityPovider;

/**
 * A class to manage web spaces.
 * @author Marcel Singer
 *
 */
public interface WebSpaceManager extends UnambiguityPovider<String> {
	
	/**
	 * Creates a new web space.
	 * @param identifier The identifier of the web space to create.
	 * @param cert The certificate of the web space to create.
	 * @return The created web space.
	 */
	public WebSpace createWebSpace(String identifier, Certificate cert);
	
	/**
	 * Tests if this manager will be able to create a web space with the given data.
	 * @param identifier The identifier of the web space to create.
	 * @param location The location of the requesting instance.
	 * @param state The state of the requesting instance.
	 * @param country The country of the requesting instance.
	 * @param organization The organization of the requesting instance.
	 * @param organizationUnit The organization unit of the requesting instance.
	 * @param key The public key of the requesting instance.
	 * @return {@code true} if it will probably be possible to create a web space with the given data.
	 */
	public boolean canCreate(String identifier, String location, String state, String country, String organization, String organizationUnit, PublicKey key);
	
	/**
	 * Returns the web space with the given identifier.
	 * @param identifier The identifier of the web space to return.
	 * @return The web space with the given identifier.
	 */
	public WebSpace getWebSpace(String identifier);

}
