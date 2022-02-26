package de.Ryeera.DragoNoises.AnimationCreator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONException;
import org.json.JSONObject;

import de.Ryeera.DragoNoises.Frame;
import de.Ryeera.DragoNoises.Pixel;
import de.Ryeera.DragoNoises.Soundboard.Soundboard;
import de.Ryeera.DragoNoises.Soundboard.SoundboardTray;
import de.Ryeera.libs.JSONUtils;

public class AnimationGUI extends JFrame {
	
	private static final long    serialVersionUID = -1916398103262479500L;
	
	public static AnimationPanel animation;
	public static OptionPanel    options;
	public static List<Frame>    frames           = new ArrayList<>();
	public static int            currentFrame     = 0;
	public static int            fps              = 15;
	public static File           file             = null;
	private JMenuItem            menuItemOpenFolder, menuItemReload;
	
	public AnimationGUI() {
		super("Dragoboard Animation Creator");
		createAndShowGUI();
	}
	
	private void createAndShowGUI() {
		animation = new AnimationPanel();
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
		add(animation, BorderLayout.CENTER);
		setResizable(false);
		setIconImage(Soundboard.IMAGE);
		setSize(1452, 875);
		setVisible(true);
		addMouseListener(animation);
		addMouseMotionListener(animation);
		addMouseWheelListener(animation);
		addKeyListener(animation);
		requestFocusInWindow();
	}
	
	private void close() {
		SoundboardTray.animationGUI = null;
		dispose();
	}
	
