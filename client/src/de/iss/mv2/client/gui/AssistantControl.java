package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A control to display an assistant.
 * 
 * @author Marcel Singer
 *
 */
public class AssistantControl extends JComponent implements ActionListener {

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
	private int stepIndex = -1;

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
	 * 
	 * @param steps
	 *            An array with the controls representing each step in this
	 *            assistant.
	 */
	public AssistantControl(JComponent[] steps) {
		this.steps = steps;
		nextButton.addActionListener(this);
		cancelButton.addActionListener(this);
		backButton.addActionListener(this);
		finishButton.addActionListener(this);
		setLayout(new BorderLayout());
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(backButton);
		buttonsPanel.add(nextButton);
		buttonsPanel.add(finishButton);
		add(buttonsPanel, BorderLayout.SOUTH);
		nextStep();
	}

	/**
	 * Navigates to the next step.
	 * 
	 * @return {@code true} if the action was successful.
	 */
	public boolean nextStep() {
		JComponent currentStep = null;
		if (stepIndex != -1)
			currentStep = steps[stepIndex];
		if (!canProceed(stepIndex)) {
			return false;
		}
		stepIndex++;
		JComponent nextStep = steps[stepIndex];
		nextButton.setEnabled(stepIndex != steps.length - 1);
		finishButton.setEnabled(!nextButton.isEnabled());
		backButton.setEnabled(canGoBack(stepIndex));

		if (currentStep != null)
			remove(currentStep);
		add(nextStep, BorderLayout.CENTER);
		repaint();
		revalidate();
		return true;
	}

	/**
	 * Tests if this assistant can be completed.
	 * 
	 * @return {@code true} if this assistant ca be completed.
	 */
	public boolean canFinish() {
		if (stepIndex != steps.length - 1)
			return false;
		if (stepIndex >= 0) {
			if (!AssistantStep.class.isAssignableFrom(steps[stepIndex]
					.getClass()))
				return ((AssistantStep) steps[stepIndex]).canProceed();
		}
		return true;
	}

	/**
	 * Navigates to the previous step.
	 * 
	 * @return {@code true} if the action was successful.
	 */
	public boolean goBack() {
		JComponent currentStep = null;
		if (stepIndex != -1)
			currentStep = steps[stepIndex];
		if (!canGoBack(stepIndex))
			return false;
		stepIndex--;
		JComponent lastStep = steps[stepIndex];
		nextButton.setEnabled(true);
		finishButton.setEnabled(false);
		backButton.setEnabled(canGoBack(stepIndex));

		if (currentStep != null)
			remove(currentStep);
		add(lastStep, BorderLayout.CENTER);
		repaint();
		revalidate();
		return true;
	}

	/**
	 * Tests if this assistant can proceed from the given step.
	 * 
	 * @param index
	 *            The index of the step to proceed from.
	 * @return {@code true} if this assistant can proceed to the next step.
	 */
	public boolean canProceed(int index) {
		if (index >= steps.length - 1)
			return false;
		if (index < 0)
			return true;
		JComponent step = steps[index];
		if (AssistantStep.class.isAssignableFrom(step.getClass())) {
			return ((AssistantStep) step).canProceed();
		}
		return true;
	}

	/**
	 * Tests if this assistant can go back to the previous step.
	 * 
	 * @param index
	 *            The index of the current step.
	 * @return {@code true} if the assistant can go back to the previous step.
	 */
	public boolean canGoBack(int index) {
		if (index <= 0)
			return false;
		JComponent lastStep = steps[index - 1];
		if (AssistantStep.class.isAssignableFrom(lastStep.getClass())) {
			return ((AssistantStep) lastStep).canGoBack();
		}

		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nextButton)
			nextStep();
		if (e.getSource() == backButton)
			goBack();
		if (e.getSource() == finishButton) {
			JComponent c = steps[stepIndex];
			boolean canFinish = true;
			if (AssistantStep.class.isAssignableFrom(c.getClass())) {
				AssistantStep step = (AssistantStep) c;
				canFinish = step.canProceed();
			}
		}
	}

}
