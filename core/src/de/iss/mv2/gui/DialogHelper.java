package de.iss.mv2.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Stellt eine Hilfsklasse dar, die Dialoge zu Fehlern anzeigt.
 * 
 * @author singer
 *
 */
public class DialogHelper {

	/**
	 * Zeigt eine Fehlermeldung, falls eine gewünschte Aktion nicht erfolgreich
	 * abgeschlossen werden konnte.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param ex
	 *            Die entstandene Exception.
	 */
	public static void showActionFailedWithExceptionMessage(
			final Component parent, final Exception ex) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane
						.showMessageDialog(
								parent,
								"Die Aktion konnten nicht abgeschlossen werden, da folgender Fehler auftrat:\n<HTML><I>"
										+ ex.getLocalizedMessage()
										+ "</I></HTML>",
								"Aktion fehlgeschlagen",
								JOptionPane.ERROR_MESSAGE);
			}
		});

	}

	/**
	 * Zeigt eine Fehlermeldung, falls eine Eingabe unvollständig oder
	 * fehlerhaft war.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param field
	 *            Das fehlerhafte Feld.
	 */
	public static void showInputError(final Component parent, final String field) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane
						.showMessageDialog(
								parent,
								"<HTML>Ihre Angabe im Feld <B>"
										+ field
										+ "</B> ist entweder ungültig oder unvollständig.</HTML>",
								"Ungültige Angabe", JOptionPane.WARNING_MESSAGE);
			}
		});
	}
	
	/**
	 * Shows a confirm dialog.
	 * @param parent The parent component.
	 * @param title The title of the dialog to display.
	 * @param message The message to display.
	 * @return {@code true} if the user answers with 'yes'.
	 */
	public static boolean showConfirmDialog(Component parent, String title, String message){
		return (JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION);
		
	}

	/**
	 * Zeigt eine Error-Meldung.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param title
	 *            Der Titel der Error-Meldung.
	 * @param text
	 *            Der Text der Error-Meldung.
	 */
	public static void showErrorMessage(final Component parent,
			final String title, final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JOptionPane.showMessageDialog(parent, text, title,
						JOptionPane.ERROR_MESSAGE);
			}
		});

	}

	/**
	 * Zeigt eine Erfolgs-Meldung an. <b>Hinweis:</b> Das Anzeigen des Dialogs
	 * erfolgt auf dem AWT-Thread. Der Aufruf ist threadsicher.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param title
	 *            Gibt den anzuzeigenden Dialog-Titel an.
	 * @param text
	 *            Gibt den anzuzeigenden Dialog-Inhalt an.
	 */
	public static void showSuccessMessage(final Component parent,
			final String title, final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JOptionPane.showMessageDialog(parent, text, title,
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	/**
	 * Displays a window containing the given control.
	 * 
	 * @param parent
	 *            The parent of the window to display.
	 * @param title
	 *            The title of the window to create.
	 * @param content
	 *            The control to be displayed inside the window.
	 */
	public static void showDialog(final Component parent, final String title,
			final Component content) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame fr = new JFrame(title);
				fr.setLocationRelativeTo(parent);
				fr.getContentPane().setLayout(new BorderLayout());
				fr.getContentPane().add(content, BorderLayout.CENTER);
				fr.pack();
				fr.setVisible(true);
			}
		});
	}
	
	/**
	 * Opens a blocking dialog with the specified title.
	 * @param title The title of the dialog to display.
	 * @param parent The parent frame of the dialog to display.
	 * @param component The component to display inside the dialog.
	 * @return The shown dialog.
	 */
	public static JDialog showBlockingDialog(String title, JFrame parent, JComponent component){
		JDialog dial = new JDialog(parent, title, true);
		dial.setLocationRelativeTo(parent);
		dial.getContentPane().setLayout(new BorderLayout());
		dial.getContentPane().add(component, BorderLayout.CENTER);
		dial.pack();
		dial.setVisible(true);
		return dial;
	}
	
	/**
	 * Opens a blocking dialog with the specified title, cancel button and submit button.
	 * @param title The title of the dialog to display.
	 * @param parent The parent frame of the dialog to display.
	 * @param component The component to display inside the dialog.
	 * @return The shown dialog.
	 */
	public static SubmitDialog<JComponent> showBlockingSubmitDialog(String title, JFrame parent, JComponent component){
		SubmitDialog<JComponent> sd = new SubmitDialog<JComponent>(parent, component, title, true);
		sd.pack();
		sd.setVisible(true);
		return sd;
	}
	
	/**
	 * Returns the parent dialog of the given component.
	 * @param comp The component thats parent should be returned.
	 * @return The parent dialog or {@code null} if there is no parent dialog.
	 */
	public static JDialog getParentDialog(JComponent comp){
		if(comp == null) return null;
		Window w = SwingUtilities.getWindowAncestor(comp);
		if(w == null) return null;
		if(JDialog.class.isAssignableFrom(w.getClass())) return (JDialog) w;
		return null;
	}
	
	/**
	 * Returns the parent frame of the given component.
	 * @param comp The component thats parent should be returned.
	 * @return The parent frame or {@code null} if there is no parent frame.
	 */
	public static JFrame getParentFrame(JComponent comp){
		if(comp == null) return null;
		Window w = SwingUtilities.getWindowAncestor(comp);
		if(w == null) return null;
		if(JFrame.class.isAssignableFrom(w.getClass())) return (JFrame) w;
		return null;
	}

}
