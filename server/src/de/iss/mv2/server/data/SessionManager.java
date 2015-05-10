package de.iss.mv2.server.data;

import de.iss.mv2.io.CommunicationPartner;

/**
 * Manages the state of a clients session.
 * @author Marcel Singer
 *
 */
public interface SessionManager {

	/**
	 * Returns if the given client is currently performing a login.
	 * @param client The client to test.
	 * @return {@code true} if the given client is currently performing a login.
	 */
	public boolean hasActiveLoginProcedure(CommunicationPartner client);

	/**
	 * Returns the identifier of the given clients login.
	 * @param client The client thats login identifier should be returned.
	 * @return The identifier of the given client.
	 */
	public String getLoginIdentifier(CommunicationPartner client);
	
	/**
	 * Returns {@code true} if the given client is authenticated.
	 * @param client The client to test.
	 * @return {@code true} if the given client is authenticated.
	 */
	public boolean isAuthenticated(CommunicationPartner client);
	
	/**
	 * Starts a login procedure for the given client.
	 * @param client The client that started the login procedure.
	 * @param identifier The identifier of the client.
	 */
	public void startLoginProcedure(CommunicationPartner client, String identifier);
	
	/**
	 * Cancels the login procedure for the given client.
	 * @param client The client thats login procedure should be canceled.
	 */
	public void cancelLoginProcedure(CommunicationPartner client);
	
}
