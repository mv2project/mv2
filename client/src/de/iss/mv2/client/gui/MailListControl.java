package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import de.iss.mv2.client.data.MailMessage;
import de.iss.mv2.client.data.MailStorage;
import de.iss.mv2.client.data.MailStorageListener;

/**
 * A list to display a list of mails.
 * 
 * @author Marcel Singer
 *
 */
public class MailListControl extends JComponent implements MailStorageListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -1780306309494780559L;

	/**
	 * Holds the store with the mails to display.
	 */
	private final MailStorage mailStorage;

	/**
	 * Holds the list.
	 */
	private final JList<MailMessage> list = new JList<MailMessage>();

	/**
	 * Creates a new instance of {@link MailListControl}.
	 * 
	 * @param mailStorage
	 *            The mail storage with the mails to display.
	 */
	public MailListControl(MailStorage mailStorage) {
		this.mailStorage = mailStorage;
		mailStorage.addChangeListener(this);
		setLayout(new BorderLayout());
		list.setCellRenderer(new ListCellRenderer<MailMessage>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends MailMessage> list, MailMessage value,
					int index, boolean isSelected, boolean cellHasFocus) {
				JComponent comp = new MailPreviewControl(value);
				if (isSelected) {
					comp.setOpaque(true);
					comp.setBackground(UIManager.getDefaults().getColor(
							"List.selectionBackground"));
					comp.setForeground(UIManager.getDefaults().getColor(
							"List.selectionForeground"));
					comp.repaint();
				}
				return comp;
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(list), BorderLayout.CENTER);
	}

	/**
	 * Updates the list.
	 */
	private void updateList() {
		DefaultListModel<MailMessage> model = new DefaultListModel<MailMessage>();
		for (MailMessage mm : mailStorage)
			model.addElement(mm);
		list.setModel(model);
	}

	@Override
	public void storeChanged(MailStorage storage) {
		updateList();
	}

	/**
	 * Returns the currently displayed {@link MailStorage}.
	 * 
	 * @return The currently displayed mail storage.
	 */
	public MailStorage getMailStorage() {
		return mailStorage;
	}

}
