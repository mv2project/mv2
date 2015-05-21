package de.iss.mv2.client;

import java.security.Security;

import javax.swing.UIManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.iss.mv2.io.CommandLineInterpreter;
import de.iss.mv2.security.KeyStrengthUmlimiter;

/**
 * The main entry class of the MV2Client.
 * @author Marcel Singer
 *
 */
@SuppressWarnings("deprecation")
public class MV2 {

/**
 * The main entry point for the application.
 * @param args The console arguments.
 */
	public static void main(String[] args){
		try {
			CommandLineInterpreter cli = new CommandLineInterpreter(args, false);
			if(cli.hasOption(ClientConstants.UNLIMITED_KEY_STRENGTH_OPTION)){
				KeyStrengthUmlimiter.removeCryptographyRestrictions();
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {

		}
		Security.addProvider(new BouncyCastleProvider());
		ClientStatrtup.start(args);
	}
	
	
}
