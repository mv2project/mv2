package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import de.iss.mv2.MV2Constants;
import de.iss.mv2.client.data.MV2ClientSettings;
import de.iss.mv2.client.data.UserPreferences;
import de.iss.mv2.data.EncryptedExportable;
import de.iss.mv2.io.PathBuilder;
import de.iss.mv2.logging.LoggerManager;

/**
 * The main window of the client application.
 * 
 * @author Marcel Singer
 *
 */
public class ClientMainWindow extends JFrame implements WindowListener,
		ActionListener {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 931571183140137367L;

	/**
	 * The menu item to open the client settings.
	 */
	private JMenuItem settingsButton;
	
	/**
	 * The menu item to open the certificate signing tool.
	 */
	private JMenuItem certSigningToolItem;

	/**
	 * Creates a new instance of {@link ClientMainWindow}.
	 */
	public ClientMainWindow() {
		setLocationRelativeTo(null);
		setTitle("MV2 Client");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));
		addWindowListener(this);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("MV2");
		menuBar.add(mnNewMenu);
		Toolkit tk = Toolkit.getDefaultToolkit();

		JMenu toolsMenu = new JMenu("Tools");
		certSigningToolItem = new JMenuItem("Certificate Signing Tool");
		certSigningToolItem.addActionListener(this);
		toolsMenu.add(certSigningToolItem);
		menuBar.add(toolsMenu);
		
		settingsButton = new JMenuItem("Settings");
		settingsButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,
				tk.getMenuShortcutKeyMask()));
		settingsButton.addActionListener(this);
		mnNewMenu.add(settingsButton);

		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		MailBoxSelector panel = new MailBoxSelector();
		splitPane.setLeftComponent(new JScrollPane(panel));
		splitPane.setRightComponent(null);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try{
			PathBuilder pb = new PathBuilder(new File(UserPreferences.getPreferences().getStoreAddress("")));
			File f = pb.getChildFile(MV2Constants.CLIENT_CONFIG_FILE_NAME);
			EncryptedExportable ee = new EncryptedExportable(MV2ClientSettings.getRuntimeSettings());
			OutputStream out = new FileOutputStream(f);
			ee.export(MV2ClientSettings.getRuntimeSettings().getPassphrase(), out);
			out.flush();
			out.close();
			MV2ClientSettings.getRuntimeSettings().saveExtras(f);
		}catch(Exception ex){
			LoggerManager.getCurrentLogger().push(ex);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == settingsButton){
			ClientSettingsWindow csw = new ClientSettingsWindow();
			csw.setLocationRelativeTo(this);
			csw.setVisible(true);
		}
		if(e.getSource() == certSigningToolItem){
			CertificateSigningTool cst = new CertificateSigningTool();
			cst.setLocationRelativeTo(this);
			cst.setVisible(true);
		}
	}

}
