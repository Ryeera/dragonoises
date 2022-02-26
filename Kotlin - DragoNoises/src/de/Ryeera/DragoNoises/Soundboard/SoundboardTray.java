package de.Ryeera.DragoNoises.Soundboard;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.JOptionPane;

import de.Ryeera.DragoNoises.MessageType;
import de.Ryeera.DragoNoises.Utils;
import de.Ryeera.DragoNoises.AnimationCreator.AnimationCreator;

public class SoundboardTray {
	
	public static SoundboardGUI          soundboardGUI = null;
	public static AnimationCreator       animationGUI  = null;
	
	public static final TrayIcon        trayIcon       = new TrayIcon(Utils.createImage("/de/Ryeera/DragoNoises/Resources/LaunchpadIcon.png"), "DragoBoard");
	
	private static void openSoundboardGUI() {
		if (soundboardGUI == null) {
			soundboardGUI = new SoundboardGUI();
			soundboardGUI.setEnabled(true);
			soundboardGUI.setVisible(true);
		} else
			soundboardGUI.requestFocus();
	}
	
	private static void openAnimationGUI() {
		if (animationGUI == null) {
			animationGUI = new AnimationCreator();
			AnimationCreator.gui.setEnabled(true);
			AnimationCreator.gui.setVisible(true);
		} else {
			AnimationCreator.gui.requestFocus();
		}
	}
	
	public static void triggerMessage(MessageType type, String message) {
		if (type == MessageType.INFO) {
			Soundboard.log("TrayIcon", LogLevel.INFO, message);
			trayIcon.displayMessage("Launchpad Soundboard", message, TrayIcon.MessageType.INFO);
		} else if (type == MessageType.WARNING) {
			Soundboard.log("TrayIcon", LogLevel.WARNING, message);
			trayIcon.displayMessage("Launchpad Soundboard", message, TrayIcon.MessageType.WARNING);
		} else if (type == MessageType.ERROR) {
			Soundboard.log("TrayIcon", LogLevel.ERROR, message);
			trayIcon.displayMessage("Launchpad Soundboard", message, TrayIcon.MessageType.ERROR);
		}
	}
	
	public void reloadPopup() {
		PopupMenu popup = new PopupMenu();
		Soundboard.log("TrayIcon", LogLevel.DEBUG, "Creating menu...");
		MenuItem         aboutItem            = new MenuItem("About");
		CheckboxMenuItem debugItem            = new CheckboxMenuItem("Debug Mode");
		debugItem.setState(Soundboard.config.getBoolean("debug"));
		debugItem.addItemListener(e -> {
			triggerMessage(MessageType.INFO, "Debug-Mode " + (debugItem.getState() ? "en" : "dis") + "abled!");
			Soundboard.config.put("debug", debugItem.getState());
			Soundboard.debug = debugItem.getState();
			Soundboard.saveConfig();
		});
		Menu			 audioMenu = new Menu("Playback Device");
		MenuItem		 animationItem = new MenuItem("Open Animation Creator");
		MenuItem         exitItem    = new MenuItem("Exit");
		
		animationItem.addActionListener(e -> {
			openAnimationGUI();
		});
		exitItem.addActionListener(a -> {
			Soundboard.exit();
		});
		
		aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "DragoBoard v" + Soundboard.VERSION + "\nby Ryeera\n\nMitten is gay", "About", JOptionPane.INFORMATION_MESSAGE, Soundboard.ICON));
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfo.length; i++) {
			Mixer.Info info = mixerInfo[i];
			if(info.getDescription().contains("Playback")) {
				CheckboxMenuItem audioItem = new CheckboxMenuItem(info.getName());
				audioItem.addItemListener(e -> {
					Soundboard.config.put("audio", audioItem.getLabel());
					Soundboard.saveConfig();
					Soundboard.reloadAudioSystem();
					for(int j = 0; j < audioMenu.getItemCount(); j++)
						((CheckboxMenuItem) audioMenu.getItem(j)).setState(false);
					audioItem.setState(true);
				});
				if(Soundboard.config.getString("audio").equalsIgnoreCase(info.getName())) audioItem.setState(true);
				audioMenu.add(audioItem);
			}
		}
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(debugItem);
		popup.addSeparator();
		popup.add(audioMenu);
		popup.add(animationItem);
		popup.add(exitItem);
		trayIcon.setPopupMenu(popup);
	}
	
	public void createAndShowIcon() {
		SystemTray tray = SystemTray.getSystemTray();
		reloadPopup();
		Soundboard.log("TrayIcon", LogLevel.DEBUG, "Setting trayicon properties...");
		trayIcon.setImageAutoSize(true);
		try {
			Soundboard.log("TrayIcon", LogLevel.DEBUG, "Adding trayicon...");
			tray.add(trayIcon);
		} catch (AWTException e) {
			Soundboard.log("TrayIcon", LogLevel.ERROR, "TrayIcon could not be added.");
			Soundboard.exit();
		}
		Soundboard.log("TrayIcon", LogLevel.DEBUG, "Adding listeners...");
		trayIcon.addActionListener(e -> openSoundboardGUI());
		Soundboard.log("TrayIcon", LogLevel.DEBUG, "Trayicon created!");
	}
}
