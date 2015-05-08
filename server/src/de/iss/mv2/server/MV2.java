package de.iss.mv2.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.swing.UIManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import de.iss.mv2.client.ClientStatrtup;
import de.iss.mv2.data.EncryptedExportable;
import de.iss.mv2.data.LocalCertificateManager;
import de.iss.mv2.data.PropertiesExportable;
import de.iss.mv2.io.MV2Client;
import de.iss.mv2.io.VirtualConsoleReader;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageField;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.security.AESWithRSACryptoSettings;
import de.iss.mv2.security.CertificateSigner;
import de.iss.mv2.security.CertificateSigningRequest;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.security.RSAOutputStream;
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
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {

		}
		try {

			Security.addProvider(new BouncyCastleProvider());

			PEMFileIO pemIO = new PEMFileIO();
			InputStream in;

			System.out.println(new File("test.txt").getAbsolutePath());

			PropertiesExportable pe = new PropertiesExportable();
			pe.getProperties().setProperty("Foo", "Bar");
			EncryptedExportable ee = new EncryptedExportable(pe);
			ee.export("test123kss!", System.out);

			/**
			 * ClientMainWindow cmw = new ClientMainWindow();
			 * cmw.setVisible(true);
			 **/

			ClientStatrtup.start(args);

			MessageCryptorSettings mcs = new AESWithRSACryptoSettings();

			in = mcs.getClass().getClassLoader()
					.getResourceAsStream("localhost.cert.der");
			X509Certificate serverCert = pemIO.readCertificate(in);
			in.close();
			String serverKeyPW = "test123";// VirtualConsoleReader.readPassword(System.in,
											// System.out, "Key-File-PW: ");
			in = mcs.getClass().getClassLoader()
					.getResourceAsStream("localhost.key.der");
			PrivateKey serverKey = pemIO.readEncryptedPrivateKey(in,
					serverKeyPW);
			in.close();
			
			ServerBindings sb = new ServerBindings();
			sb.addBinding(new ServerBinding("localhost", serverCert, serverKey));
			in = mcs.getClass().getClassLoader().getResourceAsStream("imac.fritz.box.cert.der");
			serverCert = pemIO.readCertificate(in);
			in.close();
			in = mcs.getClass().getClassLoader().getResourceAsStream("imac.fritz.box.key.der");
			serverKey = pemIO.readEncryptedPrivateKey(in, serverKeyPW);
			in.close();
			serverKeyPW = null;
			sb.addBinding(new ServerBinding("imac.fritz.box", serverCert, serverKey));
			MV2Server server = new MV2Server(sb, mcs,
					9898);
			server.start();
			MV2Message m = new MV2Message(STD_MESSAGE.CERT_REQUEST);
			System.out.println("Sending: \n" + m);
			MV2Client client = new MV2Client();
			client.setCryptoSettings(mcs);
			client.connect("127.0.0.1", 9898);
			client.send(m);
			System.out.println(client.handleNext());
			m = new MV2Message(26);
			m.setMessageField(new MessageField(11, "Hello World!"), true);
			client.send(m);
			System.out.println("Response: " + client.receive());

			String line;
			boolean canceled = false;
			while (!canceled) {
				System.out.print("> ");
				line = sc.nextLine();
				if (line.equalsIgnoreCase("certassist")) {
					createDebugCert();
					continue;
				}
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
	 * Creates a debugging certificate.
	 * 
	 * @throws Exception
	 *             Is thrown if any exception occurs.
	 */
	private static void createDebugCert() throws Exception {
		String keyPath = VirtualConsoleReader.readLine("Private-Key-File: ");
		File f = new File(keyPath);
		if (!f.exists())
			f.createNewFile();
		String certPath = VirtualConsoleReader.readLine("Cert-File: ");
		f = new File(certPath);
		if (!f.exists())
			f.createNewFile();
		createDebugCert(keyPath, certPath);
	}

	/**
	 * Creates a debugging certificate.
	 * 
	 * @param keyPath
	 *            The path to store the created key.
	 * @param certPath
	 *            The path to store the created certificate.
	 * @throws Exception
	 *             Is thrown if any exception occurs.
	 */
	private static void createDebugCert(String keyPath, String certPath)
			throws Exception {
		PEMFileIO pemIO = new PEMFileIO();
		InputStream in;

		System.out.println("Generating client rsa-keys...");
		KeyPair debugKeyPair = RSAOutputStream.getRandomRSAKey(4096);
		String password = VirtualConsoleReader.readPassword(System.in,
				System.out, "Client-Key-Password: ");
		OutputStream out = new FileOutputStream(new File(keyPath));
		pemIO.writePKCS8EncryptedPrivateKey(out, debugKeyPair.getPrivate(),
				password);
		out.flush();
		out.close();
		String country = VirtualConsoleReader.readLine("Country [C]: ");
		String state = VirtualConsoleReader.readLine("State [S]: ");
		String city = VirtualConsoleReader.readLine("City [L]: ");
		String commonName = VirtualConsoleReader.readLine("Common Name [CN]: ");
		String organisation = VirtualConsoleReader
				.readLine("Organisation [O]: ");
		String organisationUnit = VirtualConsoleReader
				.readLine("Organisation Unit [OU]: ");

		System.out
				.println("\nGenerating client certificate-signing-request...");
		CertificateSigningRequest csr = new CertificateSigningRequest();
		csr.init(commonName, country, state, city, organisation,
				organisationUnit);
		PKCS10CertificationRequest req = csr.generatePKCS10(debugKeyPair);
		pemIO.writeCertificateSigningRequest(System.out, req);
		System.out.println();
		System.out.println();

		in = pemIO.getClass().getClassLoader()
				.getResourceAsStream("MV2CA.cert");
		X509Certificate caCert = pemIO.readCertificate(in);
		in.close();
		password = VirtualConsoleReader.readPassword(System.in, System.out,
				"CA-Password: ");
		in = pemIO.getClass().getClassLoader()
				.getResourceAsStream("ca.key2.pem");
		KeyPair caKeyPair = pemIO.readEncryptedKeyFile(in, password);
		password = null;
		in.close();
		boolean allowResign = VirtualConsoleReader
				.readBoolean("Allow resigning (yes/no): ");
		CertificateSigner signer = new CertificateSigner(caCert,
				new LocalCertificateManager(caCert), new SecureRandom());
		signer.setValidity(365);
		System.out.println("\nSigning certificate request...");
		X509Certificate issued = signer.sign(caKeyPair.getPrivate(), req,
				allowResign);
		pemIO.writeCertificate(System.out, issued);
		out = new FileOutputStream(new File(certPath));
		pemIO.writeCertificate(out, issued);
		out.flush();
		out.close();
		System.out.println("Certificate exported to '" + certPath + "'.");
	}

}
