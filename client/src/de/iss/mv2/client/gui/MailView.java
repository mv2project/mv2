package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.text.DateFormat;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.iss.mv2.client.data.MailMessage;

/**
 * A control to display a mail message.
 * 
 * @author Marcel Singer
 *
 */
public class MailView extends JComponent {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -2684153159923168642L;

	/**
	 * Displays the content text of this message.
	 */
	private final JEditorPane contentArea = new JEditorPane();
	/**
	 * Holds the label to display the subject.
	 */
	private JLabel subjectLabel;
	/**
	 * Holds the label to display the sender.
	 */
	private JLabel fromLabel;
	/**
	 * Holds the label to display the receiver.
	 */
	private JLabel toLabel;
	/**
	 * Holds the label to display the timestamp.
	 */
	private JLabel timestampLabel;

	/**
	 * Creates a new instance of {@link MailView}.
	 */
	public MailView() {
		setLayout(new BorderLayout());
		JPanel upperPanel = new JPanel();
		add(upperPanel, BorderLayout.NORTH);
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));

		subjectLabel = new JLabel("");
		subjectLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		upperPanel.add(subjectLabel);

		fromLabel = new JLabel("");
		upperPanel.add(fromLabel);

		toLabel = new JLabel("");
		upperPanel.add(toLabel);

		timestampLabel = new JLabel("");
		upperPanel.add(timestampLabel);
		contentArea.setEditable(false);
		add(new JScrollPane(contentArea), BorderLayout.CENTER);
	}

	/**
	 * Sets the message to be displayed.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	public void setMessage(MailMessage message) {
		if (message == null)
			return;
		contentArea.setText(message.getContent());
		subjectLabel
				.setText("<HTML><B>" + message.getSubject() + "</B></HTML>");
		fromLabel.setText("<HTML><B>From</B>: " + message.getSender()
				+ "</HTML>");
		toLabel.setText("<HTML><B>To</B>: " + message.getSender() + "</HTML>");
		DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM,
				Locale.getDefault());
		timestampLabel.setText("<HTML><B>Received</B>: "
				+ formatter.format(message.getTimestamp()) + "</HTML>");
		repaint();
		updateUI();
	}

}
