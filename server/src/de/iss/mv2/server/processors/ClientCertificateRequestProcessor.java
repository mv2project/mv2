package de.iss.mv2.server.processors;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.NoSuchElementException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.logging.LoggerManager;
import de.iss.mv2.messaging.ClientCertificateRequest;
import de.iss.mv2.messaging.ClientCertificateResponse;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.UnableToProcessMessage;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;

/**
 * Handles incoming {@link ClientCertificateRequest} messages.
 * @author Marcel Singer
 *
 */
public class ClientCertificateRequestProcessor implements MessagePreProcessor,
		MessageProcessor {

	/**
	 * The manage to use for requesting web spaces.
	 */
	private WebSpaceManager manager;
	
	/**
	 * Creates a new instance of {@link ClientCertificateRequestProcessor}.
	 * @param manager
	 */
	public ClientCertificateRequestProcessor(WebSpaceManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if(message == null || !ClientCertificateRequest.class.isAssignableFrom(message.getClass())) return false;
		ClientCertificateRequest ccr = (ClientCertificateRequest) message;
		String identifier = ccr.getIdentifier();
		WebSpace webSpace = null;
		try{
			webSpace = manager.getWebSpace(identifier);
		}catch(NoSuchElementException ex){
			webSpace = null;
		}
		if(webSpace == null){
			UnableToProcessMessage utp = new UnableToProcessMessage();
			utp.setCause("There is no webspace for the given identifier.");
			client.send(utp);
			return true;
		}
		try{
			ClientCertificateResponse response = new ClientCertificateResponse();
			response.setCertificate(webSpace.getCertificate().getCertificate());
			client.send(response);
		}catch(CertificateEncodingException ex){
			LoggerManager.getCurrentLogger().push(ex);
			UnableToProcessMessage utp = new UnableToProcessMessage();
			utp.setCause("There was an internal server error.");
			client.send(utp);
		}
		return true;
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if(message == null) return message;
		if(message.getMessageIdentifier() == STD_MESSAGE.CLIENT_CERTIFICATE_REQUEST.getIdentifier()){
			ClientCertificateRequest ccr = new ClientCertificateRequest();
			MV2Message.merge(ccr, message);
			return ccr;
		}
		return message;
	}

}
