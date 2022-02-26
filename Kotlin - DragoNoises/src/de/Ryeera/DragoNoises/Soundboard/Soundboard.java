package de.Ryeera.DragoNoises.Soundboard;

import java.awt.Image;
import java.awt.SystemTray;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.JSONObject;

import de.Ryeera.DragoNoises.Frame;
import de.Ryeera.DragoNoises.Pixel;
import de.Ryeera.DragoNoises.Utils;
import de.Ryeera.libs.JSONUtils;
import io.cassaundra.rocket.Button;
import io.cassaundra.rocket.Color;
import io.cassaundra.rocket.Pad;
import io.cassaundra.rocket.Rocket;

public class Soundboard {

	public static final String VERSION = "0.2";
	public static Rocket launchpad = new Rocket();
	public static final File configFile = new File("config.json");
	public static JSONObject config;
	public static File soundboardFile1 = new File("soundboard1.json");
	public static File soundboardFile2 = new File("soundboard2.json");
	public static File soundboardFile3 = new File("soundboard3.json");
	public static File soundboardFile4 = new File("soundboard4.json");
	public static File soundboardFile5 = new File("soundboard5.json");
	public static File soundboardFile6 = new File("soundboard6.json");
	public static File soundboardFile7 = new File("soundboard7.json");
	public static File soundboardFile8 = new File("soundboard8.json");
	public static File soundboardFile = soundboardFile1;
	public static JSONObject soundboard;
	public static Mixer.Info mixer;
	public static List<AudioThread> currentlyPlaying = new ArrayList<>();
	public static Thread watchdog;
	public static boolean running = true;
	public static final Image IMAGE = Utils.createImage("/de/Ryeera/DragoNoises/Resources/LaunchpadIcon.png");
	public static final ImageIcon ICON = Utils.createImageIcon("/de/Ryeera/DragoNoises/Resources/LaunchpadIcon.png");
	public static Button BUTTON_STOP, BUTTON_MUTE, BUTTON_UP, BUTTON_DOWN, BUTTON_LEFT, BUTTON_RIGHT, BUTTON_SESSION, BUTTON_USER1, BUTTON_USER2, BUTTON_MIXER;
	public static int currentSoundboard = 1;
	public static boolean starting = true, transitioning = true, debug = true;
	public static SoundboardTray tray;
	
