package de.iss.mv2.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import de.iss.mv2.data.BinaryTools;
import de.iss.mv2.security.CertificateNameReader;

/**
 * A control to display informations about a {@link X509Certificate}.
 * 
 * @author Marcel Singer
 *
 */
public class CertificateView extends JComponent {

	/**
	 * Creates a new instance of {@link CertificateView}.
	 */
	public CertificateView() {
		setBackground(new JFrame().getBackground());
		setLayout(new MigLayout("", "[grow][grow][][][grow]",
				"[][][][][][][][][][][][][][][][][][][][][grow][][][grow]"));

		try {
			JButton btnNewButton = new JButton("");
			btnNewButton.setIcon(new ImageIcon(ImageIO.read(getClass()
					.getClassLoader().getResourceAsStream("certificate.png"))));
			add(btnNewButton, "cell 0 0,alignx right");
			btnNewButton.setEnabled(true);
			btnNewButton.setText("");
			btnNewButton.setBorder(BorderFactory.createEmptyBorder());
		} catch (Exception ex) {

		}

		titleLabel = new JLabel("");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		add(titleLabel, "cell 1 0 4 1");

		JSeparator separator_4 = new JSeparator();
		add(separator_4, "cell 1 1 4 1,growx,aligny top");

		JLabel label = new JLabel("<HTML><U>Owner</U></HTML>");
		add(label, "cell 0 2");

		JLabel label_1 = new JLabel("<HTML><U>Issuer</U></HTML>");
		add(label_1, "cell 3 2");

		JLabel lblCountry = new JLabel("Country:");
		add(lblCountry, "cell 0 3,alignx trailing");

		ownerCountry = new JTextField();
		ownerCountry.setEditable(false);
		add(ownerCountry, "cell 1 3,growx");
		ownerCountry.setColumns(10);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		add(separator_1, "cell 2 3 1 7,alignx center,growy");

		JLabel lblCountry_1 = new JLabel("Country:");
		add(lblCountry_1, "cell 3 3,alignx trailing");

		issuerCountry = new JTextField();
		issuerCountry.setEditable(false);
		add(issuerCountry, "cell 4 3,growx");
		issuerCountry.setColumns(10);

		JLabel lblState = new JLabel("State:");
		add(lblState, "cell 0 4,alignx trailing");

		ownerState = new JTextField();
		ownerState.setEditable(false);
		ownerState.setColumns(10);
		add(ownerState, "cell 1 4,growx");

		JLabel lblState_1 = new JLabel("State:");
		add(lblState_1, "cell 3 4,alignx trailing");

		issuerState = new JTextField();
		issuerState.setEditable(false);
		issuerState.setColumns(10);
		add(issuerState, "cell 4 4,growx");

		JLabel label_2 = new JLabel("City:");
		add(label_2, "cell 0 5,alignx trailing");

		ownerCity = new JTextField();
		ownerCity.setEditable(false);
		ownerCity.setColumns(10);
		add(ownerCity, "cell 1 5,growx");

		JLabel label_3 = new JLabel("City:");
		add(label_3, "cell 3 5,alignx trailing");

		issuerCity = new JTextField();
		issuerCity.setEditable(false);
		issuerCity.setColumns(10);
		add(issuerCity, "cell 4 5,growx");

		JLabel lblCompany = new JLabel("Company:");
		add(lblCompany, "cell 0 6,alignx trailing");

		ownerCompany = new JTextField();
		ownerCompany.setEditable(false);
		ownerCompany.setColumns(10);
		add(ownerCompany, "cell 1 6,growx");

		JLabel lblCompany_1 = new JLabel("Company:");
		add(lblCompany_1, "cell 3 6,alignx trailing");

		issuerCompany = new JTextField();
		issuerCompany.setEditable(false);
		issuerCompany.setColumns(10);
		add(issuerCompany, "cell 4 6,growx");

		JLabel lblOragnisationUnit = new JLabel("Oragnisation Unit:");
		add(lblOragnisationUnit, "cell 0 7,alignx trailing");

		ownerOU = new JTextField();
		ownerOU.setEditable(false);
		ownerOU.setColumns(10);
		add(ownerOU, "cell 1 7,growx");

		JLabel lblOrganisationUnit = new JLabel("Organisation Unit:");
		add(lblOrganisationUnit, "cell 3 7,alignx trailing");

		issuerOU = new JTextField();
		issuerOU.setEditable(false);
		issuerOU.setColumns(10);
		add(issuerOU, "cell 4 7,growx");

		JLabel lblGeneralName = new JLabel("Common Name:");
		add(lblGeneralName, "cell 0 8,alignx trailing");

		ownerGN = new JTextField();
		ownerGN.setEditable(false);
		ownerGN.setColumns(10);
		add(ownerGN, "cell 1 8,growx");

		JLabel lblGeneralName_1 = new JLabel("Common Name:");
		add(lblGeneralName_1, "cell 3 8,alignx trailing");

		issuerGN = new JTextField();
		issuerGN.setEditable(false);
		issuerGN.setColumns(10);
		add(issuerGN, "cell 4 8,growx");

		JLabel lblMail = new JLabel("Mail:");
		add(lblMail, "cell 0 9,alignx trailing");

		ownerMail = new JTextField();
		ownerMail.setEditable(false);
		ownerMail.setColumns(10);
		add(ownerMail, "cell 1 9,growx");

		JLabel lblMail_1 = new JLabel("Mail:");
		add(lblMail_1, "cell 3 9,alignx trailing");

		issuerMail = new JTextField();
		issuerMail.setEditable(false);
		issuerMail.setColumns(10);
		add(issuerMail, "cell 4 9,growx");

		JSeparator separator = new JSeparator();
		add(separator, "cell 0 10 5 1,growx");

		JLabel lblNewLabel = new JLabel("Version:");
		add(lblNewLabel, "cell 0 11,alignx right");

		versionLabel = new JLabel("");
		add(versionLabel, "cell 1 11 4 1");

		JLabel lblSerialNumber = new JLabel("Serial Number:");
		add(lblSerialNumber, "cell 0 12,alignx right");

		serialLabel = new JLabel("");
		add(serialLabel, "cell 1 12 4 1");

		JLabel lblSignaturealgorithm = new JLabel("Signature-Algorithm:");
		add(lblSignaturealgorithm, "cell 0 13,alignx right");

		sigAlgorithmLabel = new JLabel("");
		add(sigAlgorithmLabel, "cell 1 13 4 1");

		JLabel lblNewLabel_1 = new JLabel("Valid from:");
		add(lblNewLabel_1, "cell 0 15,alignx right");

		startLabel = new JLabel("");
		add(startLabel, "cell 1 15 4 1");

		JLabel lblValidTo = new JLabel("Valid to:");
		add(lblValidTo, "cell 0 16,alignx right");

		endLabel = new Label("");
		add(endLabel, "cell 1 16 4 1");

		JSeparator separator_2 = new JSeparator();
		add(separator_2, "cell 0 17 5 1,growx");

		JLabel lblPublickey = new JLabel("<HTML><U>Public-Key</U></HTML>");
		add(lblPublickey, "cell 0 18,alignx right");

		JLabel lblNewLabel_2 = new JLabel("Algorithm:");
		add(lblNewLabel_2, "cell 0 19,alignx right");

		publicKeyAlgorithmLabel = new JLabel("");
		add(publicKeyAlgorithmLabel, "cell 1 19 4 1");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(null);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, "cell 1 20 4 1,grow");

