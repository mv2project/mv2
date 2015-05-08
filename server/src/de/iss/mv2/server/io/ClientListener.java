package de.iss.mv2.server.io;

import de.iss.mv2.messaging.MV2Message;

/**
 * A listener that is notified when a client event occurs.
 * @author Marcel Singer
 *
 */
public interface ClientListener {

	/**
	 * Handles a disconnected client.
	 * @param client The client that disconnected.
	 */
	public void clientDisconnected(ClientThread client);
	/**
	 * Handles a received client message.
	 * @param client The client thats message was received.
	 * @param m The received message.
	 */
	public void messageReceived(ClientThread client, MV2Message m);
	
}
