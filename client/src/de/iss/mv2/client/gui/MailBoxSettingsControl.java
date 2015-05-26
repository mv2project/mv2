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
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import java.awt.FlowLayout;

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
	 * The field to edit the server port.
	 */
	private JTextField portField;
	/**
	 * The button to save the changes.
	 */
	private JButton saveButton;

	/**
	 * Creates a new instance of {@link MailBoxSettingsControl}.
	 * 
	 * @param mailBox
	 *            The mail box to edit.
	 */
	public MailBoxSettingsControl(MailBoxSettings mailBox) {
		this.mailBox = mailBox;
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel_2, BorderLayout.SOUTH);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		panel_2.add(saveButton);

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
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[][grow]", "[]"));
		
		JLabel lblPort = new JLabel("Port:");
		panel_1.add(lblPort, "cell 0 0,alignx trailing");
		
		portField = new JTextField();
		panel_1.add(portField, "cell 1 0,growx");
		portField.setColumns(10);
		portField.setText("" + mailBox.getServerPort());
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
		if(e.getSource() == saveButton){
			mailBox.setServerPort(Integer.parseInt(portField.getText()));
		}
	}
}
