package de.iss.mv2.server.processors;

import java.io.IOException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.UnableToProcessMessage;

/**
 * The abstract base class for default message processors.
 * @author Marcel Singer
 *
 */
public abstract class AbstractProcessor implements MessagePreProcessor, MessageProcessor {

	
	/**
	 * Fails a client request with the given cause.
	 * @param client The client thats request failed.
	 * @param cause The cause for the failure.
	 * @throws IOException If an I/O error occurs.
	 */
	protected void fail(CommunicationPartner client, String cause) throws IOException{
		UnableToProcessMessage utp = new UnableToProcessMessage();
		utp.setCause(cause);
		client.send(utp);
	}

}
