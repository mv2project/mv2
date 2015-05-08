package de.iss.mv2.client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import de.iss.mv2.MV2Constants;
import net.miginfocom.swing.MigLayout;

/**
 * A dialog to enter a passphrase.
 * 
 * @author Marcel Singer
 *
 */
public class PassphraseDialog extends JDialog implements ActionListener,
		MV2Constants {

	/**
	 * The current dialog result.
	 */
	private int dialogResult = CANCEL_OPTION;

	/**
	 * Creates a new {@link PassphraseDialog}.
	 * 
	 * @param titleLabel
	 *            The string to display inside the dialog as a caption.
	 * @param isModal
	 *            {@code true} if the dialog should be modal.
	 */
	public PassphraseDialog(String titleLabel, boolean isModal) {
		super(new JFrame(), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(
				new MigLayout("", "[][][grow][][]", "[][][]"));
		try {
			ImagePanel btnNewButton = new ImagePanel();
			InputStream in = getClass().getClassLoader().getResourceAsStream(
					"Key.png");
			btnNewButton.setImage(ImageIO.read(in));
			in.close();
			btnNewButton.setPreferredSize(new Dimension(80, 80));
			getContentPane().add(btnNewButton, "cell 0 0 1 2,grow");
		} catch (Exception e) {

		}
		this.titleLabel = new JLabel(titleLabel);
		this.titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		getContentPane().add(this.titleLabel, "cell 1 0 4 1");

		JLabel lblPassphrase = new JLabel("Passphrase:");
		getContentPane().add(lblPassphrase, "cell 1 1,alignx trailing");

		passwordField = new JPasswordField();
		getContentPane().add(passwordField, "cell 2 1 3 1,growx");

		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		getContentPane().add(submitButton, "cell 3 2");

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton, "cell 4 2");
		pack();
	}

	/**
	 * Creates a new {@link PassphraseDialog} with the default caption
	 * (<plain>Your passphrase is needed to perform this action</plain).
	 */
	public PassphraseDialog() {
		this("Your passphrase is needed to perform this action", true);
	}

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = 2322770371635102190L;
	/**
	 * The password field.
	 */
	private JPasswordField passwordField;
	/**
	 * The button that should cancel this dialog.
	 */
	private JButton cancelButton;
	/**
	 * The button that should submit this dialog.
	 */
	private JButton submitButton;
	/**
	 * The label displaying the caption.
	 */
	private JLabel titleLabel;

	/**
	 * Returns the entered password.
	 * 
	 * @return The entered password.
	 */
	public char[] getPassword() {
		return passwordField.getPassword();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton) {
			dialogResult = SUBMIT_OPTION;
			setVisible(false);
			return;
		}
		if (e.getSource() == cancelButton) {
			dialogResult = CANCEL_OPTION;
			setVisible(false);
			return;
		}
	}

	/**
	 * Sets the caption to display.
	 * 
	 * @param title
	 *            The caption to display.
	 */
	public void setTitleLabel(String title) {
		this.titleLabel.setText(title);
	}

	/**
	 * Returns the dialog result. See the constants
	 * {@link PassphraseDialog#SUBMIT_OPTION} and
	 * {@link PassphraseDialog#CANCEL_OPTION}.
	 * 
	 * @return The dialog result.
	 */
	public int getDialogResult() {
		return dialogResult;
	}

}
