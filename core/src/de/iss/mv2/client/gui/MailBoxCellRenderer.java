package de.iss.mv2.client.gui;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * A cell renderer for rendering the items inside the {@link MailBoxSelector}
 * control.
 * 
 * @author Marcel Singer
 *
 */
public class MailBoxCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -4762237006500697808L;

	@Override
	public Icon getOpenIcon() {
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream(
					"mailIcon.png");
			Icon ic = new ImageIcon(ImageIO.read(in));
			in.close();
			return ic;
		} catch (IOException ex) {
			return super.getIcon();
		}
	}
	
	@Override
	public Icon getClosedIcon() {
		return getOpenIcon();
	}
	
	@Override
	public Icon getLeafIcon() {
		try{
			InputStream in = getClass().getClassLoader().getResourceAsStream(
					"folderItem.png");
			Icon ic = new ImageIcon(ImageIO.read(in));
			in.close();
			return ic;
		}catch(IOException ex){
			return super.getLeafIcon();
		}
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		Component cmp = super.getTreeCellRendererComponent(tree, value, sel,
				expanded, leaf, row, hasFocus);
		return cmp;
	}

}
