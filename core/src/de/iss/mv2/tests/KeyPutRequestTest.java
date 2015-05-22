package de.iss.mv2.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.security.SecureRandom;

import org.junit.Test;

import de.iss.mv2.messaging.KeyPutRequest;

/**
 * A test for the {@link KeyPutRequest} message.
 * 
 * @author Marcel Singer
 *
 */
public class KeyPutRequestTest {

	/**
	 * Tests the behavior when setting a {@code null} key.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullKey() {
		new KeyPutRequest().setPrivateKey(null);
	}

	/**
	 * Tests the behavior when setting an empty key.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyKey() {
		new KeyPutRequest().setPrivateKey(new byte[0]);
	}

	/**
	 * Tests the behavior when setting a {@code null} passphrase.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullPassphrase() {
		new KeyPutRequest().setPassphrase(null);
	}

	/**
	 * Tests the behavior when setting an empty passphrase.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyPassphrase() {
		new KeyPutRequest().setPassphrase(new byte[0]);
	}

	/**
	 * Tests the behavior when requesting an unsed parameter.
	 */
	@Test
	public void testGetNullPassphraseAndKey() {
		KeyPutRequest kpr = new KeyPutRequest();
		assertNull(kpr.getPassphrase());
		assertNull(kpr.getPrivateKey());
	}

	/**
	 * Tests the get- and set-method for the passphrase.
	 */
	@Test
	public void testGetSetPassphrase() {
		SecureRandom rnd = new SecureRandom();
		byte[] value = new byte[25];
		rnd.nextBytes(value);
		KeyPutRequest kpr = new KeyPutRequest();
		kpr.setPassphrase(value);
		assertArrayEquals(value, kpr.getPassphrase());
	}

	/**
	 * Test the get- and set-method for the private key.
	 */
	@Test
	public void testGetSetKey() {
		SecureRandom rnd = new SecureRandom();
		byte[] value = new byte[25];
		rnd.nextBytes(value);
		KeyPutRequest kpr = new KeyPutRequest();
		kpr.setPrivateKey(value);
		assertArrayEquals(value, kpr.getPrivateKey());
	}

}
