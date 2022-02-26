package de.Ryeera.DragoNoises.Soundboard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.json.JSONException;

import de.Ryeera.libs.JSONUtils;

public class SoundboardGUI extends JFrame {
	
	public static SoundboardPanel soundboardPanel;
	public static OptionPanel    options;
	private JMenuItem            menuItemOpenFolder, menuItemReload;
	
	private static final long serialVersionUID = 3262021165496547142L;

	public SoundboardGUI() {
		super("DragoBoard");
		createAndShowGUI();
	}
	
	private void createAndShowGUI() {
		soundboardPanel = new SoundboardPanel();
		options   = new OptionPanel();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				close();
			}
		});
		setJMenuBar(createMenuBar());
		setLayout(new BorderLayout());
		add(options, BorderLayout.LINE_END);
		add(soundboardPanel, BorderLayout.CENTER);
		setResizable(false);
		setIconImage(Soundboard.IMAGE);
		setSize(1452, 875);
		setVisible(true);
		addMouseListener(soundboardPanel);
		addMouseMotionListener(soundboardPanel);
		addMouseWheelListener(soundboardPanel);
		addKeyListener(soundboardPanel);
		requestFocusInWindow();
	}
	
	private void close() {
		SoundboardTray.soundboardGUI = null;
		dispose();
	}
	
	public static void reload() {
		try {
			Soundboard.reloadConfig();
			Soundboard.reloadSoundboard();
		} catch (JSONException | IOException e) {
			JOptionPane.showMessageDialog(SoundboardTray.soundboardGUI, "The file " + Soundboard.configFile.getAbsolutePath() + " is not in the right format!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void save() {
		try {
			JSONUtils.writeJSON(Soundboard.soundboard, Soundboard.soundboardFile);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(SoundboardTray.soundboardGUI, "The file could not be saved! Make sure you have permission to write to " + Soundboard.configFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar          mb                  = new JMenuBar();
		JMenu             menuFile            = new JMenu("File");
		JMenuItem         menuItemSave        = new JMenuItem("Save", KeyEvent.VK_S);
		JMenuItem         menuItemExit        = new JMenuItem("Exit", KeyEvent.VK_E);
		JMenu             menuView            = new JMenu("View");
		JCheckBoxMenuItem menuItemShowCoords  = new JCheckBoxMenuItem("Show Coordinates", false);
		JMenu             menuHelp            = new JMenu("Help");
		JMenuItem         menuItemAbout       = new JMenuItem("About", KeyEvent.VK_A);
		menuItemOpenFolder = new JMenuItem("Open Containing Folder", KeyEvent.VK_F);
		menuItemReload     = new JMenuItem("Reload from Disk", KeyEvent.VK_R);
		
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuView.setMnemonic(KeyEvent.VK_V);
		menuHelp.setMnemonic(KeyEvent.VK_H);
		
		menuItemOpenFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		menuItemReload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		menuItemShowCoords.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		
		menuItemOpenFolder.setEnabled(true);
		menuItemReload.setEnabled(true);
		
		menuFile.add(menuItemOpenFolder);
		menuFile.add(menuItemReload);
		menuFile.addSeparator();
		menuFile.add(menuItemSave);
		menuFile.addSeparator();
		menuFile.add(menuItemExit);
		mb.add(menuFile);
		menuView.add(menuItemShowCoords);
		mb.add(menuView);
		menuHelp.add(menuItemAbout);
		mb.add(menuHelp);
		
		menuItemOpenFolder.addActionListener(e -> {
			try {
				Runtime.getRuntime().exec("explorer.exe \"" + Soundboard.soundboardFile.getAbsolutePath() + "\"");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		menuItemReload.addActionListener(e -> reload());
		menuItemSave.addActionListener(e -> save());
		menuItemExit.addActionListener(e -> close());
		menuItemAbout.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Launchpad Soundboard v" + Soundboard.VERSION + " by Ryeera", "About", JOptionPane.INFORMATION_MESSAGE, Soundboard.ICON);
		});
		return mb;
	}
	
}
