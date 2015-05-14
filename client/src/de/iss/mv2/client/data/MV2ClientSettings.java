package de.iss.mv2.client.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.iss.mv2.data.EncryptedExportable;
import de.iss.mv2.data.PropertiesExportable;
import de.iss.mv2.io.PathBuilder;

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
	 * Holds the mail boxes.
	 */
	private List<MailBoxSettings> mailBoxes = new ArrayList<MailBoxSettings>();

	/**
	 * The users passphrase.
	 */
	private String passphrase;
	
	/**
	 * Holds the trusted client certificates.
	 */
	private CertificateStore trustedClientCertificates = new CertificateStore();

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
	 * 
	 * @return The settings to be used for this execution.
	 * @throws IllegalStateException
	 *             Is thrown if no settings were set previously.
	 */
	public static synchronized MV2ClientSettings getRuntimeSettings()
			throws IllegalStateException {
		if (current == null)
			throw new IllegalStateException("The session was not set.");
		return current;
	}

	/**
	 * Returns the passphrase of the user.
	 * 
	 * @return The passphrase of the user.
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * Sets the passphrase of the user.
	 * 
	 * @param passphrase
	 *            The passphrase to set.
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * Adds the mail box.
	 * 
	 * @param mailBox
	 *            The mail box to add.
	 */
	public void addMailBox(MailBoxSettings mailBox) {
		mailBoxes.add(mailBox);
	}

	@Override
	protected void exportContent(OutputStream out) throws IOException {
		getProperties().setProperty("BOXES_COUNT", "" + mailBoxes.size());
		for (int i = 0; i < mailBoxes.size(); i++) {
			getProperties().setProperty("BOX_" + i,
					mailBoxes.get(i).getAddress().replace("@", "at"));
		}
		super.exportContent(out);
	}

	/**
	 * Returns the mail boxes.
	 * 
	 * @return The mail boxes of this user.
	 */
	public List<MailBoxSettings> getMailBoxes() {
		return mailBoxes;
	}
	
	/**
	 * Returns the store containing the trusted client certificates.
	 * @return The store containing the trusted client certificates.
	 */
	public CertificateStore getTrustedClientCertificates(){
		return trustedClientCertificates;
	}
	
	/**
	 * Returns the mail boxes.
	 * @return An array containing the available mail boxes.
	 */
	public MailBoxSettings[] getMailBoxesArray(){
		List<MailBoxSettings> list = getMailBoxes();
		MailBoxSettings[] boxes = new MailBoxSettings[list.size()];
		for(int i = 0; i<boxes.length; i++){
			boxes[i] = list.get(i);
		}
		return boxes;
	}

	/**
	 * Loads extra data.
	 * 
	 * @param f
	 *            The file that contains this settings.
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm to decrypt the data was not found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public void loadExtras(File f) throws IOException, NoSuchAlgorithmException {
		PathBuilder pb = new PathBuilder(f);
		EncryptedExportable ee;
		MailBoxSettings mb;
		File boxesFile;
		FileInputStream in;
		int boxesCount = Integer.parseInt(getProperties().getProperty(
				"BOXES_COUNT", "0"));
		for (int i = 0; i < boxesCount; i++) {
			boxesFile = pb
					.getChildFile(getProperties().getProperty("BOX_" + i));
			mb = new MailBoxSettings();
			ee = new EncryptedExportable(mb);
			in = new FileInputStream(boxesFile);
			ee.importData(getPassphrase(), in);
			in.close();
			addMailBox(mb);
		}
		
		boxesFile = pb.getChildFile(".clientcerts");
		if(boxesFile.exists()){
			trustedClientCertificates = new CertificateStore();
			ee = new EncryptedExportable(trustedClientCertificates);
			in = new FileInputStream(boxesFile);
			ee.importData(getPassphrase(), in);
			in.close();
		}
	}

	/**
	 * Saves extra data.
	 * 
	 * @param f
	 *            The file that contains this settings.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm to encrypt the data was not found.
	 */
	public void saveExtras(File f) throws IOException, NoSuchAlgorithmException {
		PathBuilder pb = new PathBuilder(f);
		EncryptedExportable ee;
		File boxFile;
		FileOutputStream fos;
		for (MailBoxSettings mb : mailBoxes) {
			boxFile = pb.getChildFile(mb.getAddress().replace("@", "at"));
			ee = new EncryptedExportable(mb);
			fos = new FileOutputStream(boxFile);
			ee.export(getPassphrase(), fos);
			fos.flush();
			fos.close();
		}
		boxFile = pb.getChildFile(".clientcerts");
		fos = new FileOutputStream(boxFile);
		ee = new EncryptedExportable(trustedClientCertificates);
		ee.export(getPassphrase(), fos);
		fos.flush();
		fos.close();
	}
	
	

}
