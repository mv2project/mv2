package de.iss.mv2.server;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import de.iss.mv2.data.BinaryTools;
import de.iss.mv2.extensions.ExtensionLoader;
import de.iss.mv2.io.CommandLineInterpreter;
import de.iss.mv2.io.PathBuilder;
import de.iss.mv2.io.VirtualConsoleReader;
import de.iss.mv2.security.AESWithRSACryptoSettings;
import de.iss.mv2.security.KeyStrengthUmlimiter;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.server.data.DatabaseContext;
import de.iss.mv2.server.io.ConfigFileLocator;
import de.iss.mv2.server.io.MV2Server;
import de.iss.mv2.server.io.ServerBindingsConfiguration;
import de.iss.mv2.server.io.ServerConfig;
import de.iss.mv2.server.sql.DatabaseInstaller;
import de.iss.mv2.server.sql.DatabaseSupportManager;
import de.iss.mv2.server.sql.DatabaseSupportProvider;
import de.iss.mv2.server.sql.DriverAdapter;

/**
 * The main class.
 * 
 * @author Marcel Singer
 * 
 */
@SuppressWarnings("deprecation")
public class MV2 {

	/**
	 * The main entry point for this application.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			loadExtensions();
			CommandLineInterpreter cli = new CommandLineInterpreter(args, false);
			if (cli.hasOption(ServerConstants.UNLIMIT_KEY_STRENGTH_OPTION)) {
				try {
					KeyStrengthUmlimiter.removeCryptographyRestrictions();
				} catch (Exception ex) {

				}
			}
			if(cli.hasOption(ServerConstants.CREATE_DB_OPTION)){
				createInitialDB(cli);
				return;
			}
			if (cli.hasOption(ServerConstants.CREATE_EXAMPLE_CONFIGURATION_OPTION)) {
				createBasicConfiguration(cli);
				return;
			}
			if (cli.hasOption(ServerConstants.RECRYPT_KEY_FILE_OPTION)) {
				recryptKey(cli);
				return;
			}
			startServer(cli);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the initial database structure.
	 * @param cli The command line interpreter.
	 */
	private static void createInitialDB(CommandLineInterpreter cli){
		List<String> extras = cli.getExtras(ServerConstants.CREATE_DB_OPTION);
		if(extras.size() < 1){
			System.err.println("The name of the database support provider is missing. Syntax is: -" + ServerConstants.CREATE_DB_OPTION + " <providerName>");
			return;
		}
		DatabaseSupportProvider provider = null;
		try{
			provider = DatabaseSupportManager.get(extras.get(0));
		}catch(NoSuchElementException ex){
			System.err.println("There is no support provider with the identifier '" + extras.get(0) + "'.");
			return;
		}
		try {
			String host = VirtualConsoleReader.readLine("Host: ");
			int port = Integer.parseInt(VirtualConsoleReader.readLine("Port: "));
			String database = VirtualConsoleReader.readLine("Database: ");
			String username = VirtualConsoleReader.readLine("Username: ");
			String password = VirtualConsoleReader.readPassword(System.in, System.out, "Password: ");
			DatabaseInstaller installer = new DatabaseInstaller(provider, host, port, database, username, password);
			installer.setOutputLogg(new PrintWriter(System.out));
			installer.install();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Loads extensions.
	 */
	private static void loadExtensions() {
		PathBuilder pb;
		try {
			pb = new PathBuilder(ConfigFileLocator.getConfigFileLocation());
		} catch (FileNotFoundException | IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		File extensionsDir = pb.getChildFile("ext");
		if (!extensionsDir.exists() || !extensionsDir.isDirectory())
			return;
		for (File jarFile : extensionsDir.listFiles()) {
			if (!jarFile.getName().toLowerCase().endsWith(".jar"))
				continue;
			try {
				ExtensionLoader<Driver> loader = new ExtensionLoader<Driver>(
						Driver.class, jarFile);

				for (final Driver d : loader.getAllInstances()) {
					DriverManager.registerDriver(new DriverAdapter(d));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Changes the encryption passphrase of a key file.
	 * 
	 * @param cli
	 *            The command line interpreter.
	 */
	private static void recryptKey(CommandLineInterpreter cli) {
		List<String> extras = cli
				.getExtras(ServerConstants.RECRYPT_KEY_FILE_OPTION);
		if (extras.size() == 0) {
			System.err
					.println("The path to the key file to recrypt is missing.");
			return;
		}
		File f = new File(extras.get(0));
		if (!f.exists()) {
			System.err.println("There is no key file at the given path: '"
					+ f.getAbsolutePath() + "'");
			return;
		}
		Console cons = System.console();
		String original = new String(cons.readPassword("%s",
				"Current passphrase:"));
		PEMFileIO pemIO = new PEMFileIO();
		PrivateKey key = null;
		InputStream in = null;
		try {
			in = new FileInputStream(f);
			key = pemIO.readEncryptedPrivateKey(in, original);
			if (key == null)
				throw new IOException();
		} catch (IOException | OperatorCreationException | PKCSException e) {
			e.printStackTrace();
			System.err
					.println("Could not read the given key file! Maybe the passphrase is invalid...");
			return;
		}
		String newPassphrase = new String(cons.readPassword("%s",
				"New passphrase:"));
		String newPassphraseRepeat = new String(cons.readPassword("%s",
				"New passphrase (repeat):"));
		if (!newPassphrase.equals(newPassphraseRepeat)) {
			System.err
					.println("The passphrase and its repitition do not match.");
			return;
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(f);
			pemIO.writePKCS8EncryptedPrivateKey(out, key, newPassphrase);
			out.close();
			out.flush();
		} catch (IOException | OperatorCreationException e) {
			System.err.println("Could not write the new key file!");
			return;
		}
		System.out.println("Completed!");
	}

	/**
	 * Creates a basic configuration to be adjusted by the user.
	 * 
	 * @param cli
	 *            The command line interpreter.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private static void createBasicConfiguration(CommandLineInterpreter cli)
			throws IOException {
		System.out.println("Creating basic configuration...");
		PathBuilder pb = new PathBuilder(
				ConfigFileLocator.getConfigFileLocation());
		File f = pb
				.getChildFile(ServerConstants.BINDINGS_CONFIGURATION_FILE_NAME);
		if (!f.exists()) {
			System.out
					.println("- BindingConfiguration (needs to be adjusted): "
							+ ServerConstants.BINDINGS_CONFIGURATION_FILE_NAME);
			f.createNewFile();
			ServerBindingsConfiguration sbc = ServerBindingsConfiguration
					.createExample();
			sbc.write(f);
			f = pb.getChildFile(ServerConstants.LOCALHOST_CERT_RSC_NAME);
			if (!f.exists()) {
				BinaryTools.copy(
						MV2.class.getClassLoader().getResourceAsStream(
								ServerConstants.LOCALHOST_CERT_RSC_NAME),
						new FileOutputStream(f), true, true);
			}
			f = pb.getChildFile(ServerConstants.LOCALHOST_KEY_RSC_NAME);
			if (!f.exists()) {
				BinaryTools.copy(
						MV2.class.getClassLoader().getResourceAsStream(
								ServerConstants.LOCALHOST_KEY_RSC_NAME),
						new FileOutputStream(f), true, true);
			}
			System.out
					.println("\t--> written to '" + f.getAbsolutePath() + "'");
		}
		f = pb.getChildFile(ServerConstants.SERVER_CONFIGURATION_FILE_NAME);
		if (!f.exists()) {
			System.out.println("- ServerConfiguration (needs to be adjusted): "
					+ ServerConstants.SERVER_CONFIGURATION_FILE_NAME);
			f.createNewFile();
			ServerConfig sc = ServerConfig.createExample();
			sc.store(f);
			System.out
					.println("\t--> written to '" + f.getAbsolutePath() + "'");
		}

	}

	/**
	 * Starts the server.
	 * 
	 * @param cli
	 *            The command line interpreter.
	 */
	private static void startServer(CommandLineInterpreter cli) {
		Scanner sc = new Scanner(System.in);
		System.out
				.print(ServerConstants.MV2_SERVER_IMPLEMENTATION_NAME + " - ");
		System.out.println(ServerConstants.MV2_SERVER_IMPLEMENTATION_VERSION);
		try {

			MessageCryptorSettings mcs = new AESWithRSACryptoSettings();
			ServerBindings bindings = null;
			PathBuilder pathBuilder = new PathBuilder(
					ConfigFileLocator.getConfigFileLocation());
			File serverConfigFile = pathBuilder
					.getChildFile(ServerConstants.SERVER_CONFIGURATION_FILE_NAME);
			if (!serverConfigFile.exists()) {
				System.err
						.println("The server configuration file was not found. Terminating the execution...");
				System.exit(0);
			}
			ServerConfig serverConfig = new ServerConfig();
			serverConfig.load(serverConfigFile);
			DatabaseContext.setContext(serverConfig.toDatabaseContext());
			File bindingsConfigFile = pathBuilder
					.getChildFile(ServerConstants.BINDINGS_CONFIGURATION_FILE_NAME);
			if (!bindingsConfigFile.exists()) {
				System.err
						.println("The configuration of the server bindings was not found --> using localhost.");
				bindings = getLocalBindings();
			} else {
				String pkPassphrase;
				ServerBindingsConfiguration bindingsConfig = new ServerBindingsConfiguration();
				bindingsConfig.read(bindingsConfigFile);
				if (!cli.hasOption(ServerConstants.KEY_PASSPHRASE_OPTION)
						|| cli.getExtras(ServerConstants.KEY_PASSPHRASE_OPTION)
								.size() == 0) {
					if (serverConfig.getPrivateKeyPassword() != null) {
						pkPassphrase = serverConfig.getPrivateKeyPassword();
					} else {
						System.err
								.println("The passphrase needed to decrypt the private keys was not supplied. Missing: -"
										+ ServerConstants.KEY_PASSPHRASE_OPTION
										+ " passphrase");
						sc.close();
						return;
					}
				} else {
					pkPassphrase = cli.getExtras(
							ServerConstants.KEY_PASSPHRASE_OPTION).get(0);
				}
				bindings = bindingsConfig.toServerBindings(pkPassphrase);
				pkPassphrase = null;
				System.gc();
			}

			MV2Server server = new MV2Server(serverConfig, bindings, mcs, 9898);
			server.start();

			String line;
			boolean canceled = false;
			while (!canceled) {
				System.out.print("> ");
				line = sc.nextLine();
				if (line.equalsIgnoreCase("exit")) {
					canceled = true;
					server.stopServer();
					System.out.println("Server is stopping!");
				}
			}
			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns the default local bindings.
	 * 
	 * @return The default local bindings.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	private static ServerBindings getLocalBindings() throws Exception {
		InputStream in = MV2.class.getClassLoader().getResourceAsStream(
				ServerConstants.LOCALHOST_CERT_RSC_NAME);
		PEMFileIO pemIO = new PEMFileIO();
		X509Certificate cert = pemIO.readCertificate(in);
		in.close();
		in = MV2.class.getClassLoader().getResourceAsStream(
				ServerConstants.LOCALHOST_KEY_RSC_NAME);
		PrivateKey key = pemIO.readEncryptedPrivateKey(in,
				ServerConstants.LOCALHOST_KEY_PASSWORD);
		in.close();
		ServerBindings bindings = new ServerBindings();
		bindings.addBinding(new ServerBinding("localhost", cert, key));
		return bindings;
	}

}
