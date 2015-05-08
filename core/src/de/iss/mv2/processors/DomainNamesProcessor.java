package de.iss.mv2.processors;

import java.io.IOException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.DomainNamesRequest;
import de.iss.mv2.messaging.DomainNamesResponse;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A processor to handle incoming {@link DomainNamesRequest}s.
 * @author Marcel Singer
 *
 */
public class DomainNamesProcessor implements MessageProcessor, MessagePreProcessor {

	/**
	 * Holds the response to send.
	 */
	private DomainNamesResponse response = new DomainNamesResponse();
	
	/**
	 * Creates a new instance of {@link DomainNamesProcessor}.
	 * @param domainNames The available domain names.
	 */
	public DomainNamesProcessor(String[] domainNames) {
		response.setAvailableDomainNames(domainNames);
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if(message.getMessageIdentifier() == STD_MESSAGE.DOMAIN_NAMES_REQUEST.getIdentifier()){
			DomainNamesRequest dnr = new DomainNamesRequest();
			MV2Message.merge(dnr, message);
			return dnr;
		}
		return message;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if(message == null) return false;
		if(DomainNamesRequest.class.isAssignableFrom(message.getClass())){
			client.send(response);
			return true;
		}
		return false;
	}

}
