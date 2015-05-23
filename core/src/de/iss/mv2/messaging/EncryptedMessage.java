package de.iss.mv2.messaging;

import static de.iss.mv2.data.BinaryTools.readAll;
import static de.iss.mv2.data.BinaryTools.readBuffer;
import static de.iss.mv2.data.BinaryTools.readInt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;

import de.iss.mv2.security.CryptoException;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.security.SymmetricKeyGenerator;

/**
 * Represents an encrypted message.
 * @author Marcel Singer
 *
 */
public class EncryptedMessage extends MV2Message {

	/**
	 * Holds the public key of the receiver.
	 */
	private final PublicKey publicKey;
	/**
	 * Holds the encryption settings to use for this message.
	 */
	private final MessageCryptorSettings settings;
	/**
	 * Holds the message identifier of the content message.
	 */
	private int contentMessageIdentifier;
	/**
	 * Holds the key pair of the receiving instance.
	 */
	private final KeyPair keyPair;
	/**
	 * Holds the symmetric key.
	 */
	private byte[] symmetricKey;
	/**
	 * Holds the symmetric IV.
	 */
	private byte[] symmetricIV;
	/**
	 * Holds the name of the symmetric algorithm.
	 */
	private String symmetricAlgorithm;

	/**
	 * Creates a new instance of {@link EncryptedMessage}.
	 * @param settings The settings to use for encryption.
	 * @param publicKey The public key of the receiver.
	 * @param messageIdentifier The identifier of the nested message.
	 */
	public EncryptedMessage(MessageCryptorSettings settings,
			PublicKey publicKey, int messageIdentifier) {
		super(STD_MESSAGE.ENCRYPTED_MESSAGE);
		this.contentMessageIdentifier = messageIdentifier;
		this.settings = settings;
		this.publicKey = publicKey;
		keyPair = null;
	}

	/**
	 * Creates a new instance of {@link EncryptedMessage}.
	 * @param settings The settings to use.
	 * @param privateKey The key pair of the receiving instance.
	 */
	public EncryptedMessage(MessageCryptorSettings settings, KeyPair privateKey) {
		super(STD_MESSAGE.ENCRYPTED_MESSAGE);
		this.contentMessageIdentifier = STD_MESSAGE.UNKNOWN.getIdentifier();
		this.settings = settings;
		this.publicKey = null;
		this.keyPair = privateKey;
	}

	/**
	 * Creates a new instance of {@link EncryptedMessage}.
	 * @param settings The settings to use for encryption.
	 * @param publicKey The public key of the receiver.
	 * @param message The message to be encrypted.
	 */
	public EncryptedMessage(MessageCryptorSettings settings,
			PublicKey publicKey, STD_MESSAGE message) {
		super(STD_MESSAGE.ENCRYPTED_MESSAGE);
		this.publicKey = publicKey;
		this.settings = settings;
		this.contentMessageIdentifier = message.getIdentifier();
		keyPair = null;
	}

	/**
	 * A constant defining the encoding of the parameters.
	 */
	private static final Charset ENCODING = StandardCharsets.US_ASCII;

