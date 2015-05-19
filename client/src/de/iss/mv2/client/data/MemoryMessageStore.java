package de.iss.mv2.client.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.SwingUtilities;

/**
 * A {@link MailStorage} that stores incoming mails inside the file system.
 * 
 * @author Marcel Singer
 */
public class MemoryMessageStore extends TreeSet<MailMessage> implements
		MailStorage {

	/**
	 * Creates a new instance of {@link MemoryMessageStore}.
	 */
	public MemoryMessageStore(){
		super(new Comparator<MailMessage>() {

			@Override
			public int compare(MailMessage o1, MailMessage o2) {
				return -1 * o1.getTimestamp().compareTo(o2.getTimestamp());
			}
		});
	}
	
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
