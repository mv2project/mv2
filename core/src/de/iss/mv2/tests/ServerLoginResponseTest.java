package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.security.SecureRandom;

import org.junit.Test;

import de.iss.mv2.messaging.ServerLoginResponse;

/**
 * A test for the {@link ServerLoginResponse}.
 * 
 * @author Marcel Singer
 *
 */
public class ServerLoginResponseTest {

	/**
	 * Test the behavior when setting valid parameters.
	 */
	@Test
	public void testSetNormal() {
		SecureRandom sr = new SecureRandom();
		byte[] testPhrase = new byte[10];
		sr.nextBytes(testPhrase);
		byte[] testHash = new byte[7];
		sr.nextBytes(testHash);
		String hashAlg = "SHA256";
		ServerLoginResponse slr = new ServerLoginResponse();
		slr.setTestPhrase(testPhrase);
		slr.setHashAlgorithm(hashAlg);
		slr.setTestPhraseHash(testHash);
		
		assertArrayEquals(testPhrase, slr.getTestPhrase());
		assertArrayEquals(testHash, slr.getTestPhraseHash());
		assertEquals(hashAlg, slr.getHashAlgorithm());
	}

	/**
	 * Tests the behavior when setting a null test phrase.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullTestPhrase() {
		new ServerLoginResponse().setTestPhrase(null);
	}

	/**
	 * Test the behavior when setting an empty test phrase.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyTestPhrase() {
		new ServerLoginResponse().setTestPhrase(new byte[0]);
	}

	/**
	 * Tests the behavior when setting a null test phrase hash.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullTestPhraseHash() {
		new ServerLoginResponse().setTestPhraseHash(null);
	}

	/**
	 * Tests the behavior when setting an empty test phrase hash.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyTestPhraseHash() {
		new ServerLoginResponse().setTestPhraseHash(new byte[0]);
	}

	/**
	 * Tests the behavior when setting a null hash algorithm name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullHashAlgorithm() {
		new ServerLoginResponse().setHashAlgorithm(null);
	}

	/**
	 * Tests the behavior when setting an empty hash algorithm name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyHashAlgorithm() {
		new ServerLoginResponse().setHashAlgorithm("");
	}

}
