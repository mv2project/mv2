package de.iss.mv2.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import de.iss.mv2.messaging.MessageQueryResponseMessage;

/**
 * A test for the {@link MessageQueryResponseMessage} class.
 * @author Marcel Singer
 *
 */
public class MessageQueryResponseMessageTest {

	
	/**
	 * Tests the behavior when setting correct identifiers.
	 */
	@Test
	public void testGetSetIdentifiers() {
		long[] identifier = new long[]{0, 8, 15};
		MessageQueryResponseMessage mqr = new MessageQueryResponseMessage();
		mqr.setMessageIdentifiers(identifier);
		assertArrayEquals(identifier, mqr.getMessageIdentifiers());
	}
	
	/**
	 * Tests the behavior when setting a {@code null} identifier.
	 */
	@Test
	public void testGetSetNullIdentifiers(){
		MessageQueryResponseMessage mqr = new MessageQueryResponseMessage();
		mqr.setMessageIdentifiers(null);
		assertArrayEquals(new long[0], mqr.getMessageIdentifiers());
	}
	
	

}
