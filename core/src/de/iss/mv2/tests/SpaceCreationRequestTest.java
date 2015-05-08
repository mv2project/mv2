package de.iss.mv2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.KeyPair;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.Test;

import de.iss.mv2.messaging.SpaceCreationRequest;
import de.iss.mv2.security.CertificateSigningRequest;
import de.iss.mv2.security.RSAOutputStream;

/**
 * A test for the {@link SpaceCreationRequest}-class.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceCreationRequestTest implements TestConstants {

	/**
	 * Tests the behavior when setting an {@code null} request data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullRequestData() {
		byte[] data = null;
		new SpaceCreationRequest().setSigningRequest(data);
	}

	/**
	 * Tests the behavior when setting an empty request.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmptyRequestData() {
		new SpaceCreationRequest().setSigningRequest(new byte[0]);
	}

	/**
	 * Tests the behavior when setting an {@code null} request.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetNullRequest() throws Exception {
		PKCS10CertificationRequest req = null;
		new SpaceCreationRequest().setSigningRequest(req);
	}

	/**
	 * Tests to set and get a {@link PKCS10CertificationRequest}.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void testGetSetRequest() throws Exception {
		CertificateSigningRequest csrBuilder = new CertificateSigningRequest();
		csrBuilder.init("Max Mustermann", "DE", "Baden-Wuerttemberg",
				"Musterstadt", "Muster GmbH", "Muster GmbH");
		KeyPair kp = RSAOutputStream
				.getRandomRSAKey(TestConstants.CLIENT_KEY_SIZE);
		assertNotNull(kp);
		PKCS10CertificationRequest csr = csrBuilder.generatePKCS10(kp);
		assertNotNull(csr);
		SpaceCreationRequest creationRequest = new SpaceCreationRequest();
		creationRequest.setSigningRequest(csr);
		PKCS10CertificationRequest csr2 = creationRequest.getSigningRequest();
		assertEquals(csr, csr2);
	}

}
