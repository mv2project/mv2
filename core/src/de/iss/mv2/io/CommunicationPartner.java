package de.iss.mv2.io;

import java.io.IOException;

import de.iss.mv2.messaging.MV2Message;

/**
 * Represents a communication partner.
 * @author Marcel Singer
 *
 */
public interface CommunicationPartner {

	/**
	 * Sends the given message.
	 * @param m The message to send.
	 * @throws IOException If an I/O error occurs.
	 */
	public void send(MV2Message m) throws IOException;
	
}
