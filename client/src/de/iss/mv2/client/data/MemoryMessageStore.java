package de.iss.mv2.client.data;

import java.util.ArrayList;

/**
 * A {@link MailStorage} that stores incoming mails inside the file system. 
 * @author Marcel Singer
 */
public class MemoryMessageStore extends ArrayList<MailMessage> implements MailStorage {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -1778320419766452546L;

	@Override
	public MailMessage getNewest() {
		MailMessage result = null;
		for(MailMessage m : this){
			if(result == null){
				result = m;
				continue;
			}
			if(m.getTimestamp().getTime() > result.getTimestamp().getTime()) result = m;
		}
		return result;
	}

	
	
}
