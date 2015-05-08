package de.iss.mv2.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * A control to decorate a given component with a submit and cancel button.
 * 
 * @author Marcel Singer
 * @param <T>
 *            The type of the control to decorate.
 *
 */
public class SubmitControl<T extends JComponent> extends JComponent implements
		ActionListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -8522835252329324454L;

	/**
	 * Holds the listeners to notify when this component is either submitted or
	 * canceled.
	 */
	private Set<SubmitListener> listeners = new HashSet<SubmitListener>();

	/**
	 * Holds the decorated component.
	 */
	private final T innerComponent;

	/**
	 * Holds the button that will cancel this component.
	 */
	private JButton submitButton;
	/**
	 * Holds the button that will cancel this component.
	 */
	private JButton cancelButton;

	/**
	 * Creates a new instance of {@link SubmitControl}.
	 * 
	 * @param component
	 *            The component to decorate.
	 */
	public SubmitControl(T component) {
		this.innerComponent = component;
		innerComponent.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), innerComponent.getBorder()));
		setLayout(new BorderLayout());
		add(component, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JSeparator separator = new JSeparator();
		panel.add(separator, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		panel_1.add(submitButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		panel_1.add(cancelButton);

	}

	/**
	 * Returns the decorated component.
	 * 
	 * @return The decorated component.
	 */
	public T getInnerComponent() {
		return innerComponent;
	}

	/**
	 * Adds a listener to be notified when this control is either submitted or
	 * canceled.
	 * 
	 * @param listener
	 *            The listener to add. If this parameter is {@code null} or
	 *            already present no exception will be thrown.
	 */
	public void addSubmitListener(SubmitListener listener) {
		if (listener == null)
			return;
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton) {
			for (SubmitListener l : listeners)
				l.submitted(this);
		}
		if (e.getSource() == cancelButton) {
			for (SubmitListener l : listeners)
				l.canceled(this);
		}
	}

	/**
	 * Creates a {@link SubmitControl} with the given component.
	 * 
	 * @param component
	 *            The component to be wrapped.
	 * @param listener
	 *            The listener to be notified when the component is either
	 *            submitted or canceled.
	 * @return The wrapped component.
	 */
	public static SubmitControl<JComponent> wrap(JComponent component,
			SubmitListener listener) {
		SubmitControl<JComponent> result = new SubmitControl<JComponent>(
				component);
		result.addSubmitListener(listener);
		return result;
	}

}
