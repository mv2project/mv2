package de.iss.mv2.client.gui;

import java.awt.Font;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import de.iss.mv2.client.data.WebSpaceSetup;
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.SpaceCreationRequest;
import de.iss.mv2.messaging.SpaceCreationResponse;
import de.iss.mv2.security.CertificateSigningRequest;
import de.iss.mv2.security.RSAOutputStream;

/**
 * A control to request a new web space.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceRequestControl extends JComponent implements Observer,
		AssistantStep {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -8982651936101267677L;

	/**
	 * The text field containing the web space identifier.
	 */
	private JTextField identifierName;

	/**
	 * The label for the domain name.
	 */
	private JLabel domainLabel;

	/**
	 * Holds the information relevant for this setup.
	 */
	private final WebSpaceSetup setupData;
	/**
	 * The text field containing the requesting instances name.
	 */
	private JTextField nameField;
	/**
	 * The text field containing the requesting instances city.
	 */
	private JTextField cityField;
	/**
	 * The text field containing the requesting instances state.
	 */
	private JTextField stateField;
	/**
	 * The text field containing the requesting instances country.
	 */
	private JTextField countryField;
	/**
	 * The text field containing the requesting instances organization.
	 */
	private JTextField organizationField;
	/**
	 * The text field containing the requesting instances organization unit.
	 */
	private JTextField organizationUnitField;
	/**
	 * Contains the key strength.
	 */
	private JComboBox<Integer> keyStrengthSelector;

	/**
	 * Creates a new {@link SpaceRequestControl}.
	 * 
	 * @param webSpaceSetup
	 *            An object containing all relevant information relevant for
	 *            this setup.
	 */
	public SpaceRequestControl(WebSpaceSetup webSpaceSetup) {
		this.setupData = webSpaceSetup;

		Integer[] keyStrengths = new Integer[385];
		for (int i = 0; i < keyStrengths.length; i++) {
			keyStrengths[i] = 1024 + (i * 8);
		}
		setupData.addObserver(this);
		setLayout(new MigLayout("", "[][166.00,grow][98.00,grow]",
				"[][][][][][][][][][][][][][][]"));

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

		domainLabel = new JLabel("@domain.com");
		add(domainLabel, "cell 2 3");
		domainLabel.setText("@" + setupData.getHost());

		JLabel lblName = new JLabel("Name:");
		add(lblName, "cell 0 5,alignx trailing");

		nameField = new JTextField();
		add(nameField, "cell 1 5 2 1,growx");
		nameField.setColumns(10);

		JLabel lblCity = new JLabel("City:");
		add(lblCity, "cell 0 6,alignx trailing");

		cityField = new JTextField();
		add(cityField, "cell 1 6 2 1,growx");
		cityField.setColumns(10);

		JLabel lblState = new JLabel("State:");
		add(lblState, "cell 0 7,alignx trailing");

		stateField = new JTextField();
		add(stateField, "cell 1 7 2 1,growx");
		stateField.setColumns(10);

		JLabel lblCountry = new JLabel("Country:");
		add(lblCountry, "cell 0 8,alignx trailing");

		countryField = new JTextField();
		add(countryField, "cell 1 8 2 1,growx");
		countryField.setColumns(10);

		JLabel lblOrganization = new JLabel("Organization:");
		add(lblOrganization, "cell 0 10,alignx trailing");

		organizationField = new JTextField();
		add(organizationField, "cell 1 10 2 1,growx");
		organizationField.setColumns(10);

		JLabel lblOrganizationUnit = new JLabel("Organization Unit:");
		add(lblOrganizationUnit, "cell 0 11,alignx trailing");

		organizationUnitField = new JTextField();
		add(organizationUnitField, "cell 1 11 2 1,growx");
		organizationUnitField.setColumns(10);

		JLabel lblKeyStrength = new JLabel("Key Strength:");
		add(lblKeyStrength, "cell 0 13,alignx trailing");

		keyStrengthSelector = new JComboBox<Integer>();

		keyStrengthSelector.setModel(new DefaultComboBoxModel<Integer>(
				keyStrengths));
		add(keyStrengthSelector, "cell 1 13,growx");

		JLabel lblBits = new JLabel("Bits");
		add(lblBits, "cell 2 13");
	}

	@Override
	public void update(Observable o, Object arg) {
		domainLabel.setText("@" + setupData.getHost());
	}

	@Override
	public boolean canProceed() {
		if (identifierName.getText().isEmpty()) {
			DialogHelper.showInputError(this, "Address");
			return false;
		}
		KeyPair kp = RSAOutputStream
				.getRandomRSAKey((Integer) keyStrengthSelector
						.getSelectedItem());
		setupData.setClientKey(kp);
		SpaceCreationRequest scr = new SpaceCreationRequest();
		CertificateSigningRequest csr = new CertificateSigningRequest();
		csr.init(identifierName.getText() + "@" + setupData.getHost(),
				countryField.getText(), stateField.getText(),
				cityField.getText(), organizationField.getText(),
				organizationUnitField.getText());
		csr.setMailAddress(csr.getCommonName());
		csr.setName(nameField.getText());
		try {
			scr.setSigningRequest(csr.generatePKCS10(kp));
			MV2Client client = setupData.tryConnect();
			client.send(scr);
			MV2Message response = client.handleNext();
			if (response.getMessageIdentifier() == STD_MESSAGE.UNABLE_TO_PROCESS
					.getIdentifier()) {
				throw new Exception(
						"The server could not process your request. See the following details:\n\n"
								+ response.getFieldValue(
										DEF_MESSAGE_FIELD.CAUSE, "NONE"));
			}
			if (response.getMessageIdentifier() != STD_MESSAGE.SPACE_CREATION_RESPONSE
					.getIdentifier()) {
				throw new Exception("The servers response was invalid.");
			}
			SpaceCreationResponse creationResponse = new SpaceCreationResponse();
			MV2Message.merge(creationResponse, response);
			X509Certificate clientCert = creationResponse.getCertificate();
			CertificateView cv = new CertificateView();
			cv.setCertificate(clientCert);
			
			setupData.setClientCertificate(clientCert);
			setupData.setClientKey(kp);
			
			setupData.complete(identifierName.getText());
		} catch (Exception e) {
			DialogHelper.showErrorMessage(this, "", e.getMessage());
			return false;
		}

		return true;
	}

	@Override
	public boolean canGoBack() {
		return true;
	}

}
