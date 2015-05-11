package de.iss.mv2.client;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

//import javax.imageio.ImageIO;

//import com.apple.eawt.Application;

import de.iss.mv2.MV2Constants;
import de.iss.mv2.client.data.MV2ClientSettings;
import de.iss.mv2.client.data.UserPreferences;
import de.iss.mv2.client.gui.ClientMainWindow;
import de.iss.mv2.client.gui.PassphraseDialog;
import de.iss.mv2.client.gui.StoreLocationSelector;
import de.iss.mv2.data.EncryptedExportable;
import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.io.PathBuilder;

/**
 * A helper class performing the startup of the client application.
 * 
 * @author Marcel Singer
 *
 */
public class ClientStatrtup {

	/**
	 * Performs the startup.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void start(final String[] args) {
		try{
			//Application.getApplication().setDockIconImage(ImageIO.read(ClientStatrtup.class.getClassLoader().getResourceAsStream("mv2Icon.png")));
		}catch(Exception ex){
			
		}
		
		final UserPreferences up = UserPreferences.getPreferences();
		Toolkit tk = Toolkit.getDefaultToolkit();

		if (!up.hasStoreAddress()
				|| tk.getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
			final StoreLocationSelector sls = new StoreLocationSelector();
			sls.setVisible(true);
			sls.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (sls.getDialogResult() == MV2Constants.SUBMIT_OPTION) {
						up.setStoreAddress(sls.getSelectedDirectory()
								.getAbsolutePath());
						loadSettings(args);
					} else
						System.exit(0);
				}
			});
			return;
		}
		loadSettings(args);
	}

	/**
	 * Sets up the current client.
	 * 
	 * @return {@code true} if the setup was successful.
	 * @param configFile
	 *            Specifies the file path to store the configuration file.
	 */
	private static boolean setup(String configFile) {
		PassphraseDialog pd = new PassphraseDialog(
				"Select a passphrase to encrypt your data:", true);
		PassphraseDialog pdw = new PassphraseDialog(
				"Repeat your passphrase to encrypt your data (repeat):", true);
		String pw1;
		pd.setVisible(true);
		if (pd.getDialogResult() != MV2Constants.SUBMIT_OPTION)
			System.exit(0);
		pw1 = new String(pd.getPassword());
		pdw.setVisible(true);
		if (pdw.getDialogResult() != MV2Constants.SUBMIT_OPTION)
			System.exit(0);
		String pw2 = new String(pdw.getPassword());
		if (!pw1.equals(pw2)) {
			DialogHelper.showErrorMessage(null, "",
					"The provided passphrases did not match. Try again.");
			return setup(configFile);
		}
		MV2ClientSettings clientSettings = MV2ClientSettings.createFresh();
		EncryptedExportable ee = new EncryptedExportable(clientSettings);
		try {
			FileOutputStream fos = new FileOutputStream(new File(configFile));
			ee.export(pw1, fos);
			fos.flush();
			fos.close();
		} catch (Exception ex) {
			DialogHelper.showActionFailedWithExceptionMessage(null, ex);
			return false;
		}
		return true;
	}

	/**
	 * Loads the settings from the current configuration file.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	private static void loadSettings(String[] args) {
		final UserPreferences up = UserPreferences.getPreferences();
		File f = new File(up.getStoreAddress(null));
		PathBuilder pb = null;
		try {
			pb = new PathBuilder(f);
		} catch (FileNotFoundException e) {
			DialogHelper
					.showErrorMessage(
							null,
							"File not found",
							"The configuration file could not be found at the default location. Did it move?");
			up.setStoreAddress(null);
			start(args);
			return;
		}
		File prefFile = pb.getChildFile(MV2Constants.CLIENT_CONFIG_FILE_NAME);
		if (!prefFile.exists()) {
			if (!setup(prefFile.getAbsolutePath()))
				return;
		}
		MV2ClientSettings settings = new MV2ClientSettings();
		EncryptedExportable ee = new EncryptedExportable(settings);
		PassphraseDialog pd = new PassphraseDialog(
				"Your passphrase is required to access the data: ", true);
		pd.setVisible(true);
		if (pd.getDialogResult() != MV2Constants.SUBMIT_OPTION)
			System.exit(0);
		String pw = new String(pd.getPassword());
		try {
			FileInputStream fin = new FileInputStream(prefFile);
			ee.importData(pw, fin);
			if(settings.isValid()){
				settings.setPassphrase(pw);
				settings.loadExtras(prefFile);
			}
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			DialogHelper.showActionFailedWithExceptionMessage(null, e);
			loadSettings(args);
			return;
		}
		if(!settings.isValid()){
			DialogHelper.showErrorMessage(null, "Invalid Settings", "The read settings are invalid. Maybe the file is corrupt or the provided passphrase is incorrect. Try again...");
			loadSettings(args);
			return;
		}
		
		MV2ClientSettings.setRuntimeSettings(settings);
		ClientMainWindow cmw = new ClientMainWindow();
		cmw.setVisible(true);
	}

}
