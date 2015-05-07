package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCSException;
import org.junit.Test;
import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.security.RSAOutputStream;

/**
 * Test case for testing PEMFileIO.
 * 
 * @author Marcel Singer
 *
 */
public class TestPEMFileIO {

	/**
	 * The name of the certificate to use for testing.
	 */
	private static final String TEST_CERTIFICATE_RSC = "ca.cert.pem";

	/**
	 * Tests the write and read functionality for {@link X509Certificate}s.
	 * 
	 * @throws Exception
	 *             if something goes wrong.
	 */
	@Test
	public void testCertificateReadWrite() throws Exception {
		InputStream in = getClass().getClassLoader().getResourceAsStream(
				TEST_CERTIFICATE_RSC);
		assertNotNull(in);
		PEMFileIO pem = new PEMFileIO();
		X509Certificate cert = pem.readCertificate(in);
		in.close();
		assertNotNull("The read certificate was null.", cert);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pem.writeCertificate(baos, cert);
		ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
		X509Certificate cert2 = pem.readCertificate(bin);
		assertEquals(cert2, cert);

	}
	
	/**
	 * Tests the write and read functionality for private keys.
	 * @throws Exception If something goes wrong.
	 */
	@Test
	public void testKeyReadWrite() throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		KeyPair kp = RSAOutputStream.getRandomRSAKey(1024);
		assertNotNull(kp);
		PEMFileIO pemIO = new PEMFileIO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pemIO.writePKCS8EncryptedPrivateKey(baos, kp.getPrivate(), "fooBarBar");
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		PrivateKey pk = pemIO.readEncryptedPrivateKey(bais, "fooBarBar");
		assertEquals(kp.getPrivate(), pk);
	}

	/**
	 * Tests the behavior if an invalid passphrase is given.
	 * @throws Exception If something goes wrong.
	 */
	@Test(expected=PKCSException.class)
	public void testKeyReadWriteIllegal() throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		KeyPair kp = RSAOutputStream.getRandomRSAKey(1024);
		assertNotNull(kp);
		PEMFileIO pemIO = new PEMFileIO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pemIO.writePKCS8EncryptedPrivateKey(baos, kp.getPrivate(), "fooBarBar");
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		PrivateKey pk = pemIO.readEncryptedPrivateKey(bais, "BarFooBar");
		assertEquals(kp.getPrivate(), pk);
	}
	

}
