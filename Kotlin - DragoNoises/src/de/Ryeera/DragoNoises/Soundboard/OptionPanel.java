package de.Ryeera.DragoNoises.Soundboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

public class OptionPanel extends JPanel {
	
	private static final long serialVersionUID = 3226996783749908072L;
	
	private JSlider sliderPressR, sliderPressG, sliderPressB, sliderReleaseR, sliderReleaseG, sliderReleaseB, sliderVolume;
	private JTextArea taColorPress, taColorRelease;
	private JLabel labelPressR, labelPressG, labelPressB, labelReleaseR, labelReleaseG, labelReleaseB, labelVolume;
	private JButton btnOpen;
	private JTextField tfFile;
	private boolean listen = true;
	
	public OptionPanel() {
		super(new GridBagLayout());
		
		setBackground(new Color(128, 128, 128));
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		add(new JLabel("Color On Press:"), c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("Red: "), c);
		c.gridx++;
		sliderPressR = new JSlider(0, 255, 255);
		sliderPressR.setOpaque(false);
		sliderPressR.setSnapToTicks(true);
		sliderPressR.setPreferredSize(new Dimension(510, 20));
		sliderPressR.addChangeListener(e -> processListener(labelPressR, sliderPressR, taColorPress));
		add(sliderPressR, c);
		c.gridx++;
		labelPressR = new JLabel("255");
		add(labelPressR, c);

		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Green: "), c);
		c.gridx++;
		sliderPressG = new JSlider(0, 255, 255);
		sliderPressG.setOpaque(false);
		sliderPressG.setSnapToTicks(true);
		sliderPressG.setPreferredSize(new Dimension(510, 20));
		sliderPressG.addChangeListener(e -> processListener(labelPressG, sliderPressG, taColorPress));
		add(sliderPressG, c);
		c.gridx++;
		labelPressG = new JLabel("255");
		add(labelPressG, c);
		
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Blue: "), c);
		c.gridx++;
		sliderPressB = new JSlider(0, 255, 255);
		sliderPressB.setOpaque(false);
		sliderPressB.setSnapToTicks(true);
		sliderPressB.setPreferredSize(new Dimension(510, 20));
		sliderPressB.addChangeListener(e -> processListener(labelPressB, sliderPressB, taColorPress));
		add(sliderPressB, c);
		c.gridx++;
		labelPressB = new JLabel("255");
		add(labelPressB, c);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		taColorPress = new JTextArea(5, 76);
		taColorPress.setOpaque(true);
		taColorPress.setBackground(new Color(sliderPressR.getValue(), sliderPressG.getValue(), sliderPressB.getValue()));
		taColorPress.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		taColorPress.setEnabled(false);
		add(taColorPress, c);

		// COLOR CHOOSER 2
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		add(new JLabel("Color On Release:"), c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("Red: "), c);
		c.gridx++;
		sliderReleaseR = new JSlider(0, 255, 255);
		sliderReleaseR.setOpaque(false);
		sliderReleaseR.setSnapToTicks(true);
		sliderReleaseR.setPreferredSize(new Dimension(510, 20));
		sliderReleaseR.addChangeListener(e -> processListener(labelReleaseR, sliderReleaseR, taColorRelease));
		add(sliderReleaseR, c);
		c.gridx++;
		labelReleaseR = new JLabel("255");
		add(labelReleaseR, c);

		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Green: "), c);
		c.gridx++;
		sliderReleaseG = new JSlider(0, 255, 255);
		sliderReleaseG.setOpaque(false);
		sliderReleaseG.setSnapToTicks(true);
		sliderReleaseG.setPreferredSize(new Dimension(510, 20));
		sliderReleaseG.addChangeListener(e -> processListener(labelReleaseG, sliderReleaseG, taColorRelease));
		add(sliderReleaseG, c);
		c.gridx++;
		labelReleaseG = new JLabel("255");
		add(labelReleaseG, c);
		
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Blue: "), c);
		c.gridx++;
		sliderReleaseB = new JSlider(0, 255, 255);
		sliderReleaseB.setOpaque(false);
		sliderReleaseB.setSnapToTicks(true);
		sliderReleaseB.setPreferredSize(new Dimension(510, 20));
		sliderReleaseB.addChangeListener(e -> processListener(labelReleaseB, sliderReleaseB, taColorRelease));
		add(sliderReleaseB, c);
		c.gridx++;
		labelReleaseB = new JLabel("255");
		add(labelReleaseB, c);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		taColorRelease = new JTextArea(5, 76);
		taColorRelease.setOpaque(true);
		taColorRelease.setBackground(new Color(sliderReleaseR.getValue(), sliderReleaseG.getValue(), sliderReleaseB.getValue()));
		taColorRelease.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		taColorRelease.setEnabled(false);
		add(taColorRelease, c);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("File: "), c);
		c.gridx++;
		tfFile = new JTextField();
		tfFile.setPreferredSize(new Dimension(510, 20));
		tfFile.setEditable(false);
		add(tfFile, c);
		c.gridx++;
		btnOpen = new JButton("...");
		btnOpen.addActionListener(a -> {
			JFileChooser chooser = new JFileChooser(Soundboard.soundboard.getJSONObject(String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getX()) + String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getY())).getString("soundfile").equals("") ? "D:\\Soundboard" : Soundboard.soundboard.getJSONObject(String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getX()) + String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getY())).getString("soundfile"));
			chooser.setDialogTitle("Open File");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileHidingEnabled(false);
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			chooser.setFileFilter(new FileNameExtensionFilter("Audio Files", "wav", "ogg"));
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			// Open the Dialog
			if (chooser.showSaveDialog(SoundboardTray.soundboardGUI) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				tfFile.setText(file.getAbsolutePath());
				Soundboard.soundboard.getJSONObject(String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getX()) + String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getY())).put("soundfile", file.getAbsolutePath());
				SoundboardGUI.soundboardPanel.repaint();
			}
		});
		add(btnOpen, c);
		
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Vol: "), c);
		c.gridx++;
		sliderVolume = new JSlider(0, 100, 100);
		sliderVolume.setOpaque(false);
		sliderVolume.setSnapToTicks(true);
		sliderVolume.setPreferredSize(new Dimension(510, 20));
		sliderVolume.addChangeListener(e -> processListener(labelVolume, sliderVolume, null));
		add(sliderVolume, c);
		c.gridx++;
		labelVolume = new JLabel("100");
		add(labelVolume, c);
	}
	
	public void processListener(JLabel label, JSlider slider, JTextArea ta) {
		if(listen) {
			label.setText(String.valueOf(slider.getValue()));
			if(ta != null) {
				ta.setBackground(new Color(ta == taColorPress ? sliderPressR.getValue() : sliderReleaseR.getValue(), ta == taColorPress ? sliderPressG.getValue() : sliderReleaseG.getValue(), ta == taColorPress ? sliderPressB.getValue() : sliderReleaseB.getValue()));
				JSONObject colorjson = new JSONObject();
				colorjson.put("r", ta == taColorPress ? sliderPressR.getValue() : sliderReleaseR.getValue());
				colorjson.put("g", ta == taColorPress ? sliderPressG.getValue() : sliderReleaseG.getValue());
				colorjson.put("b", ta == taColorPress ? sliderPressB.getValue() : sliderReleaseB.getValue());
				Soundboard.soundboard.getJSONObject(String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getX()) + String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getY())).put(ta == taColorPress ? "on-color" : "off-color", colorjson);
			} else if(label == labelVolume)
				Soundboard.soundboard.getJSONObject(String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getX()) + String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getY())).put("vol", slider.getValue());
			SoundboardGUI.soundboardPanel.repaint();
		}
	}
	
	public void updateColors() {
		listen = false;
		JSONObject button = Soundboard.soundboard.getJSONObject(String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getX()) + String.valueOf(SoundboardGUI.soundboardPanel.selectedButton.getY()));
		JSONObject colorPress = button.getJSONObject("on-color");
		JSONObject colorRelease = button.getJSONObject("off-color");
		taColorPress.setBackground(new Color(colorPress.getInt("r"), colorPress.getInt("g"), colorPress.getInt("b")));
		sliderPressR.setValue(colorPress.getInt("r"));
		sliderPressG.setValue(colorPress.getInt("g"));
		sliderPressB.setValue(colorPress.getInt("b"));
		labelPressR.setText(String.valueOf(colorPress.getInt("r")));
		labelPressG.setText(String.valueOf(colorPress.getInt("g")));
		labelPressB.setText(String.valueOf(colorPress.getInt("b")));
		taColorRelease.setBackground(new Color(colorRelease.getInt("r"), colorRelease.getInt("g"), colorRelease.getInt("b")));
		sliderReleaseR.setValue(colorRelease.getInt("r"));
		sliderReleaseG.setValue(colorRelease.getInt("g"));
		sliderReleaseB.setValue(colorRelease.getInt("b"));
		labelReleaseR.setText(String.valueOf(colorRelease.getInt("r")));
		labelReleaseG.setText(String.valueOf(colorRelease.getInt("g")));
		labelReleaseB.setText(String.valueOf(colorRelease.getInt("b")));
		sliderVolume.setValue(button.getInt("vol"));
		labelVolume.setText(String.valueOf(button.getInt("vol")));
		tfFile.setText(button.getString("soundfile"));
		listen = true;
	}
}