		keyContent = new JTextArea();
		keyContent.setEditable(false);
		keyContent.setFont(new Font("Lucida Console", Font.PLAIN, 13));
		keyContent.setBackground(null);
		keyContent.setLineWrap(true);
		keyContent.setWrapStyleWord(true);
		scrollPane.setViewportView(keyContent);

		JSeparator separator_3 = new JSeparator();
		add(separator_3, "cell 0 21 5 1,growx");

		JLabel lblNewLabel_3 = new JLabel("<HTML><U>Signature</U></HTML>");
		add(lblNewLabel_3, "cell 0 22,alignx right");

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBackground((Color) null);
		add(scrollPane_1, "cell 1 23 4 1,grow");

		sigContent = new JTextArea();
		sigContent.setFont(new Font("Lucida Console", Font.PLAIN, 13));
		sigContent.setBackground(null);
		sigContent.setEditable(false);
		sigContent.setLineWrap(true);
		sigContent.setWrapStyleWord(true);
		scrollPane_1.setViewportView(sigContent);
	}

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 323059759233716357L;
	/**
	 * The text field containing the subjects country.
	 */
	private JTextField ownerCountry;
	/**
	 * The text field containing the issuers country.
	 */
	private JTextField issuerCountry;
	/**
	 * The text field containing the subjects state.
	 */
	private JTextField ownerState;
	/**
	 * The text field containing the issuers state.
	 */
	private JTextField issuerState;
	/**
	 * The text field containing the subjects location.
	 */
	private JTextField ownerCity;
	/**
	 * The text field containing the issuers location.
	 */
	private JTextField issuerCity;
	/**
	 * The text field containing the subjects organization unit.
	 */
	private JTextField ownerOU;
	/**
	 * The text field containing the issuers organization unit.
	 */
	private JTextField issuerOU;
	/**
	 * The text field containing the subjects organization.
	 */
	private JTextField ownerCompany;
	/**
	 * The text field containing the issuers organization.
	 */
	private JTextField issuerCompany;
	/**
	 * The text field containing the subjects common name.
	 */
	private JTextField ownerGN;
	/**
	 * The text field containing the issuers common name.
	 */
	private JTextField issuerGN;
	/**
	 * The text field containing the subjects mail address.
	 */
	private JTextField ownerMail;
	/**
	 * The text field containing the issuers mail address.
	 */
	private JTextField issuerMail;
	/**
	 * The text field containing the certificate version.
	 */
	private JLabel versionLabel;
	/**
	 * The text field containing the certificate serial number.
	 */
	private JLabel serialLabel;
	/**
	 * The text field containing the name of the signature algorithm of the
	 * certificate.
	 */
	private JLabel sigAlgorithmLabel;
	/**
	 * The text field containing the start date of validity of the certificate.
	 */
	private JLabel startLabel;
	/**
	 * The text field containing the end date of validity of the certificate.
	 */
	private Label endLabel;
	/**
	 * The text field containing the public key algorithm of the certificate.
	 */
	private JLabel publicKeyAlgorithmLabel;
	/**
	 * The text area containing the public key of the certificate.
	 */
	private JTextArea keyContent;
	/**
	 * The text area containing the signature of the certificate.
	 */
	private JTextArea sigContent;
	/**
	 * The label containing the title (Subject CN) of this certificate.
	 */
	private JLabel titleLabel;

	/**
	 * Sets the certificate to display.
	 * 
	 * @param cert
	 *            The certificate to display.
	 */
	public void setCertificate(X509Certificate cert) {
		CertificateNameReader owner = new CertificateNameReader(
				cert.getSubjectX500Principal());
		ownerCity.setText(owner.getCity());
		ownerCountry.setText(owner.getCountry());
		ownerCompany.setText(owner.getOrangization());
		titleLabel.setText(owner.getCommonName());
		ownerGN.setText(owner.getCommonName());
		ownerOU.setText(owner.getOrganizationUnit());
		ownerState.setText(owner.getState());

		CertificateNameReader issuer = new CertificateNameReader(
				cert.getIssuerX500Principal());
		issuerCity.setText(issuer.getCity());
		issuerCountry.setText(issuer.getCountry());
		issuerCompany.setText(issuer.getOrangization());
		issuerGN.setText(issuer.getCommonName());
		issuerOU.setText(issuer.getOrganizationUnit());
		issuerState.setText(issuer.getState());

		versionLabel.setText("" + cert.getVersion());
		serialLabel.setText("" + cert.getSerialNumber());
		sigAlgorithmLabel.setText(cert.getSigAlgName() + " ( "
				+ cert.getSigAlgOID() + " )");

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		startLabel.setText(sdf.format(cert.getNotBefore()));
		endLabel.setText(sdf.format(cert.getNotAfter()));

		PublicKey pk = cert.getPublicKey();
		publicKeyAlgorithmLabel.setText(pk.getAlgorithm() + " ( "
				+ pk.getFormat() + " )");
		if (pk.getAlgorithm().equals("RSA")) {
			RSAPublicKey rsaPK = (RSAPublicKey) pk;
			byte[] keyData = rsaPK.getModulus().toByteArray();
			StringBuilder sb = new StringBuilder();
			sb.append("Exponent: " + rsaPK.getPublicExponent().toString()
					+ "\n");
			sb.append("Keylength: " + ((keyData.length - 1) * 8) + " Bit\n\n");
			sb.append((keyData.length - 1) + " Bytes:\n");
			keyData = Arrays.copyOfRange(keyData, 1, keyData.length);
			sb.append(BinaryTools.insertSpacing(
					BinaryTools.toHexString(keyData), " ", 2));

			keyContent.setText(sb.toString());
		} else {
			keyContent.setText(BinaryTools.insertSpacing(
					BinaryTools.toHexString(pk.getEncoded()), " ", 2));
		}

		byte[] signature = cert.getSignature();
		StringBuilder sb2 = new StringBuilder();
		sb2.append(signature.length + " Bytes:\n");
		sb2.append(BinaryTools.insertSpacing(
				BinaryTools.toHexString(signature), " ", 2));
		sigContent.setText(sb2.toString());
	}

}
