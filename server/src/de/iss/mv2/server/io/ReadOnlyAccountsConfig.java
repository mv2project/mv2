package de.iss.mv2.server.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A configuration containing the identifier of web spaces that are read only.
 * 
 * @author Marcel Singer
 *
 */
public class ReadOnlyAccountsConfig {

	/**
	 * Holds the identifiers of the read only web spaces.
	 */
	private final List<String> readOnlyIdentifiers = new ArrayList<String>();

	/**
	 * Creates a new instance of {@link ReadOnlyAccountsConfig}.
	 */
	public ReadOnlyAccountsConfig() {

	}

	/**
	 * Tests if the web space with the given identifier is read only.
	 * 
	 * @param identifier
	 *            The identifier of the web space to test.
	 * @return {@code true} if the web space with the given identifier is read
	 *         only.
	 */
	public boolean isReadOnly(String identifier) {
		for (String id : readOnlyIdentifiers) {
			if (id.equalsIgnoreCase(identifier))
				return true;
		}
		return false;
	}

	/**
	 * Loads the content of the given stream and closes it.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public void load(InputStream in) throws IOException {
		Scanner sc = new Scanner(new InputStreamReader(in,
				StandardCharsets.UTF_8));
		String line;
		while (sc.hasNextLine()) {
			line = sc.nextLine().trim();
			if (line == null || line.startsWith("#") || line.isEmpty()
					|| !line.contains("@"))
				continue;
			readOnlyIdentifiers.add(line);
		}
		sc.close();
	}

	/**
	 * Loads the content of the given file. If the file does not exist this
	 * method will do nothing.
	 * 
	 * @param f
	 *            The file to read.
	 * @return {@code true} if the file was read. {@code false} if the file
	 *         didn't exist.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public boolean loadOrIrgnore(File f) throws IOException {
		if (!f.exists())
			return false;
		FileInputStream fin = new FileInputStream(f);
		try {
			load(fin);
		} finally {
			fin.close();
		}
		return true;
	}

}
