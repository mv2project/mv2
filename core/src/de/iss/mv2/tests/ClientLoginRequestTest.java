package de.iss.mv2.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.iss.mv2.messaging.ClientLoginRequest;

/**
 * A test for the {@link ClientLoginRequest} message.
 * 
 * @author Marcel Singer
 *
 */
public class ClientLoginRequestTest {

	/**
	 * A valid identifier to test.
	 */
	private static final String VALID_IDENTIFIER = "max.mustermann@test.de";

	/**
	 * Tests the behavior when setting a valid identifier.
	 */
	@Test
	public void testSetNormalIdentifier() {
		ClientLoginRequest clr = new ClientLoginRequest();
		clr.setIdentifier(VALID_IDENTIFIER);
		assertEquals(clr.getIdentifier(), VALID_IDENTIFIER);
	}

	/**
	 * Test the behavior when setting a {@code null} identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullIdentifier() {
		new ClientLoginRequest().setIdentifier(null);
	}

	/**
	 * Test the behavior when setting an empty identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyIdentifier() {
		new ClientLoginRequest().setIdentifier("");
	}

}
