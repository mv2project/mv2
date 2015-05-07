package de.iss.mv2.tests;

/**
 * A interface declaring the constants used for testing purposes.
 * @author MARCEL
 *
 */
public interface TestConstants {

	/**
	 * A constant defining the name of the signers certificate resource.
	 */
	public static final String DEBUG_CERT_RSC_NAME = "debug.cert.der";
	/**
	 * A constant defining the name of the signers key resource.
	 */
	public static final String DEBUG_KEY_RSC_NAME = "debug.key.der";
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
	 */
	public static final String CA_CERT_PASSPHRASE = "test123";
	
}
