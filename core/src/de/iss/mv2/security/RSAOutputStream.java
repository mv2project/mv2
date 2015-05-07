package de.iss.mv2.security;

import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

import sun.security.rsa.RSAKeyPairGenerator;

/**
 * Ein Ausgabe-Stream zur RSA-Verschlüsselung.<br />
 * <br />
 * <b>Hinweis:</b>&nbsp;Die Hinweise bzgl. den Einschränkungen von
 * Kryptoalgorithmen (siehe Java-API von {@link Cipher}) sind zu beachten.
 */
public final class RSAOutputStream extends CipherOutputStream {

	/**
	 * Erstellt eine neue Instanz von {@link RSAOutputStream}.
	 * 
	 * @param baseStream
	 *            Der zu Ausgabe zu verwendende Basis-Ausgabe-Stream.
	 * @param key
	 *            Gibt den zur Verschlüsselung zu verwendenden RSA-Schlüssel an.
	 * @throws InvalidKeyException
	 *             Tritt auf, wenn der angegebene Schlüssel ungültig ist.
	 * @throws NoSuchPaddingException
	 *             Tritt auf, wenn das Zielpadding (PKCS1) auf diesem System
	 *             nicht verfügbar ist.
	 * @throws NoSuchAlgorithmException
	 *             Tritt auf, wenn der RSA-Algorithmus auf diesem System (mit
	 *             dieser Schlüsselstärke) nicht verfügbar ist.
	 * @throws InvalidAlgorithmParameterException
	 *             Tritt auf, wenn die angegebenen Parameter nicht zum
	 *             RSA-Algorithmus passen.
	 */
	public RSAOutputStream(OutputStream baseStream, PublicKey key)
			throws InvalidKeyException, NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		super(baseStream, getCipher("PKCS1Padding", key));
	}

	/**
	 * Erstellt eine eneue Instanz von {@link RSAOutputStream}.
	 * 
	 * @param baseStream
	 *            Gibt den zur Ausgabe zu verwendenden Basis-Ausgabe-Stream an.
	 * @param kp
	 *            Gibt den zur Verschlüsselung zu verwendenden Schlüssel an.
	 * @throws InvalidKeyException
	 *             Tritt auf, wenn der angegebene Schlüssel ungültig ist.
	 * @throws NoSuchPaddingException
	 *             Tritt auf, wenn das Zielpadding (PKCS1) auf diesem System
	 *             nicht verfügbar ist.
	 * @throws NoSuchAlgorithmException
	 *             Tritt auf, wenn der RSA-Algorithmus auf diesem System (mit
	 *             dieser Schlüsselstärke) nicht verfügbar ist.
	 * @throws InvalidAlgorithmParameterException
	 *             Tritt auf, wenn die angegebenen Parameter nicht zum
	 *             RSA-Algorithmus passen.
	 */
	public RSAOutputStream(OutputStream baseStream, KeyPair kp)
			throws InvalidKeyException, NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		this(baseStream, kp.getPublic());
	}

	/**
	 * Returns a fully initialized ciper to use for RSA-encrypted data output.
	 * @param padding The padding to be used. e.g.: <plain>PKCS1Padding</plain>
	 * @param key The key to use.
	 * @return The initialized cipher.
	 * @throws NoSuchPaddingException If the given padding was not found.
	 * @throws NoSuchAlgorithmException If there is no implementation of the RSA-algorithm.
	 * @throws InvalidAlgorithmParameterException If a parameter of the cipher is invalid.
	 * @throws InvalidKeyException If the given key if invalid.
	 */
	private static final Cipher getCipher(String padding, PublicKey key)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException {
		Cipher c = Cipher.getInstance("RSA/ECB/" + padding);
		c.init(Cipher.ENCRYPT_MODE, key);
		return c;

	}

	/**
	 * Erstellt einen zufälligen RSA-Schlüssel mit der angegebenen Stärke.
	 * @param size Gibt die Zielstärke des zu generierenden RSA-Schlüssels (in Bit) an.
	 * @return Den generierten RSA-Schlüssel.
	 */
	public static KeyPair getRandomRSAKey(int size) {
		RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
		kpg.initialize(size, new SecureRandom());
		return kpg.generateKeyPair();
	}

}
