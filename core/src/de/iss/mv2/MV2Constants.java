package de.iss.mv2;

/**
 * An interface holding the most common constants.
 * @author Marcel Singer
 *
 */
public interface MV2Constants {
	
	/**
	 * The default name of the client configuration file.
	 */
	public static final String CLIENT_CONFIG_FILE_NAME = ".mv2client";
	

	/**
	 * The dialog result if the user submits a dialog.
	 */
	public static final int SUBMIT_OPTION = 1;
	/**
	 * The dialog result if the user cancels a dialog.
	 */
	public static final int CANCEL_OPTION = -1;
	
	/**
	 * A constant defining the resource name of the localhost's certificate.
	 */
	public static final String LOCALHOST_CERT_RSC_NAME = "localhost.cert.der";
	
	/**
	 * A constant defining the resource name of the localhost's key file.
	 */
	public static final String LOCALHOST_KEY_RSC_NAME = "localhost.key.der";
	
	/**
	 * A constant defining the encryption password of the localhost's key file.
	 */
	public static final String LOCALHOST_KEY_PASSWORD = "test123";
}
