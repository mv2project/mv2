package de.iss.mv2.client.data;

import de.iss.mv2.data.PropertiesExportable;

/**
 * A store for default client settings.
 * 
 * @author Marcel Singer
 *
 */
public class MV2ClientSettings extends PropertiesExportable {

	/**
	 * A constant defining the property key for the client program version.
	 */
	private static final String CLIENT_VERSION = "Version";

	/**
	 * Holds the current runtime settings.
	 */
	private static MV2ClientSettings current = null;

	/**
	 * Creates a new instance of {@link MV2ClientSettings}.
	 */
	public MV2ClientSettings() {

	}

	/**
	 * Creates an empty client configuration file.
	 * 
	 * @return The created configuration.
	 */
	public static MV2ClientSettings createFresh() {
		MV2ClientSettings settings = new MV2ClientSettings();
		settings.setVersion("1.0");
		return settings;
	}

	/**
	 * Sets the client version.
	 * 
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(String version) {
		getProperties().put(CLIENT_VERSION, version);
	}

	/**
	 * Returns the version of the client program that created this file.
	 * 
	 * @return The version of the client program that created this file.
	 */
	public String getVersion() {
		return getProperties().getProperty(CLIENT_VERSION);
	}

	/**
	 * Checks if all needed settings are done.
	 * 
	 * @return {@code true} if all needed settings are present.
	 */
	public boolean isValid() {
		if (getVersion() == null)
			return false;

		return true;
	}

	/**
	 * Sets the settings to be used for this execution.
	 * 
	 * @param settings
	 *            The settings to be set.
	 */
	public static synchronized void setRuntimeSettings(
			MV2ClientSettings settings) {
		MV2ClientSettings.current = settings;
	}

	/**
	 * Returns the settings to be used for this execution.
	 * @return The settings to be used for this execution.
	 * @throws IllegalStateException Is thrown if no settings were set previously.
	 */
	public static synchronized MV2ClientSettings getRuntimeSettings()
			throws IllegalStateException {
		if(current == null) throw new IllegalStateException("The session was not set.");
		return current;
	}

}
