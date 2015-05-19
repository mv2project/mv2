package de.iss.mv2.client.gui;

import javax.swing.JTree;

import de.iss.mv2.client.data.MV2ClientSettings;
import de.iss.mv2.client.data.MailTreeModel;

/**
 * A component displaying the available mail boxes.
 * 
 * @author Marcel Singer
 *
 */
public class MailBoxSelector extends JTree {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -3856020966297337240L;

	/**
	 * Creates a new instance of {@link MailBoxSelector}.
	 */
	public MailBoxSelector() {
		setRowHeight(40);
		setModel(new MailTreeModel(MV2ClientSettings.getRuntimeSettings()));
		setRootVisible(false);
		setCellRenderer(new MailBoxCellRenderer());
	}

}
