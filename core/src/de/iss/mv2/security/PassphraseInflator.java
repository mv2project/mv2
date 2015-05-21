package de.iss.mv2.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A helper class to generate a second passphrase form a given one.
 * 
 * @author Marcel Singer
 *
 */
public class PassphraseInflator {

	/**
	 * Holds the name of the default message digest algorithm.
	 */
	private static final String DEF_DIGEST = "SHA-256";

	/**
	 * Holds the digest algorithm to use.
	 */
	private MessageDigest digest;

	/**
	 * Holds the charset to be used for the encoding of the passphrase.
	 */
	private Charset encoding = StandardCharsets.UTF_8;

	/**
	 * Creates a new instance of {@link PassphraseInflator}.
	 * 
	 * @param digest
	 *            The message digest algorithm to use.
	 */
	public PassphraseInflator(MessageDigest digest) {
		this.digest = digest;
	}

	/**
	 * Creates a new instance of {@link PassphraseInflator} based on the
	 * algorithm specified by {@link PassphraseInflator#DEF_DIGEST} (SHA256).
	 * 
	 * @throws NoSuchAlgorithmException
	 *             If the implementation of the default message digest algorithm
	 *             was not found.
	 */
	public PassphraseInflator() throws NoSuchAlgorithmException {
		this(MessageDigest.getInstance(DEF_DIGEST));
	}

	/**
	 * Returns the digest of the given passphrase.
	 * 
	 * @param passphrase
	 *            The passphrase thats digest should be computed.
	 * @return The digest of the given passphrase.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public byte[] getPrimaryPassphraseDigest(char[] passphrase)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(baos, encoding);
		writer.write(passphrase);
		writer.flush();
		return digest.digest(baos.toByteArray());
	}

	/**
	 * Returns the inflated passphrases digest.
	 * 
	 * @param passphrase
	 *            The passhrase thats inflated digest should be returned.
	 * @return The inflated passphrases digest.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public byte[] getSecondaryPasspharseDigest(char[] passphrase)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(baos, encoding);
		writer.write(passphrase);
		writer.flush();
		byte[] clearData = baos.toByteArray();
		ArrayList<Byte> clearList = new ArrayList<Byte>();
		for (byte b : clearData)
			clearList.add(b);
		clearData = null;
		byte[] primaryData = getPrimaryPassphraseDigest(passphrase);
		ArrayList<Byte> primaryList = new ArrayList<Byte>();
		for (byte b : primaryData)
			primaryList.add(b);
		primaryData = null;
		byte[] newData = new byte[primaryList.size() + clearList.size()];
		Iterator<Byte> clearIter = clearList.iterator();
		Iterator<Byte> primaryIter = primaryList.iterator();
		for (int i = 0; i < newData.length; i++) {
			if (i % 2 == 0) {
				if (clearIter.hasNext()) {
					newData[i] = clearIter.next();
				} else {
					newData[i] = primaryIter.next();
				}
			} else {
				if (primaryIter.hasNext()) {
					newData[i] = primaryIter.next();
				} else {
					newData[i] = clearIter.next();
				}
			}
		}
		return digest.digest(newData);
	}

}
