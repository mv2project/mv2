package de.iss.mv2.client.gui;

import javax.swing.JComponent;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.iss.mv2.client.data.MailBoxSettings;

/**
 * A control that 
 * @author Marcel Singer
 *
 */
public class MessageCreatorControl extends JComponent {

	
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
	 * @param mailBoxes An array with available mail box settings.
	 */
	public MessageCreatorControl(MailBoxSettings[] mailBoxes) {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new MigLayout("", "[][][grow]", "[][][]"));
		
		sendButton = new JButton("Send");
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
		senderSelector.setModel(new DefaultComboBoxModel<MailBoxSettings>(mailBoxes));
		panel.add(senderSelector, "cell 2 2,growx");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
	}

}
