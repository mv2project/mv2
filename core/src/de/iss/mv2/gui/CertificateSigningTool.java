package de.iss.mv2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import de.iss.mv2.client.gui.CertificateView;
import de.iss.mv2.client.gui.PassphraseDialog;
import de.iss.mv2.data.LocalCertificateManager;
import de.iss.mv2.security.CertificateSigner;
import de.iss.mv2.security.CertificateSigningRequest;
import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.security.RSAOutputStream;

/**
 * A dialog to create a certificate.
 * 
 * @author Marcel Singer
 *
 */
public class CertificateSigningTool extends JFrame implements ActionListener {

	/**
	 * Creates a new instance of {@link CertificateSigningTool}.
	 */
	public CertificateSigningTool() {
		getContentPane().setLayout(
				new MigLayout("", "[][grow][]", "[][][][][][][][][][][]"));

		JLabel lblSignerscertificate = new JLabel("Signers certificate:");
		getContentPane().add(lblSignerscertificate, "cell 0 0,alignx trailing");

		signersCert = new JTextField();
		getContentPane().add(signersCert, "cell 1 0,growx");
		signersCert.setColumns(10);

		selectCertButton = new JButton("Select");
		selectCertButton.addActionListener(this);
		getContentPane().add(selectCertButton, "cell 2 0,growx");

		JLabel lblNewLabel = new JLabel("Signers private key:");
		getContentPane().add(lblNewLabel, "cell 0 1,alignx trailing");

		signersKey = new JTextField();
		getContentPane().add(signersKey, "cell 1 1,growx");
		signersKey.setColumns(10);

		selectKeyButton = new JButton("Select");
		selectKeyButton.addActionListener(this);
		getContentPane().add(selectKeyButton, "cell 2 1,growx");

		JSeparator separator = new JSeparator();
		getContentPane().add(separator, "flowx,cell 0 2 3 1");

		JLabel lblCommonName = new JLabel("Common name:");
		getContentPane().add(lblCommonName, "cell 0 3,alignx trailing");

		commonName = new JTextField();
		getContentPane().add(commonName, "cell 1 3 2 1,growx");
		commonName.setColumns(10);

		JLabel lblLocation = new JLabel("Location:");
		getContentPane().add(lblLocation, "cell 0 4,alignx trailing");

		location = new JTextField();
		getContentPane().add(location, "cell 1 4 2 1,growx");
		location.setColumns(10);

		JLabel lblState = new JLabel("State:");
		getContentPane().add(lblState, "cell 0 5,alignx trailing");

		state = new JTextField();
		getContentPane().add(state, "cell 1 5 2 1,growx");
		state.setColumns(10);

		JLabel lblCountry = new JLabel("Country:");
		getContentPane().add(lblCountry, "cell 0 6,alignx trailing");

		country = new JTextField();
		getContentPane().add(country, "cell 1 6 2 1,growx");
		country.setColumns(10);

		JLabel lblOrganization = new JLabel("Organization:");
		getContentPane().add(lblOrganization, "cell 0 7,alignx trailing");

		organization = new JTextField();
		getContentPane().add(organization, "cell 1 7 2 1,growx");
		organization.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Organization unit:");
		getContentPane().add(lblNewLabel_1, "cell 0 8,alignx trailing");

		organizationUnit = new JTextField();
		getContentPane().add(organizationUnit, "cell 1 8 2 1,growx");
		organizationUnit.setColumns(10);

		allowResign = new JCheckBox("Allow resign");
		getContentPane().add(allowResign, "cell 1 9");

		createButton = new JButton("Create");
		createButton.addActionListener(this);
		getContentPane().add(createButton, "cell 2 10,growx");

		pack();
	}

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -7496695889535920113L;

	/**
	 * The text field of the path to the certificate.
	 */
	private JTextField signersCert;
	/**
	 * The text field of the path to the private key.
	 */
	private JTextField signersKey;
	/**
	 * The text field to enter the common name.
	 */
	private JTextField commonName;
	/**
	 * The text field to enter the location.
	 */
	private JTextField location;
	/**
	 * The text field to enter the state.
	 */
	private JTextField state;
	/**
	 * The text field to enter the country.
	 */
	private JTextField country;
	/**
	 * The text field to enter the organization.
	 */
	private JTextField organization;
	/**
	 * The text field to enter the organization unit.
	 */
	private JTextField organizationUnit;
	/**
	 * The check box declaring if the certificate to create should be capable to
	 * resign certificates.
	 */
	private JCheckBox allowResign;
	/**
	 * The button to select the path to the certificate file.
	 */
	private JButton selectCertButton;
	/**
	 * The button to select the path to the key file.
	 */
	private JButton selectKeyButton;
	/**
	 * The button to start the creation.
	 */
	private JButton createButton;

