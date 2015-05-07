package de.iss.mv2.security;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.NoSuchPaddingException;

/**
 * The default cryptographic settings using the AES-algorithm for symmetric and the RSA-algorithm for asymmetric encryption.
 * @author Marcel Singer
 *
 */
public class AESWithRSACryptoSettings implements MessageCryptorSettings {

	/**
	 * The {@link SymetricKeyGenerator} for generating the symmetic keys.
	 */
	private SymetricKeyGenerator keyGen = new AESKeyGenerator();
	
	/**
	 * Stores the strength of the key to generate in bytes.
	 */
	private int desiredSymetricKeySize = 16;

	/**
	 * Creates a new instance of {@link AESWithRSACryptoSettings}.
	 */
	public AESWithRSACryptoSettings() {

	}

	/**
	 * Sets the desired key strength.
	 * @param desiredSize The size of the symmetric key in bytes.
	 */
	public void setDesiredSymetricKeySize(int desiredSize) {
		this.desiredSymetricKeySize = desiredSize;
	}

	@Override
	public SymetricKeyGenerator getKeyGenerator() {
		return keyGen;
	}

	@Override
	public OutputStream getSymetricEncryptionStream(OutputStream baseStream,
			byte[] key, byte[] iv) throws IOException, CryptoException {
		AESOutputStream out;
		try {
			out = new AESOutputStream(baseStream, key, iv);
			return out;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public InputStream getSymetricDecryptionStream(InputStream baseStream,
			byte[] key, byte[] iv) throws IOException, CryptoException {
		AESInputStream in;
		try {
			in = new AESInputStream(baseStream, key, iv);
			return in;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public int getDesiredSymetricKeySize() {
		return desiredSymetricKeySize;
	}

	@Override
	public OutputStream getAsymetricEncryptionStream(OutputStream baseStream,
			PublicKey key) throws CryptoException, IOException {
		RSAOutputStream out;
		try {
			out = new RSAOutputStream(baseStream, key);
			return out;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public InputStream getAsymenticDecryptionStram(InputStream baseStream,
			KeyPair key) throws IOException, CryptoException {
		RSAInputStream in;
		try {
			in = new RSAInputStream(baseStream, key);
			return in;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * Constant defining the symmetric algorithm used by this settings.
	 */
	private static final String SYMETRIC_ALGORITHM_NAME = "AES/CBC/PKCS5";
	@Override
	public String getSymetricAlgorithmName() {
		return SYMETRIC_ALGORITHM_NAME;
	}

	/**
	 * Constant defining the asymmetric algorithm used byte this settings.
	 */
	private static final String ASYMENTRIC_ALGORITHM_NAME = "RSA/ECB/PKCS1";
	
	
	@Override
	public String getAsymetricAlgorithmName() {
		return ASYMENTRIC_ALGORITHM_NAME;
	}

	@Override
	public MessageCryptorSettings doClone() {
		AESWithRSACryptoSettings cl = new AESWithRSACryptoSettings();
		cl.desiredSymetricKeySize = this.desiredSymetricKeySize;
		cl.keyGen = this.keyGen.doClone();
		return cl;
	}

}
