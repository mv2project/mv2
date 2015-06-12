package de.iss.mv2.server;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.iss.mv2.MV2Constants;
import de.iss.mv2.server.io.ReadOnlyAccountsConfig;

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
	 * The name of the {@link ReadOnlyAccountsConfig}.
	 */
	public static final String READ_ONLY_CONFIGURATION_FILE_NAME = "readonly.config";
	
	/**
	 * The command line option to create an example configuration.
	 */
	public static final String CREATE_EXAMPLE_CONFIGURATION_OPTION = "c";
	
	/**
	 * The command line option to change the encryption of a key file.
	 */
	public static final String RECRYPT_KEY_FILE_OPTION = "rck";
	
	/**
	 * The command line option to create the initial database structure.
	 */
	public static final String CREATE_DB_OPTION = "cdb";
	
	/**
	 * The command line option to retrieve the passphrase of the private keys.
	 */
	public static final String KEY_PASSPHRASE_OPTION = "k";
	
	/**
	 * The command line option indicating that incoming messages should be printed to the console.
	 */
	public static final String SHOW_INCOMING_MESSAGES_OPTION = "vm";
	
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
	
	/**
	 * An option to define the path to the Bouncy Castle Provider file.
	 */
	public static final String BC_PATH_OPION = "bcp";
	
	/**
	 * The default name of the Bouncy Castle provider file.
	 */
	public static final String BC_PROV_JAR = "bcprov.jar";
	
	/**
	 * The name of the {@link BouncyCastleProvider}-class.
	 */
	public static final String BC_CLASS_NAME = "org.bouncycastle.jce.provider.BouncyCastleProvider";
	
	
	
}
