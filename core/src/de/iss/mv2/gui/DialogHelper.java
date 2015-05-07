package de.iss.mv2.gui;

import java.awt.BorderLayout;
import java.awt.Component;

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

}
