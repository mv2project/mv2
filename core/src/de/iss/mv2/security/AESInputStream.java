package de.iss.mv2.security;

import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Specifies an {@link InputStream} to read AES-encrypted data.
 * @author Marcel Singer
 * 
 */
public final class AESInputStream extends CipherInputStream {

	/**
	 * Legt eine neue Instanz von {@link AESInputStream} an.
	 * 
	 * @param baseStream
	 *            Gibt den zum Einlesen zu verwendende Basisstream an.
	 * @param key
	 *            Gibt den zur Entschlüsselung zu verwendenden AES-Schlüssel an.
	 * @param iv
	 *            Gibt den zur Entschlüsselung zu verwendendem
	 *            Initialisierungs-Vektor an.
	 * @throws NoSuchAlgorithmException
	 *             Tritt auf, wenn für den AES kein Provider gefunden wurde.
	 * @throws NoSuchPaddingException
	 *             Tritt auf, wenn kein gültiger Padding (PKCS5) Provider
	 *             gefunden wurde.
	 * @throws InvalidAlgorithmParameterException
	 *             Titt auf, wenn der angegebene IV ungültig ist.
	 * @throws InvalidKeyException
	 *             Tritt auf, wenn der angegegbene Schlüssel ungültig ist.
	 */
	public AESInputStream(InputStream baseStream, byte[] key, byte[] iv)
			throws InvalidKeyException, NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		super(baseStream, getCipher("PKCS5Padding", key, iv));
	}

	/**
	 * Returns a fully inilialized {@link Cipher}-object.
	 * @param padding Specifies the padding to be used.
	 * @param key The key to be used.
	 * @param iv The IV to be used.
	 * @return A fully initialized AES-cipher with cipher-block-chaining-mode.
	 * @throws NoSuchPaddingException If the given padding was not found.
	 * @throws NoSuchAlgorithmException If the system can't find an implementation of the AES-algorithm.
	 * @throws InvalidAlgorithmParameterException If the iv is invalid.
	 * @throws InvalidKeyException If the key was invalid.
	 */
	private static final Cipher getCipher(String padding, byte[] key, byte[] iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException {
		Cipher c = Cipher.getInstance("AES/CBC/" + padding);
		IvParameterSpec pS = new IvParameterSpec(iv);
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), pS);
		return c;
	}

}
