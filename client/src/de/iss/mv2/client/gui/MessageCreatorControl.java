package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import de.iss.mv2.MV2Constants;
import de.iss.mv2.client.data.MV2ClientSettings;
import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.data.MailMessage;
import de.iss.mv2.client.io.ClientProviderImpl;
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.client.messaging.ClientCertificateRequestProcedure;
import de.iss.mv2.client.messaging.MailSendingProcedure;
import de.iss.mv2.client.messaging.MessageProcedure;
import de.iss.mv2.client.messaging.ProcedureException;
import de.iss.mv2.client.messaging.ProcedureListenerAdapter;
import de.iss.mv2.client.messaging.ProcedureResultListener;
import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.gui.SubmitDialog;
import de.iss.mv2.security.AESWithRSACryptoSettings;
import javax.swing.ScrollPaneConstants;

/**
 * A control to create a mail message.
 * 
 * @author Marcel Singer
 *
 */
public class MessageCreatorControl extends JComponent implements ActionListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = 6656950244610337800L;

	/**
	 * The text field containing the receiver.
	 */
	private JTextField receiverField;
	/**
	 * The test field containing the subject.
	 */
	private JTextField subjectField;
	/**
	 * The button to send this message.
	 */
	private JButton sendButton;
	/**
	 * The drop down menu to select the senders web space.
	 */
	private JComboBox<MailBoxSettings> senderSelector;

	/**
	 * A field containing the content text of the message.
	 */
	private JTextArea messageContent;

	/**
	 * Creates a new instance of {@link MessageCreatorControl}.
	 * 
	 * @param mailBoxes
	 *            An array with available mail box settings.
	 */
	public MessageCreatorControl(MailBoxSettings[] mailBoxes) {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new MigLayout("", "[][][grow]", "[][][]"));

		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		panel.add(sendButton, "cell 0 0 1 3,growy");

		JLabel lblTo = new JLabel("To:");
		panel.add(lblTo, "cell 1 0,alignx trailing");

		receiverField = new JTextField();
		panel.add(receiverField, "cell 2 0,growx");
		receiverField.setColumns(10);

		JLabel lblSubject = new JLabel("Subject:");
		panel.add(lblSubject, "cell 1 1,alignx trailing");

		subjectField = new JTextField();
		panel.add(subjectField, "cell 2 1,growx");
		subjectField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Sender:");
		panel.add(lblNewLabel, "cell 1 2,alignx trailing");

		senderSelector = new JComboBox<MailBoxSettings>();
		senderSelector.setModel(new DefaultComboBoxModel<MailBoxSettings>(
				mailBoxes));
		panel.add(senderSelector, "cell 2 2,growx");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		messageContent = new JTextArea();
		messageContent.setWrapStyleWord(true);
		messageContent.setLineWrap(true);
		scrollPane.setViewportView(messageContent);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton) {
			try {
				final MV2Client client = new ClientProviderImpl()
						.connectToWebSpace(receiverField.getText());
				ClientCertificateRequestProcedure ccrp = new ClientCertificateRequestProcedure(
						client, receiverField.getText());
				LoadingDialog ld = new LoadingDialog(
						DialogHelper.getParentFrame(this), false);
				ld.setVisible(true);
				ccrp.addProcedureListener(ld);
				ccrp.addProcedureListener(new ProcedureListenerAdapter<X509Certificate>(
						new ProcedureResultListener<X509Certificate>() {

							@Override
							public void handleProcedureException(
									MessageProcedure<? extends Throwable, X509Certificate> procedure,
									ProcedureException ex) {
								try {
									client.disconnect();
								} catch (IOException e) {
								}
								DialogHelper
										.showActionFailedWithExceptionMessage(
												MessageCreatorControl.this, ex);
							}

							@Override
							public void procedureCompleted(
									MessageProcedure<? extends Throwable, X509Certificate> procedure,
									X509Certificate result) {
								try {
									client.disconnect();
								} catch (IOException e) {
								}
								if (!MV2ClientSettings.getRuntimeSettings()
										.getTrustedClientCertificates()
										.hasCertificate(result)) {
									CertificateView cv = new CertificateView();
									cv.setCertificate(result);
									SubmitDialog<JComponent> dialog = DialogHelper
											.showBlockingSubmitDialog(
													"Certificate", null,
													new JScrollPane(cv));
									if (dialog.getDialogResult() != MV2Constants.SUBMIT_OPTION) {
										DialogHelper
												.showErrorMessage(
														MessageCreatorControl.this,
														"Certificate discarded",
														"The system is not able to send this mail because you discarded the receivers certificate.");
										return;
									}
									MV2ClientSettings.getRuntimeSettings()
											.getTrustedClientCertificates()
											.add(result);
								}
								sendMessage(result);
							}
						}));
				ccrp.run();
			} catch (IOException ex) {
				DialogHelper.showActionFailedWithExceptionMessage(this, ex);
			}
		}
	}

	/**
	 * Sends the message.
	 * 
	 * @param receiverCert
	 *            The certificate of the receiver.
	 */
	@SuppressWarnings("unchecked")
	private void sendMessage(X509Certificate receiverCert) {
		LoadingDialog ld = new LoadingDialog(DialogHelper.getParentFrame(this),
				false);
		ld.setVisible(true);
		MailMessage mm = new MailMessage();
		mm.setContent(messageContent.getText());
		mm.setSubject(subjectField.getText());
		mm.setReceivers(receiverField.getText().split(";"));
		MailSendingProcedure msp = new MailSendingProcedure(
				new AESWithRSACryptoSettings(),
				mm,
				MV2ClientSettings.getRuntimeSettings().getMailBoxesArray()[senderSelector
						.getSelectedIndex()]);
		msp.addProcedureListener(ld);
		msp.addProcedureListener(new ProcedureListenerAdapter<Void>(
				new ProcedureResultListener<Void>() {

					@Override
					public void handleProcedureException(
							MessageProcedure<? extends Throwable, Void> procedure,
							ProcedureException ex) {
						ex.printStackTrace();
						DialogHelper.showActionFailedWithExceptionMessage(
								MessageCreatorControl.this, ex);
					}

					@Override
					public void procedureCompleted(
							MessageProcedure<? extends Throwable, Void> procedure,
							Void result) {
						DialogHelper.showSuccessMessage(
								MessageCreatorControl.this,
								"Delivery Successfull",
								"The mail was successfully delivered.");
					}
				}));
		msp.run();
	}

}
