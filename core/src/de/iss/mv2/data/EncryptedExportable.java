package de.iss.mv2.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.iss.mv2.security.AESInputStream;
import de.iss.mv2.security.AESOutputStream;
import de.iss.mv2.security.CryptoException;

/**
 * Defines a encryption-wrapper for other binary exportable objects.
 * 
 * @author Marcel Singer
 *
 */
public class EncryptedExportable extends Exportable {

	/**
	 * Holds the object to wrap.
	 */
	private final Exportable exportable;

	/**
	 * Creates a new instance of {@link EncryptedExportable}.
	 * 
	 * @param exportable
	 *            The object to wrap.
	 */
	public EncryptedExportable(Exportable exportable) {
		this.exportable = exportable;
	}

	/**
	 * Returns a digest to create a cryptographic hash based on the given
	 * password.
	 * 
	 * @param size
	 *            The key size (in bytes) needed byte cryptographic algorithm.
	 * @return The digest.
	 * @throws NoSuchAlgorithmException
	 *             If the desired digest-algorithm could not be found.
	 */
	protected MessageDigest getPassphraseDigest(int size)
			throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("SHA" + (size * 8),
				new BouncyCastleProvider());
	}

	/**
	 * Returns a random IV.
	 * 
	 * @return The generated random IV.
	 */
	protected SecureRandom getIVRandom() {
		return new SecureRandom();
	}

	/**
	 * Returns the needed IV size based on the key size.
	 * 
	 * @param keySize
	 *            The key size.
	 * @return The needed IV size.
	 */
	protected int getIVSize(int keySize) {
		return 16;
	}

	/**
	 * Returns the size of the key.
	 * 
	 * @return The key size in bytes.
	 */
	protected int getKeySize() {
		return 256 / 8;
	}

	/**
	 * Returns the cipher output stream.
	 * 
	 * @param baseStream
	 *            The underlying stream to write.
	 * @param key
	 *            The key to use.
	 * @param iv
	 *            The IV to use.
	 * @return The initialized cipher output stream.
	 * @throws CryptoException
	 *             If an exception occurs during the initialization of the
	 *             cipher.
	 */
	protected OutputStream getCipherOutputStream(OutputStream baseStream,
			byte[] key, byte[] iv) throws CryptoException {
		try {
			AESOutputStream out = new AESOutputStream(baseStream, key, iv);
			return out;
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * Returns the cipher input stream.
	 * 
	 * @param baseStream
	 *            The underlying stream to read.
	 * @param key
	 *            The key to use.
	 * @param iv
	 *            The IV to use.
	 * @return The initialized cipher input stream.
	 * @throws CryptoException
	 *             If an exception occurs during the initialization of the
	 *             cipher.
	 */
	protected InputStream getCipherInputStream(InputStream baseStream,
			byte[] key, byte[] iv) throws CryptoException {
		try {
			AESInputStream in = new AESInputStream(baseStream, key, iv);
			return in;
		} catch (Exception ex) {
			throw new CryptoException(ex);
		}
	}

	/**
	 * Finalizes the given cipher stream. e.g. write the final block
	 * 
	 * @param cipherStream
	 *            The cipher stream to finalize.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void finalizeCipher(OutputStream cipherStream) throws IOException {
		cipherStream.flush();
		cipherStream.close();
	}

	/**
	 * Returns the character set for encoding the password.
	 * 
	 * @return The character set for encoding the password.
	 */
	protected Charset getPassphraseCharset() {
		return StandardCharsets.UTF_8;
	}

	/**
	 * Encrypts and exports the wrapped object.
	 * 
	 * @param passphrase
	 *            The password that should be used for encryption.
	 * @param out
	 *            The output stream to export.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws NoSuchAlgorithmException
	 *             If the encryption algorithm was not found.
	 */
	public void export(String passphrase, OutputStream out) throws IOException,
			NoSuchAlgorithmException {
		int keySize = getKeySize();
		int ivSize = getIVSize(keySize);
		byte[] iv = new byte[ivSize];
		getIVRandom().nextBytes(iv);
		MessageDigest md = getPassphraseDigest(keySize);
		byte[] key = md.digest(passphrase.getBytes(getPassphraseCharset()));
		out.write(iv);
		out.flush();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream cOut = getCipherOutputStream(baos, key, iv);
		export(cOut);
		finalizeCipher(cOut);
		baos.writeTo(out);
		out.flush();

	}

	/**
	 * Imports and decrypts an object from the given input stream.
	 * 
	 * @param passphrase
	 *            The password that should be used for encryption.
	 * @param in
	 *            The input stream to import.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws NoSuchAlgorithmException
	 *             If the encryption algorithm was not found.
	 */
	public void importData(String passphrase, InputStream in)
			throws IOException, NoSuchAlgorithmException {

		int keySize = getKeySize();
		int ivSize = getIVSize(keySize);
		byte[] iv = BinaryTools.readBytes(in, ivSize);
		MessageDigest md = getPassphraseDigest(keySize);
		byte[] key = md.digest(passphrase.getBytes(getPassphraseCharset()));
		InputStream cIn = getCipherInputStream(in, key, iv);
		importData(cIn);
	}

	@Override
	protected void exportContent(OutputStream out) throws IOException {
		exportable.exportContent(out);
	}

	@Override
	protected void importContent(InputStream in) throws IOException {
		exportable.importContent(in);
	}

}
