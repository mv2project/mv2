package de.iss.mv2.server.data;

import java.util.NoSuchElementException;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import de.iss.mv2.data.Certificate;
import de.iss.mv2.security.UnambiguityPovider;

/**
 * A class to manage web spaces.
 * 
 * @author Marcel Singer
 *
 */
public interface WebSpaceManager extends UnambiguityPovider<String> {

	/**
	 * Creates a new web space.
	 * 
	 * @param identifier
	 *            The identifier of the web space to create.
	 * @param cert
	 *            The certificate of the web space to create.
	 * @return The created web space.
	 */
	public WebSpace createWebSpace(String identifier, Certificate cert);

	/**
	 * Tests if this manager will be able to create a web space from the given
	 * request.
	 * 
	 * @param request
	 *            The request to test.
	 * @return {@code true} if this manager will probably be able to create a
	 *         web space.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given request is {@code null}.
	 */
	public boolean canCreate(PKCS10CertificationRequest request)
			throws IllegalArgumentException;

	/**
	 * Returns the web space with the given identifier.
	 * 
	 * @param identifier
	 *            The identifier of the web space to return.
	 * @return The web space with the given identifier.
	 * @throws NoSuchElementException
	 *             Is thrown if there is no web space with the given identifier.
	 */
	public WebSpace getWebSpace(String identifier)
			throws NoSuchElementException;
	
	/**
	 * Stores an incoming message for the given web space.
	 * @param webSpace The receiving web space.
	 * @param content The encrypted content of the incoming message.
	 * @return The stored message.
	 */
	public Message storeMessage(WebSpace webSpace, byte[] content);

}
