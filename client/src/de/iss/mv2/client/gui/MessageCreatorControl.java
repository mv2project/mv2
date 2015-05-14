package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.cert.CertificateException;
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
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.client.messaging.ClientCertificateRequestProcedure;
import de.iss.mv2.client.messaging.MessageProcedure;
import de.iss.mv2.client.messaging.ProcedureException;
import de.iss.mv2.client.messaging.ProcedureListenerAdapter;
import de.iss.mv2.client.messaging.ProcedureResultListener;
import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.gui.SubmitDialog;

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
		add(scrollPane, BorderLayout.CENTER);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton) {
			MailBoxSettings mbs = (MailBoxSettings) senderSelector
					.getSelectedItem();
			try {
				MV2Client client = mbs.getClient();
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
								DialogHelper
										.showActionFailedWithExceptionMessage(
												MessageCreatorControl.this, ex);
							}

							@Override
							public void procedureCompleted(
									MessageProcedure<? extends Throwable, X509Certificate> procedure,
									X509Certificate result) {
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
								MV2ClientSettings.getRuntimeSettings().getTrustedClientCertificates().add(result);
							}
						}));
				ccrp.run();
			} catch (IOException | CertificateException ex) {
				DialogHelper.showActionFailedWithExceptionMessage(this, ex);
			}
		}
	}

}
