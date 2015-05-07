package de.iss.mv2.tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PublicKey;

import org.junit.Test;

import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageParser;
import de.iss.mv2.messaging.SpaceCreationRequest;
import de.iss.mv2.security.RSAOutputStream;

/**
 * A test for the {@link SpaceCreationRequest}-class.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceCreationRequestTest implements TestConstants {

	/**
	 * Tests the get and set method for the identifier.
	 */
	@Test
	public void testGetSetIdentifier() {
		SpaceCreationRequest scr = new SpaceCreationRequest();
		scr.setSpaceIdentifier("qwertz");
		assertEquals(scr.getSpaceIdentifier(), "qwertz");
	}

	/**
	 * Test the behavior when setting a {@code null}-identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullIdenifier() {
		SpaceCreationRequest scr = new SpaceCreationRequest();
		scr.setSpaceIdentifier(null);
	}

	/**
	 * Tests the behavior when setting an empty identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyIdentifier() {
		new SpaceCreationRequest().setSpaceIdentifier("");
	}

	/**
	 * Tests the get and set method for the country.
	 */
	@Test
	public void testSetCountry() {
		SpaceCreationRequest scr = new SpaceCreationRequest();
		scr.setCountry("DE");
		assertEquals(scr.getCountry(), "DE");
	}

	/**
	 * Tests the behavior when setting a country with more then two characters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetLongCountry() {
		new SpaceCreationRequest().setCountry("Deutschland");
	}

	/**
	 * Tests the behavior when setting a country with one character.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetShortCountry() {
		new SpaceCreationRequest().setCountry("D");
	}

	/**
	 * Tests the behavior when setting an empty country.
	 */
	public void testSetEmptyCountry() {
		SpaceCreationRequest scr = new SpaceCreationRequest();
		scr.setCountry("");
		assertEquals(scr.getCountry(), "");
	}

	/**
	 * Tests the behavior when setting an {@code null} country.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullCountry() {
		new SpaceCreationRequest().setCountry(null);
	}

	/**
	 * Tests the (de)serialization.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void testSerialization() throws Exception {
		SpaceCreationRequest scr = new SpaceCreationRequest();
		scr.setCountry("DE");
		scr.setLocation("HN");
		scr.setSpaceIdentifier("test123");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		scr.serialize(baos);
		baos.flush();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		MessageParser mp = new MessageParser(bais);
		MV2Message m = mp.readNext();
		SpaceCreationRequest scr2 = new SpaceCreationRequest();
		MV2Message.merge(scr2, m);
		assertEquals(scr.getSpaceIdentifier(), scr2.getSpaceIdentifier());
		assertEquals(scr.getLocation(), scr2.getLocation());
		assertEquals(scr.getCountry(), scr2.getCountry());
	}

	/**
	 * Tests the get and set method for the public key.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void testGetSetKey() throws Exception {
		PublicKey pk = RSAOutputStream.getRandomRSAKey(CLIENT_KEY_SIZE)
				.getPublic();
		SpaceCreationRequest scr = new SpaceCreationRequest();
		scr.setPublicKey(pk);
		assertEquals(scr.getKeyAlgorithmName(), pk.getAlgorithm());
		assertEquals(scr.getPublicKey(), pk);
	}

}
