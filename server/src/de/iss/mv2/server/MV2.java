package de.iss.mv2.server;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.iss.mv2.security.AESWithRSACryptoSettings;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.server.io.MV2Server;

/**
 * The main class.
 * 
 * @author Marcel Singer
 * 
 */
public class MV2 {

	/**
	 * The main entry point for this application.
	 * 
	 * @param args
	 *            The command line arguments.
	 */

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print(System.getProperty("java.runtime.name") + " - ");
		System.out.println(System.getProperty("java.runtime.version"));
		try {

			Security.addProvider(new BouncyCastleProvider());

			PEMFileIO pemIO = new PEMFileIO();
			InputStream in;

			MessageCryptorSettings mcs = new AESWithRSACryptoSettings();

			in = mcs.getClass().getClassLoader()
					.getResourceAsStream("localhost.cert.der");
			X509Certificate serverCert = pemIO.readCertificate(in);
			in.close();
			String serverKeyPW = "test123";
			in = mcs.getClass().getClassLoader()
					.getResourceAsStream("localhost.key.der");
			PrivateKey serverKey = pemIO.readEncryptedPrivateKey(in,
					serverKeyPW);
			in.close();

			ServerBindings sb = new ServerBindings();
			sb.addBinding(new ServerBinding("localhost", serverCert, serverKey));
			in = mcs.getClass().getClassLoader()
					.getResourceAsStream("imac.fritz.box.cert.der");
			serverCert = pemIO.readCertificate(in);
			in.close();
			in = mcs.getClass().getClassLoader()
					.getResourceAsStream("imac.fritz.box.key.der");
			serverKey = pemIO.readEncryptedPrivateKey(in, serverKeyPW);
			in.close();
			serverKeyPW = null;
			sb.addBinding(new ServerBinding("imac.fritz.box", serverCert,
					serverKey));
			MV2Server server = new MV2Server(sb, mcs, 9898);
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

}
