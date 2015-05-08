package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A control to display an assistant.
 * @author Marcel Singer
 *
 */
public class AssistantControl extends JComponent {

	
	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -3804290094695398039L;

	/**
	 * Holds the steps of this assistant.
	 */
	private final JComponent[] steps;
	
	/**
	 * The current step index;
	 */
	private int stepIndex = 0;
	
	/**
	 * The button to go to the next step.
	 */
	private final JButton nextButton = new JButton("Next >");
	/**
	 * The button to go to the last step.
	 */
	private final JButton backButton = new JButton("< Back");
	/**
	 * The button to cancel this assistant.
	 */
	private final JButton cancelButton = new JButton("Cancel");
	/**
	 * The button to complete this assistant.
	 */
	private final JButton finishButton = new JButton("Finish");
	
	/**
	 * Creates a new instance of {@link AssistantControl}.
	 * @param steps An array with the controls representing each step in this assistant.
	 */
	public AssistantControl(JComponent[] steps) {
		this.steps = steps;
		setLayout(new BorderLayout());
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(backButton);
		buttonsPanel.add(nextButton);
		buttonsPanel.add(finishButton);
		add(buttonsPanel, BorderLayout.SOUTH);
		setStep(0);
	}
	
	/**
	 * Sets the current step of this assistant.
	 * @param index The number of the step to set.
	 */
	private void setStep(int index){
		JComponent step = steps[index];
		if(index == 0) backButton.setEnabled(false); else backButton.setEnabled(true);
		if(index == steps.length-1){
			nextButton.setEnabled(false);
			finishButton.setEnabled(true);
		}else{
			nextButton.setEnabled(true);
			finishButton.setEnabled(false);
		}
		//TODO: Complete here!
	}
	
	

}
