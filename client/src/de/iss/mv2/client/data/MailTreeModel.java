package de.iss.mv2.client.data;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A tree model to display connected web spaces.
 * @author Marcel Singer
 *
 */
public class MailTreeModel implements TreeModel {

	/**
	 * Holds the client settings to display.
	 */
	private final MV2ClientSettings clientSettings;
	
	/**
	 * Creates a new instance of {@link MailTreeModel}.
	 * @param settings The client settings to use.
	 */
	public MailTreeModel(MV2ClientSettings settings) {
		this.clientSettings = settings;
	}

	@Override
	public Object getRoot() {
		return clientSettings;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent == clientSettings){
			return clientSettings.getMailBoxes().get(index);
		}else{
			switch (index) {
			case 0:
				return "Inbox";
			case 1:
				return "Sent";
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if(parent == clientSettings) return clientSettings.getMailBoxes().size(); else return 2;
	}

	@Override
	public boolean isLeaf(Object node) {
		if(String.class.isAssignableFrom(node.getClass())) return true;
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if(parent == clientSettings) return clientSettings.getMailBoxes().indexOf(child);
		if(child.equals("Inbox")) return 0;
		if(child.equals("Sent")) return 1;
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		
	}

}
