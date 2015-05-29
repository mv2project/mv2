package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.util.Base64;

import org.junit.Test;

import de.iss.mv2.TestConstants;
import de.iss.mv2.security.PassphraseInflator;

/**
 * A test case for the {@link PassphraseInflator} class.
 * @author Marcel Singer
 *
 */
public class PasspharseInflatorTest {

	/**
	 * Tests if the generated digest for the primary passphrase can be reproduced.
	 * @throws Exception If anything goes wrong.
	 */
	@Test
	public void testReproducePrimaryPassphrase() throws Exception {
		PassphraseInflator pi = new PassphraseInflator();
		byte[] a = pi.getPrimaryPassphraseDigest(TestConstants.SOME_STRING.toCharArray());
		pi = new PassphraseInflator();
		byte[] b = pi.getPrimaryPassphraseDigest(TestConstants.SOME_STRING.toCharArray());
		assertArrayEquals(a, b);
		System.out.println(" Primary: " + Base64.getEncoder().encodeToString(a));
 	}

	/**
	 * Tests if the generated secondary digest can be reproduced.
	 * @throws Exception If anything goes wrong.
	 */
	@Test
	public void testReproduceSecondaryPassphrase() throws Exception{
		PassphraseInflator pi = new PassphraseInflator();
		byte[] a = pi.getSecondaryPasspharseDigest(TestConstants.SOME_STRING.toCharArray());
		pi = new PassphraseInflator();
		byte[] b = pi.getSecondaryPasspharseDigest(TestConstants.SOME_STRING.toCharArray());
		assertArrayEquals(a, b);
		System.out.println("Seconary: " + Base64.getEncoder().encodeToString(a));
	}
	
	/**
	 * Tests it the generated primary and secondary digests do not match.
	 * @throws Exception If anything goes wrong.
	 */
	@Test
	public void testPrimarySecondaryUnequal() throws Exception{
		PassphraseInflator pi = new PassphraseInflator();
		byte[] p = pi.getPrimaryPassphraseDigest(TestConstants.SOME_STRING.toCharArray());
		byte[] s = pi.getSecondaryPasspharseDigest(TestConstants.SOME_STRING.toCharArray());
		if(p.length != s.length) fail("The primary and secondary digest must be the same size.");
		for(int i=0; i<p.length; i++){
			if(p[i] != s[i]) return;
		}
		fail("The primary and secondary digests must not be equal.");
	}
	
}
