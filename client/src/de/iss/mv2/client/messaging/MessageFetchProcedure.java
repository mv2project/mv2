package de.iss.mv2.client.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import de.iss.mv2.client.data.MailMessage;
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.messaging.ContentMessage;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageFetchRequest;
import de.iss.mv2.messaging.MessageFetchResponse;
import de.iss.mv2.messaging.MessageParser;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.security.AESWithRSACryptoSettings;
import de.iss.mv2.security.CertificateNameReader;
import de.iss.mv2.security.MessageCryptorSettings;

/**
 * A procedure to request the mail with by a given identifier.
 * 
 * @author Marcel Singer
 *
 */
public class MessageFetchProcedure extends
		MessageProcedure<RequestException, MailMessage> {

	/**
	 * Holds the identifier of the message to fetch.
	 */
	private final long messageIdentifier;

	/**
	 * Holds the cryptor settings to decrypt the message.
	 */
	private final MessageCryptorSettings cryptorSettings;

	/**
	 * Holds the key pair of this instance.
	 */
	private final KeyPair key;

	/**
	 * Creates a new instance of {@link MessageFetchProcedure}
	 * 
	 * @param client
	 *            The client to use.
	 * @param messageIdentifier
	 *            The identifier of the message to fetch.
	 * @param key
	 *            The key pair of this instance.
	 */
	public MessageFetchProcedure(MV2Client client, long messageIdentifier,
			KeyPair key) {
		super(client);
		this.messageIdentifier = messageIdentifier;
		cryptorSettings = new AESWithRSACryptoSettings();
		this.key = key;
	}

	@Override
	protected MailMessage doPerform(MV2Client client) throws IOException,
			RequestException {
		try {
			new InitialProcedure(client).runImmediate();
		} catch (ProcedureException e) {
			throw new RequestException("Could not initialized the connection.", e);
		}
		if (!client.isLoggedIn())
			throw new RequestException("The client is not logged in.");
		MessageFetchRequest mfr = new MessageFetchRequest();
		mfr.setIdentifier(messageIdentifier);
		client.send(mfr);
		MV2Message m = client.handleNext();
		if (m.getMessageIdentifier() == STD_MESSAGE.UNABLE_TO_PROCESS
				.getIdentifier()) {
			throw new RequestException("The request failed: "
					+ m.getFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, ""));
		}
		if (m.getMessageIdentifier() != STD_MESSAGE.MESSAGE_FETCH_RESPONSE
				.getIdentifier()) {
			throw new RequestException(
					"The server did not answer with the expected message.");
		}
		MessageFetchResponse response = new MessageFetchResponse();
		MV2Message.merge(response, m);
		ByteArrayInputStream bais = new ByteArrayInputStream(
				response.getContentMessage());
		MessageParser parser = new MessageParser(bais);
		parser.setEncryptionSetting(cryptorSettings);
		parser.setKey(key);
		MV2Message clearMessage = parser.readNext();
		bais.close();
		if (clearMessage.getMessageIdentifier() != STD_MESSAGE.CONTENT_MESSAGE
				.getIdentifier()) {
			throw new RequestException("The message was not encoded correctly.");
		}
		ContentMessage contentMessage = new ContentMessage();
		MV2Message.merge(contentMessage, clearMessage);
		MailMessage result = new MailMessage();
		result.setContent(contentMessage.getContent());
		result.setTimestamp(response.getTimestamp());
		result.setReceivers(contentMessage.getReceivers());
		result.setSubject(contentMessage.getSubject());
		try {
			X509Certificate cert = contentMessage.getSender();
			result.setSenderCertificate(cert);
			CertificateNameReader cnr = new CertificateNameReader(
					cert.getSubjectX500Principal());
			result.setSender(cnr.getCommonName());
		} catch (CertificateException e) {
			throw new RequestException(
					"The certificate of the sending instance could not be retrieved.");
		}
		try {
			result.setHadValidSignature(contentMessage.verifySignature());
		} catch (InvalidKeyException | SignatureException
				| CertificateException e) {
			throw new RequestException(
					"The signature of this message could not be validated.");
		}
		return result;
	}

	@Override
	protected void handleCommunicationException(IOException exception) {

	}

	@Override
	protected void handleProcedureException(Throwable exception) {

	}

}
