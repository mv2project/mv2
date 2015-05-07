package de.iss.mv2.security;

import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;

/**
 * Ein EingabeStream zur RSA-Entschlüsselung von Eingabedaten.<br />
 * <br />
 * <b>Hinweis:</b>&nbsp;Die Hinweise bzgl. den Einschränkungen von Kryptoalgorithmen (siehe Java-API von {@link Cipher}) sind zu beachten.
 */

public class RSAInputStream extends CipherInputStream{
	

	/**
	 * Erstellt eine neue Instanz von {@link RSAInputStream} anhand der gegebenen Eigenschaften.
	 * @param baseStream Gibt den zu entschlüsselnden Basis-EingabeStream an.
	 * @param key Gibt den zur Entschlüsselung zu verwendenden RSA-Schlüssel an.
	 * @throws InvalidKeyException Tritt auf, wenn der angegebene Schlüssel ungültig ist.
	 * @throws NoSuchPaddingException Tritt auf, wenn das Zielpadding (PKCS1) auf diesem System nicht verfügbar ist.
	 * @throws NoSuchAlgorithmException Tritt auf, wenn der RSA-Algorithmus auf diesem System (mit dieser Schlüsselstärke) nicht verfügbar ist.
	 * @throws InvalidAlgorithmParameterException Tritt auf, wenn die angegebenen Parameter nicht zum RSA-Algorithmus passen.
	 */
	public RSAInputStream(InputStream baseStream, PrivateKey key) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException{
		super(baseStream, getCipher("PKCS1Padding", key));
	}
	
	/**
	 * Erstellt eine neue Instanz von {@link RSAInputStream} anhand der gegebenen Eigenschaften.
	 * @param baseStream Gibt den zu entschlüsselnden Basis-EingabeStream an.
	 * @param kp Gibt den zur Entschlüsselung zu verwendenden RSA-Schlüssel an.
	 * @throws InvalidKeyException Tritt auf, wenn der angegebene Schlüssel ungültig ist.
	 * @throws NoSuchPaddingException Tritt auf, wenn das Zielpadding (PKCS1) auf diesem System nicht verfügbar ist.
	 * @throws NoSuchAlgorithmException Tritt auf, wenn der RSA-Algorithmus auf diesem System (mit dieser Schlüsselstärke) nicht verfügbar ist.
	 * @throws InvalidAlgorithmParameterException Tritt auf, wenn die angegebenen Parameter nicht zum RSA-Algorithmus passen.
	 */
	public RSAInputStream(InputStream baseStream, KeyPair kp) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException{
		this(baseStream, kp.getPrivate());
	}
	
	/**
	 * Returns a fully initialized cipher (to decrypt) using the RSA-algorithm with the given padding.
	 * @param padding The padding to be used. e.g. <plain>PKCS1Padding</plain>
	 * @param key The key to use.
	 * @return A fully initialized cipher in decryption mode.
	 * @throws NoSuchPaddingException If the given padding was not found.
	 * @throws NoSuchAlgorithmException If there is no implementing instance of the RSA-algorithm.
	 * @throws InvalidAlgorithmParameterException If a parameter used for initializing the cipher is invalid.
	 * @throws InvalidKeyException If the given key is invalid.
	 */
	private static final Cipher getCipher(String padding, PrivateKey key)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException {
		Cipher c = Cipher.getInstance("RSA/ECB/" + padding);
		c.init(Cipher.DECRYPT_MODE, key);
		return c;
	}

}