	public static void main(String[] args) {
//		if (Soundboard.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ")
//				.replace("/", "\\").substring(1).equalsIgnoreCase(
//						new File(System.getenv("APPDATA") + "/DragoBoard/DragoBoard.jar").getAbsolutePath())) {
			log("Starting...");
			log(LogLevel.DEBUG, "Setting Look and Feel...");
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			log(LogLevel.DEBUG, "Loading Config...");
			try {
				reloadConfig();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
			log(LogLevel.DEBUG, "Initializing Animations...");
			reloadAnimations();
			log(LogLevel.DEBUG, "Initializing Audio-System...");
			reloadAudioSystem();
			log(LogLevel.DEBUG, "Connecting Launchpad...");
			reloadLaunchpad();
			log(LogLevel.DEBUG, "Loading Soundboard...");
			try {
				reloadSoundboard();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log(LogLevel.DEBUG, "Starting Watchdog...");
			watchdog = new Thread(() -> {
				while (running) {
					List<Thread> toRemove = new ArrayList<>();
					for (Thread t : currentlyPlaying)
						if (!t.isAlive())
							toRemove.add(t);
					if (!transitioning) {
						if (currentlyPlaying.size() > 0)
							launchpad.setButton(BUTTON_MUTE, Color.RED);
						else
							launchpad.setButton(BUTTON_MUTE, Color.OFF);
					}
					currentlyPlaying.removeAll(toRemove);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			watchdog.start();
			log(LogLevel.DEBUG, "Creating TrayIcon...");
			tray = new SoundboardTray();
			tray.createAndShowIcon();
			log(LogLevel.INFO, "Soundboard started!");
//		} else {
//			try (PrintWriter out = new PrintWriter(new File(
//					System.getenv("APPDATA") + "/Microsoft/Windows/Start Menu/Programs/Startup/DragoBoard.bat"))) {
//				Utils.copyFile(
//						new File(Soundboard.class.getProtectionDomain().getCodeSource().getLocation().getPath()
//								.replace("%20", " ")),
//						new File(System.getenv("APPDATA") + "/DragoBoard/DragoBoard.jar"));
//				out.println("@ECHO OFF");
//				out.println("c:");
//				out.println("cd " + new File(System.getenv("APPDATA") + "/DragoBoard/").getAbsolutePath());
//				out.println("start javaw -Xmx256m -jar \""
//						+ new File(System.getenv("APPDATA") + "/DragoBoard/DragoBoard.jar").getAbsolutePath() + "\"");
//				out.flush();
//				ResourceHandler rs = new ResourceHandler();
//				rs.copyFileFromResource("config.json");
//				rs.copyFileFromResource("soundboard1.json");
//				rs.copyFileFromResource("soundboard2.json");
//				rs.copyFileFromResource("soundboard3.json");
//				rs.copyFileFromResource("soundboard4.json");
//				rs.copyFileFromResource("soundboard5.json");
//				rs.copyFileFromResource("soundboard6.json");
//				rs.copyFileFromResource("soundboard7.json");
//				rs.copyFileFromResource("soundboard8.json");
//				rs.copyFileFromResource("start.ani");
//				rs.copyFileFromResource("stop.ani");
//				rs.copyFileFromResource("trans1.ani");
//				rs.copyFileFromResource("trans2.ani");
//				JOptionPane.showMessageDialog(null,
//						"DragoBoard was installed successfully! It will from now on start at windows startup.\nYou can now delete this file!",
//						"DragoBoard installer", JOptionPane.INFORMATION_MESSAGE, ICON);
//				Runtime.getRuntime()
//						.exec("cmd /c \"" + new File(System.getenv("APPDATA")
//								+ "/Microsoft/Windows/Start Menu/Programs/Startup/DragoBoard.bat").getAbsolutePath()
//								+ "\"");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public static void applyFrame(Frame frame, Frame current, Frame next) {
		for (Pad pad : Pad.getAll()) {
			Pixel p = frame.getPixel(pad.getX(), pad.getY());
			if(p.isLastFrame())
				p = current.getPixel(pad.getX(), pad.getY());
			else if (p.isNextFrame())
				p = next.getPixel(pad.getX(), pad.getY());
			launchpad.setPad(pad, p.getLaunchpadColor());
		}
		for (Button button : Button.getAll()) {
			Pixel p = button.isTop() ? frame.getPixel(button.getCoord(), 8) : frame.getPixel(8, (button.getCoord() - 7) * -1);
			if(p.isLastFrame())
				launchpad.setButton(button, button.isTop() ? current.getPixel(button.getCoord(), 8).getLaunchpadColor() : current.getPixel(8, (button.getCoord() - 7) * -1).getLaunchpadColor());
			else if (p.isNextFrame())
				launchpad.setButton(button, button.isTop() ? next.getPixel(button.getCoord(), 8).getLaunchpadColor() : next.getPixel(8, (button.getCoord() - 7) * -1).getLaunchpadColor());
			else
				launchpad.setButton(button, p.getLaunchpadColor());
		}
	}
	
	private static void reloadLaunchpad() {
		launchpad.close();
		launchpad = new Rocket();
		launchpad.beginMidiScan();
		launchpad.addListener(new ButtonListener());
	}

	public static void reloadAudioSystem() {
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfo.length; i++) {
			Mixer.Info info = mixerInfo[i];
			if (info.getName().equalsIgnoreCase(config.getString("audio"))) {
				mixer = info;
				return;
			}
		}
	}

	public static void playSound(File soundFile, float volume) {
		AudioThread t = new AudioThread(soundFile, volume);
		t.start();
		currentlyPlaying.add(t);
		launchpad.setButton(BUTTON_MUTE, Color.RED);
	}
	
	public static void reloadConfig() throws IOException {
		config = JSONUtils.readJSON(configFile);
		soundboardFile1 = new File(config.getString("soundboard-file1"));
		soundboardFile2 = new File(config.getString("soundboard-file2"));
		soundboardFile3 = new File(config.getString("soundboard-file3"));
		soundboardFile4 = new File(config.getString("soundboard-file4"));
		soundboardFile5 = new File(config.getString("soundboard-file5"));
		soundboardFile6 = new File(config.getString("soundboard-file6"));
		soundboardFile7 = new File(config.getString("soundboard-file7"));
		soundboardFile8 = new File(config.getString("soundboard-file8"));
		currentSoundboard = config.getInt("current-soundboard");
		debug = config.getBoolean("debug");
	}

	
	public static void reloadSoundboard() throws IOException {
		if(currentSoundboard == 1)
			soundboardFile = soundboardFile1;
		else if(currentSoundboard == 2)
			soundboardFile = soundboardFile2;
		else if(currentSoundboard == 3)
			soundboardFile = soundboardFile3;
		else if(currentSoundboard == 4)
			soundboardFile = soundboardFile4;
		else if(currentSoundboard == 5)
			soundboardFile = soundboardFile5;
		else if(currentSoundboard == 6)
			soundboardFile = soundboardFile6;
		else if(currentSoundboard == 7)
			soundboardFile = soundboardFile7;
		else if(currentSoundboard == 8)
			soundboardFile = soundboardFile8;
		soundboard = JSONUtils.readJSON(soundboardFile);
		if(starting) {
			Animation.START.play();
			starting = false;
		} else {
			Random rnd = new Random();
			int rand = rnd.nextInt(2);
			if(rand == 0)
				Animation.TRANSITION1.play();
			else if(rand == 1)
				Animation.TRANSITION2.play();
		}
		for(Pad pad : Pad.getAll()) {
			JSONObject color = soundboard.getJSONObject(String.valueOf(pad.getX()) + String.valueOf(pad.getY())).getJSONObject("off-color");
			launchpad.setPad(pad, new Color(color.getInt("r"), color.getInt("g"), color.getInt("b")));
		}
		for (Button button : Button.getAll()) {
			if(button.isTop()) {
				if(currentSoundboard == (button.getCoord()+1)) launchpad.setButton(button, new Color(0, 0, 255));
				if(button.getCoord() == 0) {
					BUTTON_UP = button;
				} else if(button.getCoord() == 1) {
					BUTTON_DOWN = button;
				} else if(button.getCoord() == 2) {
					BUTTON_LEFT = button;
				} else if(button.getCoord() == 3) {
					BUTTON_RIGHT = button;
				} else if(button.getCoord() == 4) {
					BUTTON_SESSION = button;
				} else if(button.getCoord() == 5) {
					BUTTON_USER1 = button;
				} else if(button.getCoord() == 6) {
					BUTTON_USER2 = button;
				} else if(button.getCoord() == 7) {
					BUTTON_MIXER = button;
				}
			} else {
				if(button.getCoord() == 0) {
					
				} else if(button.getCoord() == 1) {
					
				} else if(button.getCoord() == 2) {
					
				} else if(button.getCoord() == 3) {
					
				} else if(button.getCoord() == 4) {
					launchpad.setButton(button, new Color(255, 0, 0));
					BUTTON_STOP = button;
				} else if(button.getCoord() == 5) {
					BUTTON_MUTE = button;
				} else if(button.getCoord() == 6) {
					
				} else if(button.getCoord() == 7) {
					
				}
			}
		}
	}
	
	public static void reloadAnimations() {
		log(LogLevel.DEBUG, "Reloading animations...");
		Animation.STOP = Animation.importAnimation(new File("stop.dna"));
		Animation.START = Animation.importAnimation(new File("start.dna"));
		Animation.TRANSITION1 = Animation.importAnimation(new File("trans1.dna"));
		Animation.TRANSITION2 = Animation.importAnimation(new File("trans2.dna"));
	}
	
	public static void saveConfig() {
		log("Saving config...");
		try {
			JSONUtils.writeJSON(config, configFile);
			log("Config saved!");
		} catch (FileNotFoundException e) {
			log("Config failed to save!");
			e.printStackTrace();
		}
	}
	
	public static void exit() {
		log("Shutting down gracefully...");
		log(LogLevel.DEBUG, "Playing shutdown animation...");
		Animation.STOP.play();
		log(LogLevel.DEBUG, "Waiting for all currently playing tracks...");
		for(Thread t : currentlyPlaying) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log(LogLevel.DEBUG, "Waiting for watchdog to stop...");
		running = false;
		try {
			watchdog.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		log(LogLevel.DEBUG, "Closing Launchpad connection...");
		launchpad.close();
		log(LogLevel.DEBUG, "Removing Tray-Icon...");
		SystemTray.getSystemTray().remove(SoundboardTray.trayIcon);
		log("Good Bye!");
		System.exit(0);
	}
	
	public static void switchSoundboard(int soundboard) {
		Soundboard.config.put("current-soundboard", soundboard);
		Soundboard.currentSoundboard = soundboard;
		try {
			Soundboard.reloadSoundboard();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Soundboard.saveConfig();
	}

	public static void log(String message) {
		log(LogLevel.INFO, message);
	}

	public static void log(LogLevel level, String message) {
		log("Main", level, message);
	}
	
	public static void log(String module, LogLevel level, String message) {
		if (level == LogLevel.ERROR)
			System.err.printf("[%19s] %-9s %-20s %s%n", Utils.calcDate(System.currentTimeMillis()), "[" + level + "]", "[" + module + "]", message);
		else if (level == LogLevel.DEBUG)
			if (debug)
				System.out.printf("[%19s] %-9s %-20s %s%n", Utils.calcDate(System.currentTimeMillis()), "[" + level + "]", "[" + module + "]", message);
		else
			System.out.printf("[%19s] %-9s %-20s %s%n", Utils.calcDate(System.currentTimeMillis()), "[" + level + "]", "[" + module + "]", message);
	}
}
