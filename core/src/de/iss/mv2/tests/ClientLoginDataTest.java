package de.iss.mv2.tests;

import static org.junit.Assert.assertArrayEquals;

import java.security.SecureRandom;

import org.junit.Test;

import de.iss.mv2.messaging.ClientLoginData;

/**
 * A test for the {@link ClientLoginData} message.
 * 
 * @author Marcel Singer
 *
 */
public class ClientLoginDataTest {

	/**
	 * Test the behavior when setting valid data.
	 */
	@Test
	public void testSetValid() {
		SecureRandom sr = new SecureRandom();
		byte[] data = new byte[10];
		sr.nextBytes(data);
		ClientLoginData cld = new ClientLoginData();
		cld.setDecryptedTestPhrase(data);
		assertArrayEquals(data, cld.getDecryptedTestPhrase());
	}

	/**
	 * Tests the behavior when setting a {@code null} test phrase.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullTestPhrase() {
		new ClientLoginData().setDecryptedTestPhrase(null);
	}

	/**
	 * Tests the behavior when setting an empty test phrase.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyTestPhrase() {
		new ClientLoginData().setDecryptedTestPhrase(new byte[0]);
	}

}
