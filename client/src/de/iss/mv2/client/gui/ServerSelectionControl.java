package de.iss.mv2.client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.gui.SubmitDialog;
import de.iss.mv2.gui.SubmitListener;
import de.iss.mv2.messaging.DomainNamesRequest;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A dialog that helps the user to configure his client.
 * 
 * @author Marcel Singer
 * 
 */
public class ServerSelectionControl extends JComponent implements
		ActionListener, ComponentListener {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 8351389468624490205L;
	/**
	 * A panel containing the information about the connection.
	 */
	private JPanel connectionPanel;
	/**
	 * A text field containing the host address.
	 */
	private JTextField hostField;
	/**
	 * A text field containing the host port.
	 */
	private JTextField portField;
	/**
	 * A panel containing information about the servers certificate.
	 */
	private JPanel certificatesPanel;
	/**
	 * A button to connect to the given server.
	 */
	private JButton connectButton;
	/**
	 * A control to display the servers certificate.
	 */
	private CertificateView certView = new CertificateView();

	/**
	 * Crates a new instance of {@link ServerSelectionControl}.
	 */
	public ServerSelectionControl() {
		addComponentListener(this);
		Dimension s = new Dimension(800, 600);
		setMinimumSize(s);
		setSize(s);
		setPreferredSize(s);
		setLayout(new MigLayout("", "[grow]", "[][][grow]"));

		connectionPanel = new JPanel();
		add(connectionPanel, "cell 0 0,grow");
		connectionPanel.setLayout(new MigLayout("", "[][grow][][]",
				"[][14.00][][][]"));

		JLabel lblServeradress = new JLabel("<HTML><B>Server</B></HTML>");
		connectionPanel.add(lblServeradress, "cell 0 0");

		JLabel lblAddress = new JLabel("Address:");
		connectionPanel.add(lblAddress, "cell 0 2,alignx trailing");

		hostField = new JTextField();
		connectionPanel.add(hostField, "cell 1 2,growx");
		hostField.setColumns(10);

		JLabel lblPort = new JLabel("Port:");
		connectionPanel.add(lblPort, "cell 2 2,alignx trailing");

		portField = new JTextField();
		connectionPanel.add(portField, "cell 3 2,growx");
		portField.setColumns(8);

		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		connectionPanel.add(connectButton, "cell 3 4,growx");

		certificatesPanel = new JPanel();
		certificatesPanel.setEnabled(false);
		add(certificatesPanel, "cell 0 2,grow");
		certificatesPanel
				.setLayout(new MigLayout("", "[grow]", "[][][grow][]"));

		JLabel lblcertificates = new JLabel("<HTML><B>Certificates</B></HTML>");
		certificatesPanel.add(lblcertificates, "cell 0 0");

		scrollPane = new JScrollPane();

		scrollPane.setViewportView(certView);
		certificatesPanel.add(scrollPane, "cell 0 2,grow");
	}

	/**
	 * Connects to the given server.
	 * 
	 * @param host
	 *            The address of the server.
	 * @param port
	 *            The port of the server.
	 * @return A connected client instance.
	 * @throws Exception
	 *             If the connection can't be opened for any reasons.
	 */
	private MV2Client tryConnect(String host, int port) throws Exception {
		MV2Client client = new MV2Client();
		client.connect(host, port);
		MV2Message message = new MV2Message(STD_MESSAGE.CERT_REQUEST);
		client.send(message);
		message = client.handleNext();

		if (message.getMessageIdentifier() != STD_MESSAGE.DOMAIN_NAMES_RESPONSE
				.getIdentifier()) {
			message = new DomainNamesRequest();
			client.send(message);
			client.handleNext();
		}
		return client;
	}

	/**
	 * The certificate of the server.
	 */
	private X509Certificate cert;
	/**
	 * The scroll pane containing the certificate control.
	 */
	private JScrollPane scrollPane;

	/**
	 * Holds the selected domain.
	 */
	private String serverDomain;

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == connectButton) {
			try {
				String host = hostField.getText();
				int port = Integer.parseInt(portField.getText());
				MV2Client client = tryConnect(host, port);
				if (client == null)
					throw new RuntimeException(
							"Can't connect to the remote host.");
				String[] domainNames = client.getAlternativeNames();
				if (domainNames.length == 0)
					throw new RuntimeException(
							"There is no available domain on this server.");
				final DomainSelectorControl dsc = new DomainSelectorControl();
				dsc.setAvailableDomainNames(domainNames);
				SubmitDialog<DomainSelectorControl> submitDialog;
				JFrame parentFrame = DialogHelper.getParentFrame(this);
				if (parentFrame != null) {
					submitDialog = new SubmitDialog<>(parentFrame, dsc,
							"Select a mail domain", true);
				} else {
					submitDialog = new SubmitDialog<>(
							DialogHelper.getParentDialog(this), dsc,
							"Select a domain", true);
				}
				submitDialog.addSubmitListener(new SubmitListener() {

					@Override
					public void submitted(Object sender) {
						serverDomain = dsc.getSelectedDomainName();
						hostField.setText(serverDomain);
						loadCertificate();
					}

					@Override
					public void canceled(Object sender) {

					}
				});
				submitDialog.pack();
				submitDialog.setVisible(true);
				try {
					client.disconnect();
				} catch (Exception iex) {

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				certificatesPanel.setEnabled(false);
				DialogHelper.showActionFailedWithExceptionMessage(this, ex);
			}
		}
	}

	/**
	 * Loads the certificate from the server.
	 */
	private void loadCertificate() {
		try {
			MV2Client client = tryConnect(serverDomain,
					Integer.parseInt(portField.getText()));
			if (client.getServerCertificate() == null) {
				throw new Exception("Could not load the server certificate.");
			}
			cert = client.getServerCertificate();
			setCertificate(cert);
			certificatesPanel.setEnabled(true);
			try {
				client.disconnect();
			} catch (Exception e) {

			}
		} catch (Exception ex) {
			certificatesPanel.setEnabled(false);
			DialogHelper.showActionFailedWithExceptionMessage(this, ex);
		}
	}

	/**
	 * Sets the servers certificate.
	 * 
	 * @param cert
	 *            The certificate to set.
	 */
	private void setCertificate(X509Certificate cert) {
		this.cert = cert;
		certView = new CertificateView();
		scrollPane.setViewportView(certView);
		certView.setCertificate(cert);
		repaint();
		certificatesPanel.repaint();
	}

	@Override
	public void componentResized(ComponentEvent e) {

		if (certView != null)
			certView.setPreferredSize(new Dimension((int) scrollPane
					.getViewport().getSize().getWidth(), (int) certView
					.getPreferredSize().getHeight()));
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

}