	@Override
	protected void doSerialize(OutputStream out, Charset encoding)
			throws IOException {
		SymmetricKeyGenerator keyGen = settings.getKeyGenerator();
		byte[] key = keyGen.getRandomKey(settings.getDesiredSymmetricKeySize());
		byte[] iv = keyGen.getRandomIV();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!keyGen.hasFixedKeyAndIV()) {
			this.symmetricIV = iv;
			this.symmetricKey = key;
			OutputStream cryptoStream = settings.getSymmetricEncryptionStream(
					baos, key, iv);
			MV2Message contentMessage = getContentMessage();
			contentMessage.serialize(cryptoStream);
			cryptoStream.flush();
			cryptoStream.close();
			MessageField symAlgorithmName = new MessageField(
					DEF_MESSAGE_FIELD.SYMMETRIC_ALGORITHM,
					settings.getSymmetricAlgorithmName());
			symAlgorithmName.setEncoding(ENCODING);
			MessageField asymAlgorithmName = new MessageField(
					DEF_MESSAGE_FIELD.ASYMMETRIC_ALGORITHM,
					settings.getAsymmetricAlgorithmName());
			asymAlgorithmName.setEncoding(ENCODING);
			ByteArrayOutputStream keyOut = new ByteArrayOutputStream();
			OutputStream keyCryptoOut = settings.getAsymmetricEncryptionStream(
					keyOut, publicKey);
			keyCryptoOut.write(iv);
			keyCryptoOut.write(key);
			keyCryptoOut.flush();
			keyCryptoOut.close();
			MessageField keyField = new MessageField(DEF_MESSAGE_FIELD.ENCRYPTION_KEY,
					keyOut.toByteArray());
			keyField.setEncoding(ENCODING);
			keyField.serialize(out);
			symAlgorithmName.serialize(out);
			asymAlgorithmName.serialize(out);
		} else {
			OutputStream cryptoStream = settings.getSymmetricEncryptionStream(
					baos, key, iv);
			MV2Message contentMessage = getContentMessage();
			contentMessage.serialize(cryptoStream);
			cryptoStream.flush();
			cryptoStream.close();
		}
		this.symmetricIV = iv;
		this.symmetricKey = key;
		out.write(baos.toByteArray());
		out.flush();
	}

	@Override
	public void deserialize(InputStream in) throws IOException {
		clearFields();
		byte[] key;
		byte[] iv;
		if (!settings.getKeyGenerator().hasFixedKeyAndIV()) {
			int identifier;
			int length;
			InputStream buffer;
			for (int i = 0; i < 3; i++) {
				identifier = readInt(in);
				length = readInt(in);
				buffer = readBuffer(in, length);
				MessageField mf = new MessageField(identifier);
				mf.deserialize(buffer);
				mf.completeDeserialize(ENCODING);
				setMessageField(mf, true);
			}
			MessageField symNameField = getFieldOrThrow(DEF_MESSAGE_FIELD.SYMMETRIC_ALGORITHM);
			MessageField asymNameField = getFieldOrThrow(DEF_MESSAGE_FIELD.ASYMMETRIC_ALGORITHM);
			this.symmetricAlgorithm = symNameField.getContent();
			if (!symNameField.getContent().equals(
					settings.getSymmetricAlgorithmName())
					|| !asymNameField.getContent().equals(
							settings.getAsymmetricAlgorithmName())) {
				throw new CryptoException();
			}
			byte[] keyData = getFieldDataArrayValue(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, null);
			ByteArrayInputStream bin = new ByteArrayInputStream(keyData);
			InputStream asymIn = settings.getAsymmenticDecryptionStream(bin,
					keyPair);
			keyData = readAll(asymIn);
			asymIn.close();
			int ivLength = settings.getKeyGenerator().getRandomIV().length;
			iv = Arrays.copyOfRange(keyData, 0, ivLength);

			key = Arrays.copyOfRange(keyData, ivLength, keyData.length);
		} else {
			SymmetricKeyGenerator keyGen = settings.getKeyGenerator();
			iv = keyGen.getRandomIV();
			key = keyGen.getRandomKey();
		}
		this.symmetricKey = key;
		this.symmetricIV = iv;
		InputStream symIn = settings.getSymmetricDecryptionStream(in, key, iv);
		ByteArrayInputStream content = new ByteArrayInputStream(readAll(symIn));
		MessageParser parser = new MessageParser(content);
		parser.setKey(keyPair);
		parser.setEncryptionSetting(settings);
		MV2Message contentMessage = parser.readNext();
		copyMessage(contentMessage);
	}

	/**
	 * Copies all fields and settings of the given message.
	 * @param m The message to copie from.
	 */
	private void copyMessage(MV2Message m) {
		clearFields();
		contentMessageIdentifier = m.getMessageIdentifier();
		for (MessageField mf : m.getCleanedFields()) {
			setMessageField(mf, true);
		}
	}

	@Override
	public int getMessageIdentifier() {
		return contentMessageIdentifier;
	}

	/**
	 * Returns the nested message.
	 * @return The nested message.
	 */
	private MV2Message getContentMessage() {
		MV2Message m = new MV2Message(contentMessageIdentifier);
		m.setEncoding(getEncoding());
		for (MessageField mf : getCleanedFields()) {
			m.setMessageField(mf, true);
		}
		return m;
	}

	/**
	 * Encrypts a given message. 
	 * @param m The message to encrypt.
	 * @param settings The settings to use for encryption.
	 * @param key The public key of the receiver.
	 * @param skipIfEncypted {@code true} if the encryption should be skipped if the given message is already encrypted.
	 * @return The encrypted message.
	 */
	public static EncryptedMessage encrypt(MV2Message m,
			MessageCryptorSettings settings, PublicKey key,
			boolean skipIfEncypted) {
		if (m == null)
			return null;
		if (EncryptedMessage.class.isAssignableFrom(m.getClass())
				&& skipIfEncypted)
			return (EncryptedMessage) m;
		EncryptedMessage enc = new EncryptedMessage(settings, key,
				m.getMessageIdentifier());
		enc.setEncoding(m.getEncoding());
		for (MessageField mf : m.getCleanedFields()) {
			enc.setMessageField(mf, true);
		}
		return enc;
	}

	/**
	 * Returns the used symmetric key.
	 * @return The used symmetric key.
	 */
	public byte[] getUsedSymmetricKey() {
		return symmetricKey;
	}

	/**
	 * Returns the used symmetric IV.
	 * @return The used symmetric IV.
	 */
	public byte[] getUsedSymmetricIV() {
		return symmetricIV;
	}

	/**
	 * Returns the name of the used symmetric algorithm.
	 * @return The name of the used symmetric algorithm.
	 */
	public String getUsedSymmetricAlgorithm() {
		return symmetricAlgorithm;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ENCRYPTED ] " + super.toString());
		Encoder enc = Base64.getEncoder();
		sb.append("\t"
				+ new MessageField(DEF_MESSAGE_FIELD.SYMMETRIC_ALGORITHM,
						getUsedSymmetricAlgorithm()) + "\n");
		sb.append("\t[i] USED_KEY: " + enc.encodeToString(getUsedSymmetricKey())
				+ "\n");
		sb.append("\t[i] USED_IV: " + enc.encodeToString(getUsedSymmetricIV())
				+ "\n");
		return sb.toString();
	}

}
