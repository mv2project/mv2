package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import sun.security.provider.SecureRandom;
import de.iss.mv2.TestConstants;
import de.iss.mv2.messaging.ContentMessage;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MessageField;
import de.iss.mv2.security.PEMFileIO;

/**
 * A test case for the {@link ContentMessage} class.
 * @author Marcel Singer
 *
 */
public class ContentMessageTest implements TestConstants {

	/**
	 * Holds the certificate of the signing instance.
	 */
	private X509Certificate signCert;
	/**
	 * Holds the private key of the signing instance.
	 */
	private PrivateKey signKey;
	
	/**
	 * The message to test.
	 */
	private ContentMessage message = new ContentMessage();
	
	/**
	 * Prepares the fields used by this test.
	 * @throws Exception If anything goes wrong.
	 */
	@Before
	public void prepare() throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		PEMFileIO pemIO = new PEMFileIO();
		InputStream in = getClass().getClassLoader().getResourceAsStream(DEBUG_CERT_RSC_NAME);
		signCert = pemIO.readCertificate(in);
		in.close();
		assertNotNull(signCert);
		in = getClass().getClassLoader().getResourceAsStream(DEBUG_KEY_RSC_NAME);
		signKey = pemIO.readEncryptedPrivateKey(in, DEBUG_KEY_PASSPHRASE);
		in.close();
		assertNotNull(signKey);
		
		message.setCarbonCopyAddresses(new String[]{"mustermann@max.com", "angela@example.com"});
		message.setContent("This is a test.");
		message.setReceivers(new String[]{"max@mus.org", "this.is@a.test"});
		message.setSubject("JUnit Test");
		message.setSender(signCert);
	}
	
	/**
	 * Tests the behavior when setting a valid certificate.
	 * @throws Exception If anything goes wrong.
	 */
	@Test
	public void testGetSetCerificate() throws Exception{
		message.setSender(signCert);
		assertEquals(signCert, message.getSender());
	}
	
	/**
	 * Test the behavior when applying a valid signature.
	 * @throws Exception
	 */
	@Test
	public void testValidSignature() throws Exception{
		message.sign(signKey);
		assertTrue(message.verifySignature());
	}
	
	/**
	 * Tests the behavior when applying a valid signature but modifying the data afterwards.
	 * @throws Exception
	 */
	@Test
	public void testManipulatedSignature() throws Exception{
		message.sign(signKey);
		message.setContent("Manipulated content!");
		assertFalse(message.verifySignature());
	}
	
	/**
	 * Tests the behavior when applying a valid signature but adding more fields afterwards.
	 * @throws Exception
	 */
	@Test
	public void testAddedSignature() throws Exception{
		message.sign(signKey);
		message.setMessageField(new MessageField(DEF_MESSAGE_FIELD.CAUSE, "Added Field"), true);
		assertFalse(message.verifySignature());
	}
	
	/**
	 * Tests the behavior when applying an invalid signature.
	 * @throws Exception
	 */
	@Test
	public void testIvalidSignature() throws Exception{
		byte[] testSig = new byte[256];
		new SecureRandom().engineNextBytes(testSig);
		message.setMessageField(new MessageField(DEF_MESSAGE_FIELD.SIGNATURE, testSig), true);
		assertFalse(message.verifySignature());
	}

}
