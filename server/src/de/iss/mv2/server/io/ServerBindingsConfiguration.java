package de.iss.mv2.server.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import de.iss.mv2.io.PathBuilder;
import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.server.ServerBinding;
import de.iss.mv2.server.ServerBindings;

/**
 * A configuration file for the bindings of a server.
 * 
 * @author Marcel Singer
 *
 */
public class ServerBindingsConfiguration {

	/**
	 * The map holding the certificate mapping.
	 */
	private Map<String, String[]> map = new HashMap<String, String[]>();

	/**
	 * Creates a new instance of {@link ServerBindingsConfiguration}.
	 */
	public ServerBindingsConfiguration() {

	}
	
	/**
	 * Returns an exemplary {@link ServerBindingsConfiguration}.
	 * @return An exemplary {@link ServerBindingsConfiguration}.
	 */
	public static ServerBindingsConfiguration createExample(){
		ServerBindingsConfiguration sbc = new ServerBindingsConfiguration();
		sbc.addBinding("binding1.com", "binding1.cert.der", "binding1.key.der");
		sbc.addBinding("binding2.org", "binding2.cert.der", "binding2.key.der");
		return sbc;
	}

	/**
	 * Tries to read all certificates and keys and returns the server bindings.
	 * @param passphrase The passphrase of the encrypted key files.
	 * @return The read bindings.
	 * @throws IOException If an I/O error occurs. Especially if the certificate or key file was not found.
	 * @throws CertificateException If the certificate can not be read.
	 * @throws PKCSException If the key can not be read or the supplied passphrase is incorrect.
	 * @throws OperatorCreationException If the operator is invalid.
	 */
	public ServerBindings toServerBindings(String passphrase) throws CertificateException, IOException, OperatorCreationException, PKCSException {
		File certFile;
		File keyFile;
		InputStream in;
		PEMFileIO pemIO = new PEMFileIO();
		X509Certificate cert;
		PrivateKey key;
		PathBuilder pb = new PathBuilder(ConfigFileLocator.getConfigFileLocation());
		ServerBindings serverBindings = new ServerBindings();
		for(String binding : getBindings()){
			certFile = pb.getChildFile(getCertificatePath(binding));
			keyFile = pb.getChildFile(getKeyPath(binding));
			in = new FileInputStream(certFile);
			cert = pemIO.readCertificate(in);
			in.close();
			in = new FileInputStream(keyFile);
			key = pemIO.readEncryptedPrivateKey(in, passphrase);
			in.close();
			serverBindings.addBinding(new ServerBinding(binding, cert, key));
		}
		return serverBindings;
	}

	/**
	 * Reads the server bindings from the given input stream.
	 * 
	 * @param in
	 *            The input stream to read.
	 */
	public void read(InputStream in) {
		Scanner scanner = new Scanner(in);
		String line;
		String args[];
		String args2[];
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.startsWith("#"))
				continue;
			if (!line.contains("="))
				continue;
			args = line.split("=");
			if (args.length != 2)
				continue;
			args2 = args[1].trim().split(";");
			if (args2.length != 2)
				continue;
			map.put(args[0].trim(), args2);
		}
		scanner.close();
	}

	/**
	 * Reads the server bindings from the given input stream.
	 * 
	 * @param file
	 *            The file to read.
	 * @throws FileNotFoundException
	 *             If the given file was not found.
	 */
	public void read(File file) throws FileNotFoundException {
		read(new FileInputStream(file));
	}

	/**
	 * Writes the server bindings to the given input stream and closes it.
	 * 
	 * @param out
	 *            The output stream to write to.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public void write(OutputStream out) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(out,
				StandardCharsets.UTF_8);
		String[] files;
		for (String binding : map.keySet()) {
			files = map.get(binding);
			writer.write(binding + "=" + files[0] + ";" + files[1] + "\n");
		}
		writer.flush();
		writer.close();
	}

	/**
	 * Writes the server bindings to the given file.
	 * 
	 * @param file
	 *            The file to write to.
	 * @throws FileNotFoundException
	 *             If the given file does not exist and can not be created.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public void write(File file) throws FileNotFoundException, IOException {
		if (!file.exists())
			file.createNewFile();
		write(new FileOutputStream(file));
	}

	/**
	 * Returns a set with the bindings in this configuration.
	 * 
	 * @return A set with the bindings in this configuration.
	 */
	public Set<String> getBindings() {
		return map.keySet();
	}

	/**
	 * Returns the certificate path for the given binding.
	 * 
	 * @param binding
	 *            The binding thats certificate path should be returned.
	 * @return The certificate path for the given binding.
	 */
	public String getCertificatePath(String binding) {
		return map.get(binding)[0].trim();
	}
	
	/**
	 * Returns the path to the key file for the given binding.
	 * @param binding The binding thats key file path should be returned.
	 * @return The path to the key file.
	 */
	public String getKeyPath(String binding){
		return map.get(binding)[1].trim();
	}

	/**
	 * Adds a binding to this configuration.
	 * 
	 * @param binding
	 *            The binding to add.
	 * @param certificatePath
	 *            The path to the connected certificate.
	 * @param keyPath
	 *            The path to the connected private key file.
	 */
	public void addBinding(String binding, String certificatePath,
			String keyPath) {
		map.put(binding, new String[] { certificatePath, keyPath });
	}

}
