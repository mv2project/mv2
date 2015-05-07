package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;

import org.junit.Before;
import org.junit.Test;

import de.iss.mv2.client.data.CertificateStore;
import de.iss.mv2.data.EncryptedExportable;
import de.iss.mv2.security.PEMFileIO;

/**
 * A test for the {@link CertificateStore}.
 * @author MARCEL
 *
 */
public class CertificateStoreTest implements TestConstants {

	/**
	 * Test certificate #1.
	 */
	private X509Certificate caCert;
	/**
	 * Test certificate #2.
	 */
	private X509Certificate debugCert;
	
	
	
	/**
	 * Prepares this test.
	 * @throws Exception
	 */
	@Before
	public void prepare() throws Exception{
		PEMFileIO pemIO = new PEMFileIO();
		InputStream in = getClass().getClassLoader().getResourceAsStream(CA_CERT_RSC_NAME);
		caCert = pemIO.readCertificate(in);
		in.close();
		assertNotNull(caCert);
		in = getClass().getClassLoader().getResourceAsStream(DEBUG_CERT_RSC_NAME);
		debugCert = pemIO.readCertificate(in);
		in.close();
		assertNotNull(debugCert);
	}
	
	/**
	 * Tests the export and import of a {@link CertificateStore}.
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testImportExport() throws IOException{
		CertificateStore cs = new CertificateStore();
		cs.add(caCert);
		cs.add(debugCert);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		cs.export(baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		CertificateStore cs2 = new CertificateStore();
		cs2.importData(bais);
		assertEquals(cs, cs2);
	}
	
	/**
	 * Tests for an exception if {@code null} is added to the store.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetNull(){
		CertificateStore cs = new CertificateStore();
		cs.add(null);
	}
	
	/**
	 * Tests the behavior if one certificate is added twice.
	 */
	@Test
	public void testAddDouble(){
		CertificateStore cs = new CertificateStore();
		cs.add(caCert);
		assertFalse(cs.add(caCert));
		assertTrue(cs.size() == 1);
	}
	
	/**
	 * Tests the encrypted import an export.
	 * @throws Exception If something goes wrong.
	 */
	@Test
	public void testEncryptedImportExport() throws Exception{
		CertificateStore cs = new CertificateStore();
		cs.add(caCert);
		cs.add(debugCert);
		EncryptedExportable ee = new EncryptedExportable(cs);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ee.export(CA_CERT_PASSPHRASE, baos);
		CertificateStore cs2 = new CertificateStore();
		ee = new EncryptedExportable(cs2);
		ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray());
		ee.importData(CA_CERT_PASSPHRASE, bais);
		assertEquals(cs, cs2);
	}
}
