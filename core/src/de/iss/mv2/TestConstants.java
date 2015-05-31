package de.iss.mv2;

/**
 * A interface declaring the constants used for testing purposes.
 * @author MARCEL
 *
 */
public interface TestConstants {

	/**
	 * A constant defining the name of the signers certificate resource.
	 */
	public static final String DEBUG_CERT_RSC_NAME = MV2Constants.LOCALHOST_CERT_RSC_NAME;
	/**
	 * A constant defining the name of the signers key resource.
	 */
	public static final String DEBUG_KEY_RSC_NAME = MV2Constants.LOCALHOST_KEY_RSC_NAME;
	/**
	 * A constant defining the size of the client certificate to create.
	 */
	public static final int CLIENT_KEY_SIZE = 1024;

	/**
	 * A constant defining the name of the CA key resource.
	 */
	public static final String CA_CERT_RSC_NAME = "ca.cert.pem";

	/**
	 * A constant holding the passphrase to decrypt the CA's private key file.
	 * @deprecated An old debug constant. The same as {@link TestConstants#DEBUG_KEY_PASSPHRASE}
	 */
	@Deprecated
	public static final String CA_CERT_PASSPHRASE = "test123";
	
	/**
	 * A constant holding the passphrase to decrypt the private key connected to the debug certificate.
	 */
	public static final String DEBUG_KEY_PASSPHRASE = MV2Constants.LOCALHOST_KEY_PASSWORD;
	
	/**
	 * A string containing random characters.
	 */
	public static final String SOME_STRING = "fjklkdgjolajgjiopuä9ru 92u9)§891";
	
	/**
	 * The host of the database intended to use for testing purposes.
	 */
	public static final String TEST_HOST = "shome1.selfhost.eu";
	
	/**
	 * Test user to connect to the database sever intended for testing purposes.
	 */
	public static final String TEST_USER = "mv2user";
	/**
	 * Password of the test user.
	 */
	public static final String TEST_PW = "test123kss!";
	
	/**
	 * The port of the test host.
	 */
	public static final int TEST_PORT = 5432;

	
}
