package de.iss.mv2.client.processors;

import java.io.IOException;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.DomainNamesResponse;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A processor to handle domain names responses.
 * 
 * @author Marcel Singer
 *
 */
public class DomainNamesResponseProcessor implements MessagePreProcessor,
		MessageProcessor {

	/**
	 * Holds the client.
	 */
	private final MV2Client client;

	/**
	 * Creates a new instance of {@link DomainNamesResponseProcessor}.
	 * 
	 * @param client
	 *            The client using this processor.
	 */
	public DomainNamesResponseProcessor(MV2Client client) {
		this.client = client;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if(message == null) return false;
		if(DomainNamesResponse.class.isAssignableFrom(message.getClass())){
			DomainNamesResponse dnr = (DomainNamesResponse) message;
			this.client.setAlternativeNames(dnr.getAvailableDomainNames());
			return true;
		}
		return false;
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if(message.getMessageIdentifier() == STD_MESSAGE.DOMAIN_NAMES_RESPONSE.getIdentifier()){
			DomainNamesResponse resp = new DomainNamesResponse();
			MV2Message.merge(resp, message);
			return resp;
		}
		return message;
	}

}
