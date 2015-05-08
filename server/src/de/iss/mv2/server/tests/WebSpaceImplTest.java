package de.iss.mv2.server.tests;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.cert.X509Certificate;

import org.junit.Before;
import org.junit.Test;

import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.server.data.CertificateImpl;
import de.iss.mv2.server.data.WebSpaceImpl;
import de.iss.mv2.tests.TestConstants;

/**
 * A test for {@link WebSpaceImpl}.
 * @author Marcel Singer
 *
 */
public class WebSpaceImplTest {

	/**
	 * Holds the test certificate.
	 */
	private X509Certificate cert;
	
	/**
	 * Sets up this test.
	 * @throws Exception If anything goes wrong.
	 */
	@Before
	public void setup() throws Exception{
		InputStream in = getClass().getClassLoader().getResourceAsStream(TestConstants.CA_CERT_RSC_NAME);
		PEMFileIO pemReader = new PEMFileIO();
		cert = pemReader.readCertificate(in);
		in.close();
		assertNotNull(cert);
	}
	
	
	/**
	 * Tests the behavior when setting a {@code null} certificate.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetNullCert() {
		new WebSpaceImpl("identifier", null);
	}
	
	/**
	 * Tests the behavior when setting a {@code null} identifier.
	 * @throws Exception If anything goes wrong.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetNullIdentifier() throws Exception{
		CertificateImpl ci = new CertificateImpl(cert);
		new WebSpaceImpl(null, ci);
	}
	
	/**
	 * Tests the behavior when setting an empty identifier.
	 * @throws Exception If anything goes wrong.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetEmptyIdentifier() throws Exception{
		CertificateImpl ci = new CertificateImpl(cert);
		new WebSpaceImpl("", ci);
	}
	
	
	

}
