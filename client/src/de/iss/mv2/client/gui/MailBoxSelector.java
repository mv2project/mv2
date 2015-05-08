package de.iss.mv2.client.gui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("JTree") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				DefaultMutableTreeNode node_1;
				node_1 = new DefaultMutableTreeNode("colors");
				node_1.add(new DefaultMutableTreeNode("blue"));
				node_1.add(new DefaultMutableTreeNode("violet"));
				node_1.add(new DefaultMutableTreeNode("red"));
				node_1.add(new DefaultMutableTreeNode("yellow"));
				add(node_1);
				node_1 = new DefaultMutableTreeNode("sports");
				node_1.add(new DefaultMutableTreeNode("basketball"));
				node_1.add(new DefaultMutableTreeNode("soccer"));
				node_1.add(new DefaultMutableTreeNode("football"));
				node_1.add(new DefaultMutableTreeNode("hockey"));
				add(node_1);
				node_1 = new DefaultMutableTreeNode("food");
				node_1.add(new DefaultMutableTreeNode("hot dogs"));
				node_1.add(new DefaultMutableTreeNode("pizza"));
				node_1.add(new DefaultMutableTreeNode("ravioli"));
				node_1.add(new DefaultMutableTreeNode("bananas"));
				add(node_1);
			}
		}));
		tree.setRootVisible(false);
		tree.setCellRenderer(new MailBoxCellRenderer());
		add(tree, BorderLayout.CENTER);

	}

}
