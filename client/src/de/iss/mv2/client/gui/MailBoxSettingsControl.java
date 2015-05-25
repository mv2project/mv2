package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.iss.mv2.MV2Constants;
import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.messaging.KeyPutProcedure;
import de.iss.mv2.client.messaging.MessageProcedure;
import de.iss.mv2.client.messaging.ProcedureException;
import de.iss.mv2.client.messaging.ProcedureListener;
import de.iss.mv2.gui.DialogHelper;

/**
 * A control to manage the settings of a mail box.
 * 
 * @author Marcel Singer
 *
 */
public class MailBoxSettingsControl extends JComponent implements ActionListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -8578085904359298122L;
	/**
	 * Holds the mail box.
	 */
	private MailBoxSettings mailBox;
	/**
	 * The button to export the private key.
	 */
	private JButton exportKeyButton;

	/**
	 * Creates a new instance of {@link MailBoxSettingsControl}.
	 * 
	 * @param mailBox
	 *            The mail box to edit.
	 */
	public MailBoxSettingsControl(MailBoxSettings mailBox) {
		this.mailBox = mailBox;
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel mailBoxIdentifierLabel = new JLabel("New label");
		mailBoxIdentifierLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(mailBoxIdentifierLabel, BorderLayout.NORTH);

		exportKeyButton = new JButton("Export Key");
		panel.add(exportKeyButton, BorderLayout.EAST);
		
		mailBoxIdentifierLabel.setText(mailBox.getAddress());
		exportKeyButton.addActionListener(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exportKeyButton){
			char[] passphrase;
			char[] passphraseRepeat;
			boolean match;
			PassphraseDialog dialog = new PassphraseDialog("Enter a passphrase to be used to export this key:", true);
			dialog.setLocationRelativeTo(this);
			PassphraseDialog repeatDialog = new PassphraseDialog("Repeat the passphrase to be used to export this key:", true);
			repeatDialog.setLocationRelativeTo(this);
			do{
				dialog.setVisible(true);
				if(dialog.getDialogResult() != MV2Constants.SUBMIT_OPTION) return;
				passphrase = dialog.getPassword();
				repeatDialog.setVisible(true);
				if(repeatDialog.getDialogResult() != MV2Constants.SUBMIT_OPTION) return;
				passphraseRepeat = repeatDialog.getPassword();
				match = Arrays.equals(passphrase, passphraseRepeat);
				if(!match){
					DialogHelper.showErrorMessage(this, "Invalid Input", "The password and its repetition do not match. Try again...");
				}
			}while(!match);
			Arrays.fill(passphraseRepeat, ' ');
			LoadingDialog ld = new LoadingDialog(DialogHelper.getParentFrame(this), false);
			KeyPutProcedure procedure = new KeyPutProcedure(mailBox, passphrase);
			procedure.addProcedureListener(new ProcedureListener<Void>() {
				
				@Override
				public void procedureCompleted(
						MessageProcedure<? extends Throwable, Void> procedure, Void result) {
					
				}
				
				@Override
				public void handleProcedureException(
						MessageProcedure<? extends Throwable, Void> procedure,
						ProcedureException ex) {
					ex.printStackTrace();
					
				}
				
				@Override
				public void procedureStateChanged(
						MessageProcedure<? extends Throwable, Void> procedure,
						String state, int progress) {
					
				}
			});
			procedure.addProcedureListener(ld);
			ld.setVisible(true);
			procedure.run();
		}
	}
}
