package de.iss.mv2.messaging;

import java.io.IOException;

import de.iss.mv2.io.CommunicationPartner;

/**
 * Processes a received {@link MV2Message}.
 * @author Marcel Singer
 *
 */
public interface MessageProcessor {

	/**
	 * Processes a received message.
	 * @param client The client that sent the message.
	 * @param message The message to process.
	 * @return {@code true}, if this processor processed the given message.
	 * @throws IOException if an I/O error occurs.
	 */
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException;

}
