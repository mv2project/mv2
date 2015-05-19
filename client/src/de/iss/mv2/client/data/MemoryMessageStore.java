package de.iss.mv2.client.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * A {@link MailStorage} that stores incoming mails inside the file system.
 * 
 * @author Marcel Singer
 */
public class MemoryMessageStore extends ArrayList<MailMessage> implements
		MailStorage {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -1778320419766452546L;

	/**
	 * Holds the listeners.
	 */
	private final List<MailStorageListener> listeners = new ArrayList<MailStorageListener>();

	@Override
	public MailMessage getNewest() {
		MailMessage result = null;
		for (MailMessage m : this) {
			if (result == null) {
				result = m;
				continue;
			}
			if (m.getTimestamp().getTime() > result.getTimestamp().getTime())
				result = m;
		}
		return result;
	}

	@Override
	public void markChanged() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				for (MailStorageListener al : listeners) {
					al.storeChanged(MemoryMessageStore.this);
				}
			}
		});
	}

	@Override
	public void addChangeListener(MailStorageListener listener) {
		listeners.add(listener);
	}

}
