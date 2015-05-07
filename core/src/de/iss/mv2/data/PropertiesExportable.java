package de.iss.mv2.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * A wrapper for the Java-Properties-Class.
 * @author Marcel Singer
 *
 */
public class PropertiesExportable extends Exportable {

	/**
	 * Holds the wrapped properties.
	 */
	private final Properties properties;

	/**
	 * Creates a new instance of {@link PropertiesExportable}.
	 */
	public PropertiesExportable(){
		properties = new Properties();
	}
	
	/**
	 * Returns the wrapped properties.
	 * @return The wrapped properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	@Override
	protected void exportContent(OutputStream out) throws IOException {
		properties.store(out, "");

	}

	@Override
	protected void importContent(InputStream in) throws IOException {
		properties.load(in);
	}

}
