package de.iss.mv2.gui;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * A dialog with a submit and cancel button.
 * 
 * @author Marcel Singer
 * @param <T>
 *            The type of the displayed control.
 *
 */
public class SubmitDialog<T extends JComponent> extends JDialog implements
		SubmitListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = 7079901700698316446L;

	/**
	 * A set containing the listeners to be notified when this dialog is
	 * submitted or canceled.
	 */
	private final Set<SubmitListener> listeners = new HashSet<SubmitListener>();

	/**
	 * Holds the displayed content control.
	 */
	private final SubmitControl<T> contentControl;

	/**
	 * Creates a new instance of {@link SubmitDialog}.
	 * 
	 * @param parent
	 *            The parent frame of this dialog.
	 * @param contentControl
	 *            The control to be displayed inside this dialog.
	 * @param title
	 *            The title of this dialog.
	 * @param blocking
	 *            {@code true} if the calling thread should wait for this dialog
	 *            to close.
	 */
	public SubmitDialog(JFrame parent, SubmitControl<T> contentControl,
			String title, boolean blocking) {
		super(parent, title, blocking);
		setLayout(new BorderLayout());
		getContentPane().add(contentControl, BorderLayout.CENTER);
		contentControl.addSubmitListener(this);
		this.contentControl = contentControl;
		setLocationRelativeTo(parent);
	}

	/**
	 * Creates a new instance of {@link SubmitDialog}.
	 * 
	 * @param parent
	 *            The parent frame of this dialog.
	 * @param contentControl
	 *            The control to be displayed inside this dialog.
	 * @param title
	 *            The title of this dialog.
	 * @param blocking
	 *            {@code true} if the calling thread should wait for this dialog
	 *            to close.
	 */
	public SubmitDialog(JDialog parent, SubmitControl<T> contentControl,
			String title, boolean blocking) {
		super(parent, title, blocking);
		setLayout(new BorderLayout());
		getContentPane().add(contentControl, BorderLayout.CENTER);
		contentControl.addSubmitListener(this);
		this.contentControl = contentControl;
		setLocationRelativeTo(parent);
	}

	/**
	 * Creates a new instance of {@link SubmitDialog}.
	 * 
	 * @param parent
	 *            The parent frame of this dialog.
	 * @param contentControl
	 *            The control to be displayed inside this dialog.
	 * @param title
	 *            The title of this dialog.
	 * @param blocking
	 *            {@code true} if the calling thread should wait for this dialog
	 *            to close.
	 */
	public SubmitDialog(JFrame parent, T contentControl, String title,
			boolean blocking) {
		this(parent, new SubmitControl<T>(contentControl), title, blocking);
	}

	/**
	 * Creates a new instance of {@link SubmitDialog}.
	 * 
	 * @param parent
	 *            The parent frame of this dialog.
	 * @param contentControl
	 *            The control to be displayed inside this dialog.
	 * @param title
	 *            The title of this dialog.
	 * @param blocking
	 *            {@code true} if the calling thread should wait for this dialog
	 *            to close.
	 */
	public SubmitDialog(JDialog parent, T contentControl, String title,
			boolean blocking) {
		this(parent, new SubmitControl<T>(contentControl), title, blocking);
	}
	
	

	/**
	 * Adds a listener to be notified when this dialog gets submitted or
	 * canceled.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addSubmitListener(SubmitListener listener) {
		if (listener == null)
			return;
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * Returns the component displayed by this dialog.
	 * 
	 * @return The component displayed by this dialog.
	 */
	public T getInnerComponent() {
		return contentControl.getInnerComponent();
	}

	@Override
	public void submitted(Object sender) {
		for (SubmitListener l : listeners)
			l.submitted(this);
		setVisible(false);
	}

	@Override
	public void canceled(Object sender) {
		for (SubmitListener l : listeners)
			l.canceled(this);
		setVisible(false);
	}

}
