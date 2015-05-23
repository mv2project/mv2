package de.iss.mv2.server.processors;

import de.iss.mv2.messaging.EncryptedMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.security.SymmetricKeyGenerator;
import de.iss.mv2.server.io.ClientThread;

/**
 * A preprocessor to decrypt incoming encrypted messages.
 * @author Marcel Singer
 *
 */
public class EncryptedMessagePreProcessor implements MessagePreProcessor {

	/**
	 * The thread of the client.
	 */
	private final ClientThread client;
	
	/**
	 * Creates a new instance of {@link EncryptedMessagePreProcessor}
	 * @param client The client which receives the messages.
	 */
	public EncryptedMessagePreProcessor(ClientThread client) {
		this.client = client;
	}

	/**
	 * Decrypts incoming encrypted messages.
	 */
	@Override
	public MV2Message prepare(MV2Message message) {
		if(message == null) return message;
		if(EncryptedMessage.class.isAssignableFrom(message.getClass())){
			EncryptedMessage enc = (EncryptedMessage) message;
			SymmetricKeyGenerator keyGen = client.getParser().getCryptorSettings().getKeyGenerator();
			keyGen.setFixedIV(enc.getUsedSymmetricIV());
			keyGen.setFixedKey(enc.getUsedSymmetricKey());
		}
		return message;
	}

}
