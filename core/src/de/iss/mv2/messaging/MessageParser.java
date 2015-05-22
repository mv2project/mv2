package de.iss.mv2.messaging;

import static de.iss.mv2.data.BinaryTools.readBuffer;
import static de.iss.mv2.data.BinaryTools.readInt;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;

import de.iss.mv2.security.CryptoException;
import de.iss.mv2.security.MessageCryptorSettings;

/**
 * A parser for parsing received {@link MV2Message}s.
 * @author Marcel Singer
 *
 */
public class MessageParser {

	/**
	 * Hold the input stream to parse.
	 */
	private final InputStream in;

	/**
	 * Holds the asymmetric key of this instance.
	 */
	private KeyPair asymmetricKey = null;
	/**
	 * Holds the settings to use for encryption.
	 */
	private MessageCryptorSettings settings = null;

	/**
	 * Creates a new {@link MessageParser} to parse from the given input stream.
	 * @param in The input stream to read.
	 */
	public MessageParser(InputStream in) {
		this.in = in;
	}

	/**
	 * Sets the asymmetric key pair of this instance.
	 * @param key
	 */
	public void setKey(KeyPair key) {
		this.asymmetricKey = key;
	}

	/**
	 * Sets the encryption settings to be used.
	 * @param settings The encryption settings to set.
	 */
	public void setEncryptionSetting(MessageCryptorSettings settings) {
		this.settings = settings;
	}

	/**
	 * Reads the next {@link MV2Message} from the input stream.
	 * @return The read message.
	 * @throws IOException if an I/O error occurs.
	 * @throws CryptoException if there was an exception during cryptographic operations.
	 */
	public MV2Message readNext() throws IOException, CryptoException {
		int identifier = readInt(in);
		int length = readInt(in);
		InputStream messageStream = readBuffer(in, length);
		MV2Message message;
		if (identifier != STD_MESSAGE.ENCRYPTED_MESSAGE.getIdentifier()) {
			message = new MV2Message(identifier);
		} else {
			if (!settings.getKeyGenerator().hasFixedKeyAndIV()) {
				if (asymmetricKey == null)
					throw new CryptoException(
							"The key needed for decryption was not set.");

			}
			if (settings == null)
				throw new CryptoException(
						"The settings needed for decryption was not set.");
			message = new EncryptedMessage(settings, asymmetricKey);
		}
		message.deserialize(messageStream);
		return message;
	}

	/**
	 * Returns the encryption settings.
	 * @return The encryption settings.
	 */
	public MessageCryptorSettings getCryptorSettings() {
		return settings;
	}

}
