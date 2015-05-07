package de.iss.mv2.processors;

import java.io.IOException;
import java.security.PrivateKey;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.SpaceCreationRequest;
import de.iss.mv2.security.CertificateSigner;

/**
 * Processes a {@link SpaceCreationRequest} message.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceCreationProcessor implements MessagePreProcessor,
		MessageProcessor {

	/**
	 * Holds the signer to sign the web space certificate.
	 */
	private final CertificateSigner signer;

	/**
	 * Holds the private key of the signing instance.
	 */
	private final PrivateKey privateKey;

	/**
	 * Creates a new instance of {@link SpaceCreationProcessor}.
	 * 
	 * @param signer
	 *            The signer to use for signing the web space certificates.
	 * @param privateKey
	 *            The private key of the signing instance.
	 */
	public SpaceCreationProcessor(CertificateSigner signer,
			PrivateKey privateKey) {
		this.signer = signer;
		this.privateKey = privateKey;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (!SpaceCreationRequest.class.isAssignableFrom(message.getClass()))
			return false;
		SpaceCreationRequest scr = (SpaceCreationRequest) message;
		
		return true;
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if (message.getMessageIdentifier() == STD_MESSAGE.SPACE_CREATION_REQUEST
				.getIdentifier()) {
			SpaceCreationRequest scr = new SpaceCreationRequest();
			MV2Message.merge(scr, message);
			return scr;
		}
		return message;
	}

}
