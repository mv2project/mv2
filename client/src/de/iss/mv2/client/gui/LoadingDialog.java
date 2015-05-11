package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.iss.mv2.client.messaging.MessageProcedure;
import de.iss.mv2.client.messaging.ProcedureException;
import de.iss.mv2.client.messaging.ProcedureListener;

/**
 * A dialog that displays a loading activity.
 * 
 * @author Marcel Singer
 *
 */
@SuppressWarnings("rawtypes")
public class LoadingDialog extends JDialog implements ProcedureListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -8536427569628064821L;
	/**
	 * Holds the state text field.
	 */
	private JTextArea stateArea;
	/**
	 * Holds the progress bar.
	 */
	private JProgressBar progressBar;

	/**
	 * Creates a new loading dialog with the provided owner.
	 * 
	 * @param owner
	 *            The owner of this dialog.
	 * @param modal
	 *            {@code true} if this dialog is modal.
	 * @wbp.parser.constructor
	 */
	public LoadingDialog(Frame owner, boolean modal) {
		super(owner, modal);
		buildGUI();
	}

	/**
	 * Creates a new loading dialog with the provided owner.
	 * 
	 * @param owner
	 *            The owner of this dialog.
	 * @param modal
	 *            {@code true} if this dialog is modal.
	 */
	public LoadingDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		buildGUI();
	}

	/**
	 * Builds the GUI of this dialog.
	 */
	private void buildGUI() {
		setTitle("Please stand by");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JLabel lblPleaseStandBy = new JLabel(
				"Please stand by while the client trys to communicate with the server...");
		lblPleaseStandBy.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		getContentPane().add(lblPleaseStandBy, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		progressBar.setIndeterminate(true);
		panel.add(progressBar, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		stateArea = new JTextArea();
		scrollPane.setViewportView(stateArea);
	}

	@Override
	public void handleProcedureException(MessageProcedure procedure,
			ProcedureException ex) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(false);
	}

	@Override
	public void procedureCompleted(MessageProcedure procedure, Object result) {
		// TODO Auto-generated method stub
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(false);
	}

	@Override
	public void procedureStateChanged(MessageProcedure procedure, String state,
			int progress) {
		if (progress != 0 && progress != progressBar.getValue()) {
			progressBar.setIndeterminate(false);
			progressBar.setValue(progress);
		}
		if (state != null && !state.isEmpty()) {
			stateArea.setText(state + "\n" + stateArea.getText());
			stateArea.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
		}
	}

}
