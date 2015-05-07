package de.iss.mv2.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PublicKey;

/**
 * Configures the cryptographic processing of all processed messages.
 * {@link AESWithRSACryptoSettings} is the default implementation of this interface.
 * @author Marcel Singer
 *
 */
public interface MessageCryptorSettings extends
		Cloneable<MessageCryptorSettings> {

	/**
	 * Returns the key generator to be used.
	 * @return The key generator.
	 */
	public SymetricKeyGenerator getKeyGenerator();

	/**
	 * Creates an output stream for encrypted data output. The stream must be fully initialized.
	 * @param baseStream The underlying stream.
	 * @param key The key to use for encryption.
	 * @param iv The IV to use for encryption.
	 * @return A fully initialized stream.
	 * @throws IOException if an I/O error occurs.
	 * @throws CryptoException Is thrown if there was an error during the initialization of the encryption.
	 */
	public OutputStream getSymetricEncryptionStream(OutputStream baseStream,
			byte[] key, byte[] iv) throws IOException, CryptoException;

	/**
	 * Creates an input stream for encrypted data input. The stream must be fully initialized.
	 * @param baseStream The underlying stream containing the encrypted data.
	 * @param key The key to use.
	 * @param iv The IV to use.
	 * @return A fully initialized stream.
	 * @throws IOException if an I/O error occurs.
	 * @throws CryptoException Is thrown if there was an error during the initialization of the cipher. 
	 */
	public InputStream getSymetricDecryptionStream(InputStream baseStream,
			byte[] key, byte[] iv) throws IOException, CryptoException;

	/**
	 * Returns the desired key size of this settings.
	 * @return The desired symmetric key size in bytes.
	 */
	public int getDesiredSymetricKeySize();

	/**
	 * Creates an output stream for asymmetric encrypted data output. The stream must be fully initialized.
	 * @param baseStream The underlying stream.
	 * @param key The key to use for encryption.
	 * @return A fully initialized stream.
	 * @throws IOException if an I/O error occurs.
	 * @throws CryptoException Is thrown if there was an error during the initialization of the encryption.
	 */
	public OutputStream getAsymetricEncryptionStream(OutputStream baseStream,
			PublicKey key) throws IOException, CryptoException;

	
	/**
	 * Creates an input stream for asymmetric encrypted data input. The stream must be fully initialized.
	 * @param baseStream The underlying stream containing the encrypted data.
	 * @param key The key to use.
	 * @return A fully initialized stream.
	 * @throws IOException if an I/O error occurs.
	 * @throws CryptoException Is thrown if there was an error during the initialization of the cipher. 
	 */
	public InputStream getAsymenticDecryptionStram(InputStream baseStream,
			KeyPair key) throws IOException, CryptoException;

	/**
	 * Returns the name of the symmetric encryption algorithm.
	 * @return The name of the symmetric encryption algorithm.
	 */
	public String getSymetricAlgorithmName();

	/**
	 * Returns the name of the asymmetric encryption algorithm.
	 * @return The name of the asymmetric encryption algorithm.
	 */
	public String getAsymetricAlgorithmName();

}
