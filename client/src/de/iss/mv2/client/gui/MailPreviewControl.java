package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.iss.mv2.client.data.MailMessage;

/**
 * A preview control to be displayed in the mail list.
 * 
 * @author Marcel Singer
 *
 */
public class MailPreviewControl extends JPanel {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = 3459518376012572538L;

	/**
	 * Holds the message to be previewed.
	 */
	private final MailMessage message;

	/**
	 * Initializes a new {@link MailPreviewControl} to preview the given mail.
	 * 
	 * @param message
	 *            The message to be previewed.
	 */
	public MailPreviewControl(MailMessage message) {
		this.message = message;
		setLayout(new BorderLayout());
		JLabel label = new JLabel("<HTML><P><B>" + message.getSubject()
				+ "</B></P></HTML>");
		add(label, BorderLayout.NORTH);
		add(new JLabel(message.getSender()), BorderLayout.SOUTH);
		setBackground(null);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	/**
	 * Returns the mail message previewed by this control.
	 * 
	 * @return The mail message previewed by this control.
	 */
	public MailMessage getMessage() {
		return message;
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		for (Component c : getComponents())
			c.setForeground(fg);
	}

}
