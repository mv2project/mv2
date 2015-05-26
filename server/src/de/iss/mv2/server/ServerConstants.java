package de.iss.mv2.server;

import de.iss.mv2.MV2Constants;

/**
 * Defines constants used by the MV2Server.
 * @author Marcel Singer
 *
 */
public interface ServerConstants extends MV2Constants {

	/**
	 * The name of the bindings configuration file.
	 */
	public static final String BINDINGS_CONFIGURATION_FILE_NAME = "bindings.config";
	
	/**
	 * The name of the server configuration file.
	 */
	public static final String SERVER_CONFIGURATION_FILE_NAME = "server.config";
	
	/**
	 * The command line option to create an example configuration.
	 */
	public static final String CREATE_EXAMPLE_CONFIGURATION_OPTION = "c";
	
	/**
	 * The command line option to change the encryption of a key file.
	 */
	public static final String RECRYPT_KEY_FILE_OPTION = "rck";
	
	/**
	 * The command line option to retrieve the passphrase of the private keys.
	 */
	public static final String KEY_PASSPHRASE_OPTION = "k";
	
	/**
	 * The name of the current MV2 server implementation.
	 */
	public static final String MV2_SERVER_IMPLEMENTATION_NAME = "MV2 Server";
	
	/**
	 * The version of the current MV2 server implementation.
	 */
	public static final String MV2_SERVER_IMPLEMENTATION_VERSION = "0.2 Beta and Debug";
	
	/**
	 * The command line option to unlimit the key strength limitation.
	 */
	public static final String UNLIMIT_KEY_STRENGTH_OPTION = "u";
	
	
}
