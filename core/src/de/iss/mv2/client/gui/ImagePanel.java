package de.iss.mv2.client.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * A control to display an image.
 * @author Marcel Singer
 *
 */
public class ImagePanel extends JPanel {
	
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 8305526533004089868L;
	/**
	 * The image that is or should be displayed.
	 */
	private Image image;

	/**
	 * Creates a new image panel.
	 */
	public ImagePanel() {
	}

	/**
	 * Sets the image that should be displayed and repaints this control.
	 * @param image The image to set. 
	 */
	public void setImage(Image image) {
		this.image = image;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

}