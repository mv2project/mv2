package de.iss.mv2.client.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.data.MailMessage;
import de.iss.mv2.client.io.ClientProvider;
import de.iss.mv2.client.io.ClientProviderImpl;
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.messaging.ContentMessage;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.EncryptedMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageDeliveryRequest;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.security.MessageCryptorSettings;

/**
 * A procedure that sends a message.
 * 
 * @author Marcel Singer
 *
 */
public class MailSendingProcedure extends
		MessageProcedure<RequestException, Void> {

	/**
	 * Holds the message to send.
	 */
	private final MailMessage message;
	/**
	 * Holds the mail account to use.
	 */
	private final MailBoxSettings account;

	/**
	 * The provider to encrypt the message content.
	 */
	private final MessageCryptorSettings cryptorSettings;

	/**
	 * Holds the client provider.
	 */
	private final ClientProvider clientProvider = new ClientProviderImpl();

	/**
	 * Creates a new instance of {@link MailSendingProcedure}.
	 * 
	 * @param cryptorSettings
	 *            The settings to use to encrypt the message.
	 * @param message
	 *            The message to send.
	 * @param account
	 *            The account to be used to send the given message.
	 */
	public MailSendingProcedure(MessageCryptorSettings cryptorSettings,
			MailMessage message, MailBoxSettings account) {
		super(null);
		this.cryptorSettings = cryptorSettings;
		this.message = message;
		this.account = account;
	}

	@Override
	protected Void doPerform(MV2Client client) throws IOException,
			RequestException {
		int steps = message.getReceivers().length;
		MV2Client current = null;
		X509Certificate clientCert = null;
		Map<String, Exception> fails = new HashMap<String, Exception>();
		String receiver;
		X509Certificate cert = null;
		ContentMessage contentMessage = new ContentMessage();
		try {
			clientCert = account.getClientCertificate();
			contentMessage.setSender(clientCert);
		} catch (Exception ex) {
			throw new RequestException(
					"Can't get the current clients certificate.", ex);
		}
		contentMessage.setSubject(message.getSubject());
		contentMessage.setContent(message.getContent());
		contentMessage.setReceivers(message.getReceivers());
		try {
			contentMessage.sign(account.getClientKey());
		} catch (InvalidKeyException | SignatureException
				| OperatorCreationException | IllegalStateException
				| PKCSException e1) {
			throw new RequestException("Can't sign the message.", e1);
		}

		for (int i = 0; i < steps; i++) {
			if (current != null) {
				current.disconnect();
				current = null;
			}
			update((int) ((i * 100.0) / (steps * 1.0)));
			receiver = message.getReceivers()[i];
			try {
				current = clientProvider.connectToWebSpace(receiver);

				new InitialProcedure(current).runImmediate();
			} catch (ProcedureException | IOException e) {
				fails.put(message.getReceivers()[i], e);
				update("Can't connect to '" + receiver + "'.");
				continue;
			}
			update("Requesting the certificate of '" + receiver + "'.");
			try {
				cert = getCertificate(current, receiver);
			} catch (ProcedureException | IOException e) {
				fails.put("Can't obtain the receivers ('" + receiver
						+ "') certificate.", e);
				update("Can't obtain the receivers ('" + receiver
						+ "') certificate.");
				continue;
			}
			EncryptedMessage em = null;
			cryptorSettings.getKeyGenerator().setFixedIV(null);
			cryptorSettings.getKeyGenerator().setFixedKey(null);
			em = new EncryptedMessage(cryptorSettings, cert.getPublicKey(),
					STD_MESSAGE.CONTENT_MESSAGE);
			MV2Message.merge(em, contentMessage);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			em.serialize(baos);
			baos.flush();
			MessageDeliveryRequest mdr = new MessageDeliveryRequest();
			mdr.setReceiver(receiver);
			mdr.setContent(baos.toByteArray());
			baos.close();
			current.send(mdr);
			MV2Message m = current.handleNext();
			try {
				if (m.getMessageIdentifier() == STD_MESSAGE.UNABLE_TO_PROCESS
						.getIdentifier())
					throw new RequestException(
							"The message could not be sent to '"
									+ receiver
									+ "'. The server responded with: "
									+ m.getFieldStringValue(
											DEF_MESSAGE_FIELD.CAUSE, ""));
				if (m.getMessageIdentifier() != STD_MESSAGE.MESSAGE_DELIVERY_RESPONSE
						.getIdentifier())
					throw new RequestException(
							"The server did not answer with the expected message.");
			} catch (RequestException ex) {
				fails.put(ex.getMessage(), ex);
				update(ex.getMessage());
				continue;
			}
		}
		if (current != null)
			current.disconnect();
		return null;
	}

	/**
	 * Requests the certificate of the given receiver.
	 * 
	 * @param client
	 *            The client to use for the request.
	 * @param receiver
	 *            The receiver thats certificate should be obtained.
	 * @return The certificate of the given receiver.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws ProcedureException
	 *             If the procedure fails for any other reason.
	 */
	private X509Certificate getCertificate(MV2Client client, String receiver)
			throws IOException, ProcedureException {
		ClientCertificateRequestProcedure ccrp = new ClientCertificateRequestProcedure(
				client, receiver);
		return ccrp.runImmediate();
	}

	@Override
	protected void handleCommunicationException(IOException exception) {

	}

	@Override
	protected void handleProcedureException(Throwable exception) {

	}

}
