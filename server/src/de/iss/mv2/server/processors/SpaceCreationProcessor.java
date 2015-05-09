package de.iss.mv2.server.processors;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import de.iss.mv2.data.Certificate;
import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.logging.LoggerManager;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageField;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.SpaceCreationRequest;
import de.iss.mv2.messaging.SpaceCreationResponse;
import de.iss.mv2.security.CertificateNameReader;
import de.iss.mv2.security.CertificateSigner;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;
import de.iss.mv2.server.io.MV2Server;

/**
 * Processes a {@link SpaceCreationRequest} message.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceCreationProcessor implements MessagePreProcessor,
		MessageProcessor {

	/**
	 * The server that receives the messages.
	 */
	private final MV2Server server;



	/**
	 * Holds the web space manager.
	 */
	private final WebSpaceManager webSpaceManager;

	/**
	 * Creates a new instance of {@link SpaceCreationProcessor}.
	 * 
	 * @param server
	 *            The server that receives the messages to process.
	 * @param webSpaceManager
	 *            The {@link WebSpaceManager} to use.
	 */
	public SpaceCreationProcessor(MV2Server server, WebSpaceManager webSpaceManager) {
		this.server = server;
		this.webSpaceManager = webSpaceManager;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (!SpaceCreationRequest.class.isAssignableFrom(message.getClass()))
			return false;
		SpaceCreationRequest scr = (SpaceCreationRequest) message;
		try {
			if (!webSpaceManager.canCreate(scr.getSigningRequest())) {
				fail(client, "The request was denied.");
				return true;
			}
		} catch (Exception e) {
			fail(client,
					"There was an internal server error processing the request.");
			return true;
		}
		CertificateNameReader cnr = new CertificateNameReader(scr
				.getSigningRequest().getSubject());
		String identifier = cnr.getCommonName();
		if (!identifier.contains("@")) {
			fail(client, "The identifier is invalid.");
			return true;
		}
		if (!webSpaceManager.isUnambiguously(identifier)) {
			fail(client, "The identifier is taken.");
			return true;
		}
		accept(client, identifier, scr);
		return true;
	}

	/**
	 * Accepts an {@link SpaceCreationRequest}.
	 * 
	 * @param partner
	 *            The requesting client.
	 * @param identifier
	 *            The identifier of the web space to create.
	 * @param scr
	 *            The request to accept.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private void accept(CommunicationPartner partner, String identifier,
			SpaceCreationRequest scr) throws IOException {
		PKCS10CertificationRequest csr = null;
		try {
			csr = scr.getSigningRequest();
		} catch (IOException e) {
			fail(partner, "The request could not be parsed.");
			return;
		}
		X509Certificate cert = null;
		try {
			CertificateSigner signer = new CertificateSigner(server.getCertificate(partner.getHostName()), server.getCertificateManager(), new SecureRandom());
			cert = signer.sign(server.getPrivateKey(partner.getHostName()), csr, false);
		} catch (OperatorCreationException | CertificateException
				| NoSuchAlgorithmException | InvalidKeySpecException e) {
			LoggerManager.getCurrentLogger().push(e);
			fail(partner, "There was an error signing the request.");
			return;
		}
		Certificate dataCert = server.getCertificateManager().load(
				cert.getSerialNumber());
		try {
			WebSpace ws = webSpaceManager.createWebSpace(identifier, dataCert);
			SpaceCreationResponse response = new SpaceCreationResponse();
			response.setCertificate(ws.getCertificate().getCertificate());
			partner.send(response);
		} catch (Exception ex) {
			LoggerManager.getCurrentLogger().push(ex);
			server.getCertificateManager().remove(cert, false);
			fail(partner, "There was an error creating the web space.");
			return;
		}
	}

	/**
	 * Fails a space creation request.
	 * 
	 * @param client
	 *            The client.
	 * @param cause
	 *            The cause of the failure.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private void fail(CommunicationPartner client, String cause)
			throws IOException {
		MV2Message m = new MV2Message(STD_MESSAGE.UNABLE_TO_PROCESS);
		m.setMessageField(new MessageField(DEF_MESSAGE_FIELD.CAUSE, cause),
				true);
		client.send(m);
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
