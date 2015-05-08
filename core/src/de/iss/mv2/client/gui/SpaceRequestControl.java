package de.iss.mv2.client.gui;

import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * A control to request a new web space.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceRequestControl extends JComponent {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -8982651936101267677L;

	/**
	 * The text field containing the web sapce identifier.
	 */
	private JTextField identifierName;

	/**
	 * A control to select the domain.
	 */
	private JComboBox domainSelector;

	/**
	 * Creates a new {@link SpaceRequestControl}.
	 */
	public SpaceRequestControl() {
		setLayout(new MigLayout("", "[][166.00,grow][98.00,grow]", "[][][][]"));

		JLabel lblCreateANew = new JLabel("Request a new Account");
		lblCreateANew.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		add(lblCreateANew, "cell 0 0 2 1");

		JLabel lblNewLabel_1 = new JLabel(
				"Select the address of the account to create:");
		add(lblNewLabel_1, "cell 0 2 3 1");

		JLabel lblNewLabel = new JLabel("Address:");
		add(lblNewLabel, "cell 0 3,alignx trailing");

		identifierName = new JTextField();
		add(identifierName, "cell 1 3,growx");
		identifierName.setColumns(10);

		domainSelector = new JComboBox();
		add(domainSelector, "cell 2 3,growx");

	}
}
