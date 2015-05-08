package de.iss.mv2.server.processors;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.CertificateResponeMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.server.io.MV2Server;

/**
 * A processor to process a certificate request.
 * @author Marcel Singer
 *
 */
public class CertProcessor implements MessageProcessor {

	/**
	 * Holds the server that received the message.
	 */
	private final MV2Server server;

	/**
	 * Creates a new instance of {@link CertProcessor}.
	 * @param server The server that receives the messages.
	 */
	public CertProcessor(MV2Server server) {
		this.server = server;
	}
	

	@Override
	/**
	 * Answers to a received certificate request.
	 */
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (message.getMessageIdentifier() == STD_MESSAGE.CERT_REQUEST
				.getIdentifier() && server != null) {
			CertificateResponeMessage response;
			String binding = client.getHostName();
			try {
				response = new CertificateResponeMessage(
						server.getCertificate(binding));
				client.send(response);
				return true;
			} catch (CertificateEncodingException e) {
			}
		}
		return false;
	}

}
