package de.iss.mv2.client.gui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JTree;

import de.iss.mv2.client.data.MV2ClientSettings;
import de.iss.mv2.client.data.MailTreeModel;

/**
 * A component displaying the available mail boxes.
 * 
 * @author Marcel Singer
 *
 */
public class MailBoxSelector extends JComponent {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -3856020966297337240L;

	/**
	 * Creates a new instance of {@link MailBoxSelector}.
	 */
	public MailBoxSelector() {
		setLayout(new BorderLayout(0, 0));

		JTree tree = new JTree();
		tree.setRowHeight(40);
		tree.setModel(new MailTreeModel(MV2ClientSettings.getRuntimeSettings()));
		tree.setRootVisible(false);
		tree.setCellRenderer(new MailBoxCellRenderer());
		add(tree, BorderLayout.CENTER);

	}

}
