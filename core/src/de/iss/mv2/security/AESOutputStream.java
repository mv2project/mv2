package de.iss.mv2.security;

import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Eine Ausgabe-Stream-Klasse zur AES-verschlüsselten ausgabe von Daten.
 */
public final class AESOutputStream extends CipherOutputStream {

	/**
	 * Legt eine neue Instanz von {@link AESOutputStream} an.
	 * 
	 * @param baseStream
	 *            Gibt den zur Ausgabe zu verwendenden Basisstream an.
	 * @param key
	 *            Gibt den zur Verschlüsselung zu verwendenden Schlüssel an. Aus
	 *            der Schlüssellänge folgt die AES-Stärke. Gültige
	 *            Schlüssellängen sind:
	 *            <ul>
	 *            <li>128 bit (16byte)</li>
	 *            <li>256 bit (32byte)*</li>
	 *            <li>512 bit (64byte)*</li>
	 *            </ul>
	 *            <i>* Hinweis:</i>&nbsp;Unter Umständen kann für diese
	 *            Schlüsselstärken eine Einschränkung bestehen. Siehe
	 *            {@link Cipher}.
	 * @param iv
	 *            Gibt den zur Verschlüsselung zu verwendenden
	 *            Initialisierungsvektor an. Dieser hat immer eine Länge von 128
	 *            bit (16byte).
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
	public AESOutputStream(OutputStream baseStream, byte[] key, byte[] iv)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, InvalidKeyException {
		super(baseStream, getCipher("PKCS5Padding", key, iv));
	}

	
	/**
	 * Returns a fully initialized cipher for AES-encrypted data output.
	 * @param padding Specifies the padding to be used.
	 * @param key The key.
	 * @param iv The IV.
	 * @return A fully initialized AES-cipher in cipher-block-chaining mode and the specified padding.
	 * @throws NoSuchPaddingException If the given padding was not found.
	 * @throws NoSuchAlgorithmException If the system can't find an implementation of the AES-algorithm.
	 * @throws InvalidAlgorithmParameterException If the IV is invalid.
	 * @throws InvalidKeyException If the key is invalid.
	 */
	private static final Cipher getCipher(String padding, byte[] key, byte[] iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException {
		Cipher c = Cipher.getInstance("AES/CBC/" + padding);
		IvParameterSpec pS = new IvParameterSpec(iv);
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), pS);

		return c;
	}

	
	/**
	 * Erzeugt einen kryptographisch zufälligen AES-Schlüssel mit der angegebenen Stärke.
	 * @param strength Die Stärke des Schlüssels in Bits. Siehe {@link AESOutputStream#AESOutputStream(OutputStream, byte[], byte[])}
	 * @return Gibt den zufällig erzeugten AES-Schlüssel zurück.
	 */
	public static byte[] getRandomAESKey(int strength){
		SecureRandom sr = new SecureRandom();
		byte[] key = new byte[strength / 8];
		sr.nextBytes(key);
		return key;
	}
	
	/**
	 * Erzeugt einen kryptograpisch zufälligen AES-IV.
	 * @return Gibt den zufällig erzeugten AES-IV zurück. Dieser hat immer eine Länge von 16 byte.
	 */
	public static byte[] getRandomAESIV(){
		SecureRandom sr = new SecureRandom();
		byte[] iv = new byte[16];
		sr.nextBytes(iv);
		return iv;
	}
	
}