	/**
	 * The loaded certificate.
	 */
	private X509Certificate caCert;
	/**
	 * The loaded private key.
	 */
	private PrivateKey caKey;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectKeyButton) {
			JFileChooser jfc = new JFileChooser(new File(signersKey.getText()));
			jfc.setMultiSelectionEnabled(false);
			jfc.setFileFilter(new FileNameExtensionFilter("Private Key File",
					"pem", "der", "p7b"));
			if (jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			File f = jfc.getSelectedFile();
			if (!f.exists()) {
				DialogHelper.showErrorMessage(this, "Invalid File",
						"The selected file does not exist.");
				return;
			}
			PassphraseDialog pd = new PassphraseDialog(
					"Enter the passphrase to decrypt the private key file:",
					true);
			pd.setLocationRelativeTo(this);
			pd.setVisible(true);
			if (pd.getDialogResult() != PassphraseDialog.SUBMIT_OPTION)
				return;
			String passphrase = new String(pd.getPassword());
			signersKey.setText(f.getAbsolutePath());
			try {
				PEMFileIO pem = new PEMFileIO();
				FileInputStream in = new FileInputStream(f);
				PrivateKey pk = pem.readEncryptedPrivateKey(in, passphrase);
				in.close();
				if (pk == null)
					throw new Exception();
				caKey = pk;
			} catch (Exception ex) {
				DialogHelper
						.showErrorMessage(this, "Invalid file or passphrase",
								"Either the given file or passphrase is invalid and could not be read.");
				return;
			}
		}
		if (e.getSource() == selectCertButton) {
			JFileChooser jfc = new JFileChooser(new File(signersCert.getText()));
			jfc.setMultiSelectionEnabled(false);
			jfc.setFileFilter(new FileNameExtensionFilter("Certificate-File",
					"cert", "pem", "der"));
			if (jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			File f = jfc.getSelectedFile();
			if (!f.exists()) {
				DialogHelper.showErrorMessage(this, "Invalid File",
						"The selected file does not exist.");
				return;
			}
			signersCert.setText(f.getAbsolutePath());
			try {
				PEMFileIO pem = new PEMFileIO();
				FileInputStream in = new FileInputStream(f);
				X509Certificate cert = pem.readCertificate(in);
				in.close();
				caCert = cert;
				if (cert == null)
					throw new Exception();

				CertificateView cv = new CertificateView();
				cv.setCertificate(caCert);
				DialogHelper.showDialog(this, "Certificate Details", cv);
			} catch (Exception ex) {
				DialogHelper
						.showErrorMessage(this, "Invalid File",
								"The selected file seems to be invalid and could not be read.");
				return;
			}
		}
		if (e.getSource() == createButton) {
			createCert();
		}
	}

	/**
	 * Validates the user input.
	 * 
	 * @return {@code true} if all needed inputs are present and valid.
	 */
	private boolean validateInputs() {
		if (caCert == null) {
			DialogHelper.showErrorMessage(this, "No certificate",
					"Please select the certificate of the signing instance.");
			return false;
		}
		if (caKey == null) {
			DialogHelper.showErrorMessage(this, "No key",
					"Please select the key of the signing instance.");
			return false;
		}
		if (commonName.getText().isEmpty()) {
			DialogHelper.showInputError(this, "Common name");
			return false;
		}
		if (location.getText().isEmpty()) {
			DialogHelper.showInputError(this, "Location");
			return false;
		}
		if (state.getText().isEmpty()) {
			DialogHelper.showInputError(this, "State");
			return false;
		}
		if (country.getText().isEmpty()) {
			DialogHelper.showInputError(this, "Country");
			return false;
		}
		if (organization.getText().isEmpty()) {
			DialogHelper.showInputError(this, "Organization");
			return false;
		}
		if (organizationUnit.getText().isEmpty()) {
			DialogHelper.showInputError(this, "Organization Unit");
			return false;
		}
		return true;
	}

	/**
	 * Creates the certificate.
	 */
	private void createCert() {
		try {
			if (!validateInputs())
				return;
			KeyPair clientKey = RSAOutputStream.getRandomRSAKey(2048);
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Select a location to store the private key");
			if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			PassphraseDialog pd = new PassphraseDialog(
					"Select a passphrase to encrpyt the private key:", true);
			pd.setLocationRelativeTo(this);
			pd.setVisible(true);
			String passphrase = new String(pd.getPassword());
			if (pd.getDialogResult() != PassphraseDialog.SUBMIT_OPTION)
				return;
			File f = jfc.getSelectedFile();
			OutputStream out = new FileOutputStream(f);
			PEMFileIO pemIO = new PEMFileIO();
			pemIO.writePKCS8EncryptedPrivateKey(out, clientKey.getPrivate(),
					passphrase);
			out.flush();
			out.close();
			PKCS10CertificationRequest csr = createRequest(clientKey);
			CertificateSigner signer = new CertificateSigner(caCert,
					new LocalCertificateManager(caCert), new SecureRandom());
			X509Certificate sigendCert = signer.sign(caKey, csr,
					allowResign.isSelected());
			jfc = new JFileChooser(f.getAbsoluteFile());
			jfc.setDialogTitle("Select a location to store the signed certificate");
			if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			f = jfc.getSelectedFile();
			out = new FileOutputStream(f);
			pemIO.writeCertificate(out, sigendCert);
			out.flush();
			out.close();
			DialogHelper.showSuccessMessage(
					this,
					"Certificate generated",
					"The sigend certificate was exported to '"
							+ f.getAbsolutePath() + "'.");
		} catch (Exception ex) {
			DialogHelper.showActionFailedWithExceptionMessage(this, ex);
		}
	}

	/**
	 * Creates a {@link PKCS10CertificationRequest} with the given user inputs.
	 * 
	 * @param kp
	 *            The key pair of the requesting instance.
	 * @return The created certification request.
	 * @throws OperatorCreationException
	 *             If an exception occurs during the generation of the request.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private PKCS10CertificationRequest createRequest(KeyPair kp)
			throws OperatorCreationException, IOException {
		CertificateSigningRequest csr = new CertificateSigningRequest();
		csr.init(commonName.getText(), country.getText(), state.getText(),
				location.getText(), organization.getText(),
				organizationUnit.getText());
		return csr.generatePKCS10(kp);
	}

}
