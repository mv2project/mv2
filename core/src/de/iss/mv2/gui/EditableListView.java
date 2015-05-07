package de.iss.mv2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A component to display an editable list.
 * 
 * @author Marcel Singer
 *
 * @param <T>
 *            The type of the elements to display.
 */
public class EditableListView<T> extends JComponent implements ActionListener, ListSelectionListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -2296142506233379820L;

	/**
	 * The core list component.
	 */
	private JList<T> list;
	/**
	 * The button to add an item.
	 */
	private JButton addButton;
	/**
	 * The button to remove an item.
	 */
	private JButton removeButton;

	/**
	 * A list containing the listeners to be notified when a user requests to
	 * add or delete an item.
	 */
	private final List<EditableListListener<T>> listeners = new ArrayList<EditableListListener<T>>();
	
	/**
	 * A list containing the listeners to be notified when the current selection changed.
	 */
	private final List<ListSelectionListener> selectionListeners = new ArrayList<ListSelectionListener>();

	/**
	 * Creates a new instance of {@link EditableListView}.
	 */
	public EditableListView() {
		setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		Border b = null;
		addButton = new JButton("+");
		addButton.setForeground(Color.WHITE);
		addButton.setFont(new Font("Lucida Grande", Font.PLAIN, 23));
		addButton.addActionListener(this);
		addButton.setBorder(b);
		buttonPanel.add(addButton);

		removeButton = new JButton("-");
		removeButton.setForeground(Color.WHITE);
		removeButton.setBorder(b);
		removeButton.setFont(addButton.getFont());
		removeButton.addActionListener(this);
		buttonPanel.add(removeButton);

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		list = new JList<T>();
		list.addListSelectionListener(this);
		scrollPane.setViewportView(list);
		updateGUI();
	}

	/**
	 * Updates the state of all elements.
	 */
	private void updateGUI() {
		removeButton.setEnabled(list.getSelectedIndex() != -1);
	}

	/**
	 * Sets the model to display.
	 * 
	 * @param model
	 *            The model to be displayed.
	 */
	public void setListModel(ListModel<T> model) {
		list.setModel(model);
		updateGUI();
	}

	/**
	 * Returns the currently displayed model.
	 * 
	 * @return The currently displayed model.
	 */
	public ListModel<T> getListModel() {
		return list.getModel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			for (EditableListListener<T> l : listeners)
				l.addItem(this);
		}
		if (e.getSource() == removeButton) {
			int selectedIndex = list.getSelectedIndex();
			if (selectedIndex == -1)
				return;
			T selectedItem = list.getSelectedValue();
			for (EditableListListener<T> l : listeners)
				l.removeItem(this, selectedItem, selectedIndex);
		}
	}

	/**
	 * Adds a listener to be notified when the user requests to add or remove an
	 * item.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addListener(EditableListListener<T> listener) {
		if (listener == null)
			return;
		if (!listeners.contains(listener))
			listeners.add(listener);
	}
	
	/**
	 * Adds a listener to be notified when the current selection changed.
	 * @param listener The listener to be notified.
	 */
	public void addSelectionListener(ListSelectionListener listener){
		if(listener == null)return;
		if(!selectionListeners.contains(listener)) selectionListeners.add(listener);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		updateGUI();
		ListSelectionEvent lse = new ListSelectionEvent(this, e.getFirstIndex(), e.getLastIndex(), e.getValueIsAdjusting());
		for(ListSelectionListener l : selectionListeners){
			l.valueChanged(lse);
		}
	}

}
