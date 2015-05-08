package de.iss.mv2.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.HashSet;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.Before;
import org.junit.Test;

import de.iss.mv2.data.CertificateManager;
import de.iss.mv2.security.CertificateSigner;
import de.iss.mv2.security.CertificateSigningRequest;
import de.iss.mv2.security.CertificateVerifier;
import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.security.RSAOutputStream;
import de.iss.mv2.server.data.CertificateManagerImpl;
import de.iss.mv2.server.data.DatabaseContext;

/**
 * A test for the certification process.
 * This test needs a JRE with unlimited key strength.
 * @author Marcel Singer
 *
 */
public class CertificateTest implements TestConstants {

	/**
	 * Holds the certificate of the signing instance.
	 */
	private X509Certificate signingCertificate;
	/**
	 * Holds the key pair of the signing instance.
	 */
	private KeyPair signersKey;
	/**
	 * Holds the certificate manager.
	 */
	private CertificateManager certManager;
	/**
	 * Holds the key pair of the client.
	 */
	private KeyPair clientKey;

	/**
	 * Holds the certificate of the CA.
	 */
	private X509Certificate caCert;

	/**
	 * Holds a set of trusted certificates.
	 */
	private HashSet<X509Certificate> trusted = new HashSet<X509Certificate>();
	/**
	 * Holds a pem writer.
	 */
	private PEMFileIO pemIO;
	

	/**
	 * Sets up this test.
	 * 
	 * @throws Exception
	 *             If an exception occurs.
	 */
	@Before
	public void setUp() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		pemIO = new PEMFileIO();
		InputStream in = pemIO.getClass().getClassLoader()
				.getResourceAsStream(DEBUG_CERT_RSC_NAME);

		signingCertificate = pemIO.readCertificate(in);
		in.close();
		in = pemIO.getClass().getClassLoader()
				.getResourceAsStream(CA_CERT_RSC_NAME);
		caCert = pemIO.readCertificate(in);
		in.close();
		in = pemIO.getClass().getClassLoader()
				.getResourceAsStream(DEBUG_KEY_RSC_NAME);
		PrivateKey pk = pemIO.readEncryptedPrivateKey(in, CA_CERT_PASSPHRASE);
		signersKey = new KeyPair(signingCertificate.getPublicKey(), pk);
		certManager = new CertificateManagerImpl(DatabaseContext.getContext());
		clientKey = RSAOutputStream.getRandomRSAKey(CLIENT_KEY_SIZE);
		trusted.add(signingCertificate);
		trusted.add(caCert);
	}

	/**
	 * Performs the test.
	 */
	@Test
	public void test() {
		assertNotNull(signingCertificate);
		assertNotNull(caCert);
		assertNotNull(signersKey);
		assertNotNull(certManager);

		CertificateSigningRequest csr = new CertificateSigningRequest();
		csr.init("Max Mustermann", "DE", "Baden-Württemberg", "Heilbronn",
				"Individual Software Solutions - ISS", "Test Case");
		assertTrue(csr.getState().equals("Baden-Wuerttemberg"));
		PKCS10CertificationRequest request = null;
		try {
			request = csr.generatePKCS10(clientKey);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		CertificateSigner signer = new CertificateSigner(signingCertificate,
				certManager, new SecureRandom());
		assertNotNull(request);
		X509Certificate clientCert = null;
		try {
			clientCert = signer.sign(signersKey.getPrivate(), request, false);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		assertNotNull(clientCert);

		try {
			CertificateVerifier.verifyCertificate(clientCert, trusted);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		certManager.remove(clientCert, false);
		return;
	}

}
