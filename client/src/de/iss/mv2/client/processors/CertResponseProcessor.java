package de.iss.mv2.client.processors;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.CertificateResponeMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * Processes a certificate response.
 * 
 * @author Marcel Singer
 *
 */
public class CertResponseProcessor implements MessageProcessor {

	/**
	 * The client receiving the messages.
	 */
	private final MV2Client client;

	/**
	 * Creates a new instance of {@link CertResponseProcessor}.
	 * 
	 * @param client
	 *            The client which receives the messages.
	 */
	public CertResponseProcessor(MV2Client client) {
		this.client = client;
	}

	@Override
	/**
	 * Checks incoming certificate responses and commits them to the client.
	 */
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (message.getMessageIdentifier() == STD_MESSAGE.CERT_RESPONSE
				.getIdentifier()) {
			CertificateResponeMessage crm = new CertificateResponeMessage();
			MV2Message.merge(crm, message);
			try {
				X509Certificate cert = crm.getCertificate();
				if (!this.client.checkTrust(cert)) {
					System.err.println("The certificate is not trusted.");
					return false;
				}
				System.out.println("Server-Certificate received!");
				this.client.setServerCert(cert);
				return true;
			} catch (CertificateException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

}
