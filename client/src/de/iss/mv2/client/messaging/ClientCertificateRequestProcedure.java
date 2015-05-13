package de.iss.mv2.client.messaging;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.logging.LoggerManager;
import de.iss.mv2.messaging.ClientCertificateRequest;
import de.iss.mv2.messaging.ClientCertificateResponse;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A 
 * @author Marcel Singer
 *
 */
public class ClientCertificateRequestProcedure extends SwingMessageProcedure<RequestException, X509Certificate> {

	
	/**
	 * The identifier of the web space thats certificate should be requested.
	 */
	private final String identifier;
	
	/**
	 * Creates a new instance of {@link ClientCertificateRequestProcedure} with the identifier of the web space thats certificate should be requested.
	 * @param client The client to use for the requests.
	 * @param identifier The identifier of the web space thats certificate should be requested.
	 * 
	 */
	public ClientCertificateRequestProcedure(MV2Client client, String identifier) {
		super(client);
		this.identifier = identifier;
	}

	@Override
	protected X509Certificate doPerform(MV2Client client) throws RequestException,
			IOException {
		ClientCertificateRequest ccr = new ClientCertificateRequest();
		ccr.setIdentifier(identifier);
		client.send(ccr);
		MV2Message m = client.handleNext();
		if(m.getMessageIdentifier() == STD_MESSAGE.UNABLE_TO_PROCESS.getIdentifier()){
			throw new RequestException(m.getFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, ""));
		}
		if(m.getMessageIdentifier() != STD_MESSAGE.CLIENT_CERTIFICATE_RESPONSE.getIdentifier()) throw new RequestException("The server did not respond with the requested certificate.");
		ClientCertificateResponse response = new ClientCertificateResponse();
		MV2Message.merge(response, m);
		X509Certificate cert = null; 
		try{
			cert = response.getCertificate();
		} catch (CertificateException ex) {
			LoggerManager.getCurrentLogger().push(ex);
			cert = null;
		}
		if(cert == null) 
			throw new RequestException("The received certificate could not be processed.");
		return cert;
	}

	@Override
	protected void handleCommunicationException(IOException exception) {
		
	}

	@Override
	protected void handleProcedureException(Throwable exception) {
		
	}


}
