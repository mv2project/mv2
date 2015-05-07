package de.iss.mv2.client.gui;

import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import de.iss.mv2.MV2Constants;
import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.io.PathBuilder;

/**
 * Represents a window to select the path to store the client data.
 * 
 * @author Marcel Singer
 *
 */
public class StoreLocationSelector extends JFrame implements ActionListener,
		DocumentListener, ComponentListener {

	/**
	 * Holds the dialog result.
	 */
	private int dialogResult = MV2Constants.CANCEL_OPTION;
	
	/**
	 * A list holding all the listeners to notify when a selection was made.
	 */
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	
	/**
	 * Creates a new instance of {@link StoreLocationSelector}.
	 */
	public StoreLocationSelector() {
		getContentPane().setLayout(
				new MigLayout("", "[][grow][][]", "[][][26.00][28.00][]"));

		JTextArea txtrPleaseSelectThe = new JTextArea();
		txtrPleaseSelectThe.setEditable(false);
		txtrPleaseSelectThe
				.setText("Please select the location where your client-data is or should be stored. If the system can't find a valid configuration file at the given path a new one will be created.");
		txtrPleaseSelectThe.setRows(3);
		txtrPleaseSelectThe.setLineWrap(true);
		txtrPleaseSelectThe.setWrapStyleWord(true);
		txtrPleaseSelectThe.setBackground(SystemColor.window);
		getContentPane().add(txtrPleaseSelectThe, "cell 0 0 4 1,grow");

		JLabel lblNewLabel = new JLabel("Path:");
		getContentPane().add(lblNewLabel, "cell 0 2,alignx right");

		pathField = new JTextField();
		pathField.getDocument().addDocumentListener(this);
		getContentPane().add(pathField, "cell 1 2 2 1,growx");
		pathField.setColumns(5);

		searchPathButton = new JButton("Search");
		getContentPane().add(searchPathButton, "cell 3 2,growx");

		stateLabel = new JLabel("No path given...");
		getContentPane().add(stateLabel, "cell 1 3 2 1");

		submitButton = new JButton("Submit");
		getContentPane().add(submitButton, "cell 2 4");

		cancelButton = new JButton("Cancel");
		getContentPane().add(cancelButton, "cell 3 4");

		searchPathButton.addActionListener(this);

		setPreferredSize(new Dimension(515, 217));
		setSize(515, 217);

		cancelButton.addActionListener(this);
		submitButton.addActionListener(this);
		addComponentListener(this);
	}

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -4568963074718234101L;
	/**
	 * The text field containing the file path.
	 */
	private JTextField pathField;
	/**
	 * The label displaying the information about the given path.
	 */
	private JLabel stateLabel;
	/**
	 * The button that should open a file selector.
	 */
	private JButton searchPathButton;
	/**
	 * The button that should cancel this dialog.
	 */
	private JButton cancelButton;
	/**
	 * The button that should commit this dialog.
	 */
	private JButton submitButton;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == searchPathButton) {
			JFileChooser jfc = new JFileChooser();
			jfc.setMultiSelectionEnabled(false);
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				pathField.setText(jfc.getSelectedFile().getAbsolutePath());
			}
		}
		if(e.getSource() == cancelButton){
			dialogResult = MV2Constants.CANCEL_OPTION;
			notifyListeners();
			setVisible(false);
		}
		if(e.getSource() == submitButton){
			File f = getSelectedDirectory();
			if(!f.exists() || !f.isDirectory()){
				DialogHelper.showErrorMessage(this, "Invalid Path", "The provided path is invalid and can not be used.");
				return;
			}
			dialogResult = MV2Constants.SUBMIT_OPTION;
			notifyListeners();
			setVisible(false);
		}
	}

	/**
	 * Handles the change of the entered path.
	 */
	private void pathChanged() {
		String path = pathField.getText();
		File f = new File(path);
		stateLabel.setText("");
		if (!f.exists()) {
			stateLabel.setText("The provided path is invalid.");
			return;
		}
		if (!f.isDirectory()) {
			stateLabel.setText("The provided path is not a directory.");
			return;
		}
		try {
			PathBuilder pb = new PathBuilder(f);
			if (pb.getChildFile(MV2Constants.CLIENT_CONFIG_FILE_NAME).exists()) {
				stateLabel.setText("An existing configuration was found.");
			} else {
				stateLabel
						.setText("No configuration found. A new one will be created...");
			}
		} catch (FileNotFoundException e) {
			// Will never occur because of previous checks.
		}
	}
	
	/**
	 * Notifies all listeners that a selection was made.
	 */
	private void notifyListeners(){
		ActionEvent e = new ActionEvent(this, 0, "");
		for(ActionListener l : listeners) l.actionPerformed(e);
	}
	
	/**
	 * Adds a listener to be notified when a selection was made.
	 * @param listener The listener to be notified.
	 */
	public void addActionListener(ActionListener listener){
		if(listener == null) return;
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	/**
	 * Returns the result of this dialog.
	 * @return The result of this dialog. Possible values are {@link MV2Constants#SUBMIT_OPTION} and {@link MV2Constants#CANCEL_OPTION}.
	 */
	public int getDialogResult(){
		return dialogResult;
	}

	/**
	 * Returns the selected directory.
	 * @return The selected directory.
	 */
	public File getSelectedDirectory(){
		return new File(pathField.getText());
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		pathChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		pathChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		pathChanged();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		getContentPane().revalidate();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

}