	public static void reload() {
		try {
			frames = new ArrayList<>();
			currentFrame = 0;
			JSONObject animation = JSONUtils.readJSON(file);
			fps = animation.getInt("fps");
			JSONObject frames = animation.getJSONObject("frames");
			for(int frame = 0; frames.has(String.valueOf(frame)); frame++) {
				JSONObject frameJSON = frames.getJSONObject(String.valueOf(frame));
				Frame f = new Frame();
				for(int x = 0; x < 9; x++) {
					for(int y = 0; y < 9; y++) {
						Pixel p = new Pixel(new Color(frameJSON.getJSONObject(x + "" + y).getInt("c")));
						p.makeLastFrame(frameJSON.getJSONObject(x + "" + y).getBoolean("a"));
						p.makeNextFrame(frameJSON.getJSONObject(x + "" + y).getBoolean("b"));
						f.setPixel(x, y, p);
					}
				}
				AnimationGUI.frames.add(f);
			}
			options.btnNextFrame.setEnabled(true);
			options.btnPrevFrame.setEnabled(false);
			options.labelCurrentFrame.setText("0");
			options.labelFPS.setText(String.valueOf(fps));
			options.labelMaxFrame.setText(String.valueOf(AnimationGUI.frames.size()-1));
			options.sliderFPS.setValue(fps);
			options.sliderFrames.setMaximum(AnimationGUI.frames.size()-1);
			options.sliderFrames.setValue(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(AnimationCreator.gui, "The file could not be read! Make sure it exists and you have permission to read " + file.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
			file = null;
		} catch (JSONException e) {
			JOptionPane.showMessageDialog(AnimationCreator.gui, "The file " + file.getAbsolutePath() + " is not in the right format!", "Error", JOptionPane.ERROR_MESSAGE);
			file = null;
		}
	}
	
	public static void saveAs() {
		JFileChooser chooser = new JFileChooser(file);
		chooser.setDialogTitle("Save As");
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileHidingEnabled(false);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setFileFilter(new FileNameExtensionFilter("Java Script Object Notation File", "json"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (chooser.showSaveDialog(AnimationCreator.gui) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			save();
		}
	}
	
	public static void save() {
		JSONObject animation = new JSONObject();
		animation.put("fps", fps);
		JSONObject frames = new JSONObject();
		for (int i = 0; i < AnimationGUI.frames.size(); i++) {
			JSONObject frame = new JSONObject();
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					JSONObject pixel = new JSONObject();
					pixel.put("c", AnimationGUI.frames.get(i).getPixel(x, y).getColor().getRGB());
					pixel.put("a", AnimationGUI.frames.get(i).getPixel(x, y).isLastFrame());
					pixel.put("b", AnimationGUI.frames.get(i).getPixel(x, y).isNextFrame());
					frame.put(x + "" + y, pixel);
				}
			}
			frames.put(String.valueOf(i), frame);
		}
		animation.put("frames", frames);
		try {
			JSONUtils.writeJSON(animation, file);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(AnimationCreator.gui, "The file could not be saved! Make sure you have permission to write to " + file.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar          mb                  = new JMenuBar();
		JMenu             menuFile            = new JMenu("File");
		JMenuItem         menuItemNew         = new JMenuItem("New", KeyEvent.VK_N);
		JMenuItem         menuItemOpen        = new JMenuItem("Open...", KeyEvent.VK_O);
		JMenuItem         menuItemSave        = new JMenuItem("Save", KeyEvent.VK_S);
		JMenuItem         menuItemSaveAs      = new JMenuItem("Save As...", KeyEvent.VK_A);
		JMenuItem         menuItemSaveCopyAs  = new JMenuItem("Save Copy As...", KeyEvent.VK_C);
		JMenuItem         menuItemExit        = new JMenuItem("Exit", KeyEvent.VK_E);
		JMenu             menuEdit            = new JMenu("Edit");
		JMenuItem         menuItemUndo        = new JMenuItem("Undo", KeyEvent.VK_U);
		JMenuItem         menuItemRedo        = new JMenuItem("Redo", KeyEvent.VK_R);
		JMenuItem         menuItemCut         = new JMenuItem("Cut", KeyEvent.VK_T);
		JMenuItem         menuItemCopy        = new JMenuItem("Copy", KeyEvent.VK_C);
		JMenuItem         menuItemPaste       = new JMenuItem("Paste", KeyEvent.VK_P);
		JMenuItem         menuItemSelectAll   = new JMenuItem("Select All", KeyEvent.VK_S);
		JMenuItem         menuItemMirrorX     = new JMenuItem("Mirror Horizontally", KeyEvent.VK_H);
		JMenuItem         menuItemMirrorY     = new JMenuItem("Mirror Vertically", KeyEvent.VK_V);
		JMenuItem         menuItemMirrorBoth  = new JMenuItem("Mirror on both axes", KeyEvent.VK_M);
		JMenu             menuView            = new JMenu("View");
		JCheckBoxMenuItem menuItemShowCoords  = new JCheckBoxMenuItem("Show Coordinates", false);
		JMenu             menuSettings        = new JMenu("Settings");
		JCheckBoxMenuItem cbDebug             = new JCheckBoxMenuItem("Debug", false);
		JMenu             menuTools           = new JMenu("Tools");
		JCheckBoxMenuItem menuItemMirrorModeX = new JCheckBoxMenuItem("Mirror X", false);
		JCheckBoxMenuItem menuItemMirrorModeY = new JCheckBoxMenuItem("Mirror Y", false);
		JMenu             menuHelp            = new JMenu("Help");
		JMenuItem         menuItemAbout       = new JMenuItem("About", KeyEvent.VK_A);
		menuItemOpenFolder = new JMenuItem("Open Containing Folder", KeyEvent.VK_F);
		menuItemReload     = new JMenuItem("Reload from Disk", KeyEvent.VK_R);
		
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuEdit.setMnemonic(KeyEvent.VK_E);
		menuView.setMnemonic(KeyEvent.VK_V);
		menuSettings.setMnemonic(KeyEvent.VK_S);
		menuTools.setMnemonic(KeyEvent.VK_T);
		menuHelp.setMnemonic(KeyEvent.VK_H);
		
		menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItemOpenFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		menuItemReload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		menuItemSaveCopyAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.ALT_MASK));
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		menuItemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menuItemRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		menuItemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		menuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menuItemMirrorX.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
		menuItemMirrorY.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
		menuItemMirrorBoth.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		menuItemShowCoords.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		cbDebug.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		menuItemMirrorModeX.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		menuItemMirrorModeY.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		
		menuItemOpenFolder.setEnabled(false);
		menuItemReload.setEnabled(false);
		
		menuFile.add(menuItemNew);
		menuFile.addSeparator();
		menuFile.add(menuItemOpen);
		menuFile.add(menuItemOpenFolder);
		menuFile.add(menuItemReload);
		menuFile.addSeparator();
		menuFile.add(menuItemSave);
		menuFile.add(menuItemSaveAs);
		menuFile.add(menuItemSaveCopyAs);
		menuFile.addSeparator();
		menuFile.add(menuItemExit);
		mb.add(menuFile);
		menuEdit.add(menuItemUndo);
		menuEdit.add(menuItemRedo);
		menuEdit.addSeparator();
		menuEdit.add(menuItemCut);
		menuEdit.add(menuItemCopy);
		menuEdit.add(menuItemPaste);
		menuEdit.add(menuItemSelectAll);
		menuEdit.addSeparator();
		menuEdit.add(menuItemMirrorX);
		menuEdit.add(menuItemMirrorY);
		menuEdit.add(menuItemMirrorBoth);
		mb.add(menuEdit);
		menuView.add(menuItemShowCoords);
		mb.add(menuView);
		menuSettings.add(cbDebug);
		mb.add(menuSettings);
		menuTools.add(menuItemMirrorModeX);
		menuTools.add(menuItemMirrorModeY);
		mb.add(menuTools);
		menuHelp.add(menuItemAbout);
		mb.add(menuHelp);
		
		menuItemNew.addActionListener(e -> {
			frames = new ArrayList<>();
			frames.add(new Frame());
			currentFrame = 0;
			fps          = 15;
			options.btnPrevFrame.setEnabled(false);
			options.btnNextFrame.setEnabled(false);
			options.sliderFrames.setMaximum(0);
			options.sliderFrames.setValue(0);
			options.sliderFPS.setValue(15);
			options.labelCurrentFrame.setText("0");
			options.labelMaxFrame.setText("0");
			options.labelFPS.setText("15");
			options.playing = false;
			menuItemOpenFolder.setEnabled(false);
			menuItemReload.setEnabled(false);
			file = null;
			animation.repaint();
		});
		menuItemOpen.addActionListener(e -> {
			if(file == null) {
				file = new File(".");
			}
			JFileChooser chooser = new JFileChooser(file);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileHidingEnabled(false);
			chooser.setFileFilter(new FileNameExtensionFilter("JavaScript Object Notation File (*.json)", "json"));
			if (chooser.showOpenDialog(AnimationCreator.gui) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				menuItemOpenFolder.setEnabled(true);
				menuItemReload.setEnabled(true);
				reload();
			}
		});
		menuItemOpenFolder.addActionListener(e -> {
			try {
				Runtime.getRuntime().exec("explorer.exe \"" + file.getAbsolutePath() + "\"");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		menuItemReload.addActionListener(e -> reload());
		menuItemSave.addActionListener(e -> {
			if (file == null)
				saveAs();
			else
				save();
		});
		menuItemSaveAs.addActionListener(e -> saveAs());
		menuItemSaveCopyAs.addActionListener(e -> {
			File temp = file;
			saveAs();
			file = temp;
		});
		menuItemExit.addActionListener(e -> close());
		menuItemUndo.addActionListener(e -> {
			
		});
		menuItemRedo.addActionListener(e -> {
			
		});
		menuItemCut.addActionListener(e -> {
			
		});
		menuItemCopy.addActionListener(e -> {
			
		});
		menuItemPaste.addActionListener(e -> {
			
		});
		menuItemSelectAll.addActionListener(e -> {
			
		});
		menuItemMirrorX.addActionListener(e -> {
			
		});
		menuItemMirrorY.addActionListener(e -> {
			
		});
		menuItemMirrorBoth.addActionListener(e -> {
			
		});
		menuItemAbout.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Dragoboard Animation Creator v" + AnimationCreator.VERSION + " by Ryeera", "About", JOptionPane.INFORMATION_MESSAGE, Soundboard.ICON);
		});
		return mb;
	}
	
}
