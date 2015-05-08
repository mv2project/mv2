package de.iss.mv2.client;

import java.security.Security;

import javax.swing.UIManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * The main entry class of the MV2Client.
 * @author Marcel Singer
 *
 */
public class MV2 {

/**
 * The main entry point for the application.
 * @param args The console arguments.
 */
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {

		}
		Security.addProvider(new BouncyCastleProvider());
		ClientStatrtup.start(args);
	}
	
	
}
