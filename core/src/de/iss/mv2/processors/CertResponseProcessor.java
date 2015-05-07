package de.iss.mv2.processors;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.io.MV2Client;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.STD_MESSAGE_FIELD;
import de.iss.mv2.security.CertificateLoader;

/**
 * Processes a certificate response.
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
	 * @param client The client which receives the messages.
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
			String content = message.getFieldValue(
					STD_MESSAGE_FIELD.CONTENT_BASE64, "");
			if (content.isEmpty()) {
				System.err
						.println("The content of the certificate-response was empty!");
				return false;
			}
			byte[] dat = Base64.getDecoder().decode(content);
			try{
				X509Certificate cert = CertificateLoader.load(dat);
				if(!this.client.checkTrust(cert)){
					System.err.println("The certificate is not trusted.");
					return false;
				}
				System.out.println("Server-Certificate received!");
				this.client.setServerCert(cert);
				return true;
			}catch(CertificateException ex){
				ex.printStackTrace();
			}
		}
		return false;
	}

}
