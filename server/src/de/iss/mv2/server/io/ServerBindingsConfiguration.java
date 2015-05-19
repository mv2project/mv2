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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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
	private Map<String, String> map = new HashMap<String, String>();

	/**
	 * Creates a new instance of {@link ServerBindingsConfiguration}.
	 */
	public ServerBindingsConfiguration() {

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
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.startsWith("#"))
				continue;
			if (!line.contains("="))
				continue;
			args = line.split("=");
			if (args.length != 2)
				continue;
			map.put(args[0].trim(), args[1].trim());
		}
		scanner.close();
	}

	/**
	 * Reads the server bindings from the given input stream.
	 * 
	 * @param file The file to read.
	 * @throws FileNotFoundException If the given file was not found.
	 */
	public void read(File file) throws FileNotFoundException {
		read(new FileInputStream(file));
	}

	/**
	 * Writes the server bindings to the given input stream and closes it.
	 * @param out The output stream to write to.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(OutputStream out) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(out,
				StandardCharsets.UTF_8);
		for(String binding : map.keySet()){
			writer.write(binding + "=" + map.get(binding) + "\n");
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * Writes the server bindings to the given file.
	 * @param file The file to write to.
	 * @throws FileNotFoundException If the given file does not exist and can not be created.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(File file) throws FileNotFoundException, IOException {
		if(!file.exists()) file.createNewFile();
		write(new FileOutputStream(file));
	}
	
	/**
	 * Returns a set with the bindings in this configuration.
	 * @return A set with the bindings in this configuration.
	 */
	public Set<String> getBindings(){
		return map.keySet();
	}
	
	/**
	 * Returns the certificate path for the given binding.
	 * @param binding The binding thats certificate path should be returned.
	 * @return The certificate path for the given binding.
	 */
	public String getCertificatePath(String binding){
		return map.get(binding);
	}

	/**
	 * Adds a binding to this configuration.
	 * @param binding The binding to add.
	 * @param certificatePath The path to the connected certificate.
	 */
	public void addBinding(String binding, String certificatePath){
		map.put(binding, certificatePath);
	}
	
}
