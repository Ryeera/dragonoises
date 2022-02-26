package de.Ryeera.DragoNoises.AnimationCreator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import de.Ryeera.DragoNoises.Button;
import de.Ryeera.DragoNoises.Frame;
import de.Ryeera.DragoNoises.Soundboard.Soundboard;

public class OptionPanel extends JPanel {
	
	private static final long serialVersionUID = 3226996783749908072L;
	JButton btnMakeLastFrame, btnMakeNextFrame, btnSetColor, btnAddFrame, btnInsertFrame, btnRemoveFrame, btnAddFrameCopy, btnInsertFrameCopy, btnPlayLoop, btnPlayOnce, btnPlayLaunchpad, btnStop, btnPrevFrame, btnNextFrame, btnPickColor;
	JSlider sliderR, sliderG, sliderB, sliderFrames, sliderFPS;
	JLabel labelR, labelG, labelB, labelCurrentFrame, labelMaxFrame, labelFPS;
	JTextArea taColor;
	boolean playing = false;
	
	public OptionPanel() {
		super(new GridBagLayout());
		
		setBackground(new Color(128, 128, 128));
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		add(new JLabel("FPS: "), c);
		c.gridx++;
		sliderFPS = new JSlider(1, 144, AnimationGUI.fps);
		sliderFPS.setOpaque(false);
		sliderFPS.setSnapToTicks(true);
		sliderFPS.setPreferredSize(new Dimension(510, 20));
		sliderFPS.addChangeListener(e -> {
			labelFPS.setText(String.valueOf(sliderFPS.getValue()));
			AnimationGUI.fps = sliderFPS.getValue();
		});
		add(sliderFPS, c);
		c.gridx++;
		labelFPS = new JLabel(String.valueOf(AnimationGUI.fps));
		add(labelFPS, c);
		
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Red: "), c);
		c.gridx++;
		sliderR = new JSlider(0, 255, 255);
		sliderR.setOpaque(false);
		sliderR.setSnapToTicks(true);
		sliderR.setPreferredSize(new Dimension(510, 20));
		sliderR.addChangeListener(e -> {
			labelR.setText(String.valueOf(sliderR.getValue()));
			Color color = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
			taColor.setBackground(color);
		});
		add(sliderR, c);
		c.gridx++;
		labelR = new JLabel("255");
		add(labelR, c);

		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Green: "), c);
		c.gridx++;
		sliderG = new JSlider(0, 255, 255);
		sliderG.setOpaque(false);
		sliderG.setSnapToTicks(true);
		sliderG.setPreferredSize(new Dimension(510, 20));
		sliderG.addChangeListener(e -> {
			labelG.setText(String.valueOf(sliderG.getValue()));
			Color color = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
			taColor.setBackground(color);
		});
		add(sliderG, c);
		c.gridx++;
		labelG = new JLabel("255");
		add(labelG, c);
		
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Blue: "), c);
		c.gridx++;
		sliderB = new JSlider(0, 255, 255);
		sliderB.setOpaque(false);
		sliderB.setSnapToTicks(true);
		sliderB.setPreferredSize(new Dimension(510, 20));
		sliderB.addChangeListener(e -> {
			labelB.setText(String.valueOf(sliderB.getValue()));
			Color color = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
			taColor.setBackground(color);
		});
		add(sliderB, c);
		c.gridx++;
		labelB = new JLabel("255");
		add(labelB, c);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		taColor = new JTextArea(5, 76);
		taColor.setOpaque(true);
		taColor.setBackground(new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()));
		taColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		taColor.setEnabled(false);
		add(taColor, c);
		
		c.gridx = 0;
		c.gridy++;
		JPanel buttonpanel = new JPanel(new GridLayout(2, 2));
		btnSetColor = new JButton("Set Color");
		btnSetColor.setPreferredSize(new Dimension(200, 30));
		btnSetColor.setOpaque(false);
		btnSetColor.addActionListener(e -> {
			Color color = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
			Frame frame = AnimationGUI.frames.get(AnimationGUI.currentFrame);
			for(Button button : AnimationGUI.animation.selectedButtons) {
				frame.getPixel(button.getX(), button.getY()).setColor(color);
				frame.getPixel(button.getX(), button.getY()).makeLastFrame(false);
				frame.getPixel(button.getX(), button.getY()).makeNextFrame(false);
			}
			AnimationGUI.animation.repaint();
		});
		btnPickColor = new JButton("Pick Color of selected Pixel");
		btnPickColor.setPreferredSize(new Dimension(302, 30));
		btnPickColor.setOpaque(false);
		btnPickColor.setEnabled(false);
		btnPickColor.addActionListener(e -> {
			Button b = AnimationGUI.animation.selectedButtons.get(0);
			Color color = AnimationGUI.frames.get(AnimationGUI.currentFrame).getPixel(b.getX(), b.getY()).getColor();
			sliderR.setValue(color.getRed());
			sliderG.setValue(color.getGreen());
			sliderB.setValue(color.getBlue());
			labelR.setText(String.valueOf(color.getRed()));
			labelG.setText(String.valueOf(color.getGreen()));
			labelB.setText(String.valueOf(color.getBlue()));
		});
		btnMakeLastFrame = new JButton("Use Color of last Frame before animation started");
		btnMakeLastFrame.setPreferredSize(new Dimension(200, 30));
		btnMakeLastFrame.setOpaque(false);
		btnMakeLastFrame.addActionListener(e -> {
			Frame frame = AnimationGUI.frames.get(AnimationGUI.currentFrame);
			for(Button button : AnimationGUI.animation.selectedButtons) {
				frame.getPixel(button.getX(), button.getY()).makeLastFrame(true);
				frame.getPixel(button.getX(), button.getY()).makeNextFrame(false);
			}
			AnimationGUI.animation.repaint();
		});
		btnMakeNextFrame = new JButton("Use Color of next Frame after animation is finished");
		btnMakeNextFrame.setPreferredSize(new Dimension(200, 30));
		btnMakeNextFrame.setOpaque(false);
		btnMakeNextFrame.addActionListener(e -> {
			Frame frame = AnimationGUI.frames.get(AnimationGUI.currentFrame);
			for(Button button : AnimationGUI.animation.selectedButtons) {
				frame.getPixel(button.getX(), button.getY()).makeNextFrame(true);
				frame.getPixel(button.getX(), button.getY()).makeLastFrame(false);
			}
			AnimationGUI.animation.repaint();
		});
		buttonpanel.setOpaque(false);
		buttonpanel.add(btnSetColor);
		buttonpanel.add(btnPickColor);
		buttonpanel.add(btnMakeLastFrame);
		buttonpanel.add(btnMakeNextFrame);
		add(buttonpanel, c);
		
		c.gridy++;
		JLabel labelMinFrame = new JLabel("0", SwingConstants.CENTER);
		JLabel currentFrame = new JLabel("Current Frame:", SwingConstants.CENTER);
		currentFrame.setPreferredSize(new Dimension(200, 30));
		labelMinFrame.setPreferredSize(new Dimension(20, 20));
		labelCurrentFrame = new JLabel("0", SwingConstants.CENTER);
		labelCurrentFrame.setPreferredSize(new Dimension(20, 20));
		labelMaxFrame = new JLabel("0", SwingConstants.CENTER);
		labelMaxFrame.setPreferredSize(new Dimension(20, 20));
		sliderFrames = new JSlider(0, 0, 0);
		sliderFrames.setPreferredSize(new Dimension(570, 20));
		sliderFrames.setOpaque(false);
		sliderFrames.addChangeListener(e -> {
			labelCurrentFrame.setText(String.valueOf(sliderFrames.getValue()));
			AnimationGUI.currentFrame = sliderFrames.getValue();
			btnNextFrame.setEnabled(!playing && AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(!playing && AnimationGUI.currentFrame > 0);
			AnimationGUI.animation.repaint();
		});
		btnPrevFrame = new JButton("<");
		btnPrevFrame.setOpaque(false);
		btnPrevFrame.setEnabled(false);
		btnPrevFrame.addActionListener(e -> {
			AnimationGUI.currentFrame--;
			sliderFrames.setValue(AnimationGUI.currentFrame);
			btnNextFrame.setEnabled(AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(AnimationGUI.currentFrame > 0);
		});
		btnNextFrame = new JButton(">");
		btnNextFrame.setOpaque(false);
		btnNextFrame.setEnabled(false);
		btnNextFrame.addActionListener(e -> {
			AnimationGUI.currentFrame++;
			sliderFrames.setValue(AnimationGUI.currentFrame);
			btnNextFrame.setEnabled(AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(AnimationGUI.currentFrame > 0);
		});
		JPanel south = new JPanel(new BorderLayout());
		south.add(btnPrevFrame, BorderLayout.LINE_START);
		south.add(labelCurrentFrame, BorderLayout.CENTER);
		south.add(btnNextFrame, BorderLayout.LINE_END);
		south.setOpaque(false);
		JPanel panelFrames = new JPanel(new BorderLayout());
		panelFrames.add(sliderFrames, BorderLayout.CENTER);
		panelFrames.add(labelMinFrame, BorderLayout.LINE_START);
		panelFrames.add(labelMaxFrame, BorderLayout.LINE_END);
		panelFrames.add(currentFrame, BorderLayout.PAGE_START);
		panelFrames.add(south, BorderLayout.PAGE_END);
		panelFrames.setBackground(new Color(150, 150, 150));
		add(panelFrames, c);
		
		c.gridy++;
		btnAddFrame = new JButton("Add Frame");
		btnAddFrame.setPreferredSize(new Dimension(203, 30));
		btnAddFrame.setOpaque(false);
		btnAddFrame.addActionListener(e -> {
			AnimationGUI.frames.add(new Frame());
			sliderFrames.setMaximum(AnimationGUI.frames.size()-1);
			AnimationGUI.currentFrame = AnimationGUI.frames.size()-1;
			sliderFrames.setValue(AnimationGUI.currentFrame);
			labelMaxFrame.setText(String.valueOf(AnimationGUI.frames.size()-1));
			labelCurrentFrame.setText(String.valueOf(AnimationGUI.currentFrame));
			btnNextFrame.setEnabled(AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(AnimationGUI.currentFrame > 0);
			AnimationGUI.animation.repaint();
		});
		btnInsertFrame = new JButton("Insert Frame");
		btnInsertFrame.setPreferredSize(new Dimension(203, 30));
		btnInsertFrame.setOpaque(false);
		btnInsertFrame.addActionListener(e -> {
			AnimationGUI.frames.add(AnimationGUI.currentFrame, new Frame());
			sliderFrames.setMaximum(AnimationGUI.frames.size()-1);
			labelMaxFrame.setText(String.valueOf(AnimationGUI.frames.size()-1));
			btnNextFrame.setEnabled(AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(AnimationGUI.currentFrame > 0);
			AnimationGUI.animation.repaint();
		});
		btnRemoveFrame = new JButton("Remove Frame");
		btnRemoveFrame.setPreferredSize(new Dimension(203, 30));
		btnRemoveFrame.setOpaque(false);
		btnRemoveFrame.addActionListener(e -> {
			AnimationGUI.frames.remove(AnimationGUI.currentFrame);
			if(AnimationGUI.currentFrame != 0)AnimationGUI.currentFrame--;
			if(AnimationGUI.frames.size() == 0)AnimationGUI.frames.add(new Frame());
			sliderFrames.setMaximum(AnimationGUI.frames.size()-1);
			sliderFrames.setValue(AnimationGUI.currentFrame);
			labelMaxFrame.setText(String.valueOf(AnimationGUI.frames.size()-1));
			labelCurrentFrame.setText(String.valueOf(AnimationGUI.currentFrame));
			btnNextFrame.setEnabled(AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(AnimationGUI.currentFrame > 0);
			AnimationGUI.animation.repaint();
		});
		btnAddFrameCopy = new JButton("Add Copy of Current Frame");
		btnAddFrameCopy.setPreferredSize(new Dimension(203, 30));
		btnAddFrameCopy.setOpaque(false);
		btnAddFrameCopy.addActionListener(e -> {
			AnimationGUI.frames.add(AnimationGUI.frames.get(AnimationGUI.currentFrame).clone());
			sliderFrames.setMaximum(AnimationGUI.frames.size()-1);
			AnimationGUI.currentFrame = AnimationGUI.frames.size()-1;
			sliderFrames.setValue(AnimationGUI.currentFrame);
			labelMaxFrame.setText(String.valueOf(AnimationGUI.frames.size()-1));
			labelCurrentFrame.setText(String.valueOf(AnimationGUI.currentFrame));
			btnNextFrame.setEnabled(AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(AnimationGUI.currentFrame > 0);
			AnimationGUI.animation.repaint();
		});
		btnInsertFrameCopy = new JButton("Insert Copy of Current Frame");
		btnInsertFrameCopy.setPreferredSize(new Dimension(203, 30));
		btnInsertFrameCopy.setOpaque(false);
		btnInsertFrameCopy.addActionListener(e -> {
			AnimationGUI.frames.add(AnimationGUI.currentFrame, AnimationGUI.frames.get(AnimationGUI.currentFrame).clone());
			sliderFrames.setMaximum(AnimationGUI.frames.size()-1);
			labelMaxFrame.setText(String.valueOf(AnimationGUI.frames.size()-1));
			btnNextFrame.setEnabled(AnimationGUI.currentFrame < AnimationGUI.frames.size()-1);
			btnPrevFrame.setEnabled(AnimationGUI.currentFrame > 0);
			AnimationGUI.animation.repaint();
		});
		JPanel panelButtons = new JPanel(new GridLayout(2, 3));
		panelButtons.add(btnAddFrame);
		panelButtons.add(btnInsertFrame);
		panelButtons.add(btnRemoveFrame);
		panelButtons.add(btnAddFrameCopy);
		panelButtons.add(btnInsertFrameCopy);
		panelButtons.setOpaque(false);
		add(panelButtons, c);
		
		c.gridy++;
		btnPlayLoop = new JButton("Play Loop");
		btnPlayLoop.setPreferredSize(new Dimension(203, 30));
		btnPlayLoop.setOpaque(false);
		btnPlayLoop.addActionListener(e -> {
			new Thread(() -> {
				playing = true;
				btnStop.setEnabled(true);
				btnPlayLoop.setEnabled(false);
				btnPlayOnce.setEnabled(false);
				btnPlayLaunchpad.setEnabled(false);
				btnNextFrame.setEnabled(false);
				btnPrevFrame.setEnabled(false);
				sliderFPS.setEnabled(false);
				sliderFrames.setEnabled(false);
				while(playing) {
					long start = System.currentTimeMillis();
					for(AnimationGUI.currentFrame = 0; AnimationGUI.currentFrame < AnimationGUI.frames.size() && playing; AnimationGUI.currentFrame++) {
						AnimationGUI.animation.repaint();
						labelCurrentFrame.setText(String.valueOf(AnimationGUI.currentFrame));
						sliderFrames.setValue(AnimationGUI.currentFrame);
						while(System.currentTimeMillis()-start < (AnimationGUI.currentFrame+1)*(1000.0/AnimationGUI.fps)) {
							try {
								Thread.sleep(1);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
					AnimationGUI.currentFrame = AnimationGUI.frames.size()-1;
				}
				btnStop.setEnabled(false);
				btnPlayLoop.setEnabled(true);
				btnPlayOnce.setEnabled(true);
				btnPlayLaunchpad.setEnabled(true);
				btnNextFrame.setEnabled(false);
				btnPrevFrame.setEnabled(true);
				sliderFPS.setEnabled(true);
				sliderFrames.setEnabled(true);
			}).start();
		});
		btnPlayOnce = new JButton("Play Once");
		btnPlayOnce.setPreferredSize(new Dimension(203, 30));
		btnPlayOnce.setOpaque(false);
		btnPlayOnce.addActionListener(e -> {
			new Thread(() -> {
				playing = true;
				btnStop.setEnabled(true);
				btnPlayLoop.setEnabled(false);
				btnPlayOnce.setEnabled(false);
				btnPlayLaunchpad.setEnabled(false);
				btnNextFrame.setEnabled(false);
				btnPrevFrame.setEnabled(false);
				sliderFPS.setEnabled(false);
				sliderFrames.setEnabled(false);
				long start = System.currentTimeMillis();
				for(AnimationGUI.currentFrame = 0; AnimationGUI.currentFrame < AnimationGUI.frames.size() && playing; AnimationGUI.currentFrame++) {
					AnimationGUI.animation.repaint();
					labelCurrentFrame.setText(String.valueOf(AnimationGUI.currentFrame));
					sliderFrames.setValue(AnimationGUI.currentFrame);
					while(System.currentTimeMillis()-start < (AnimationGUI.currentFrame+1)*(1000.0/AnimationGUI.fps)) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				AnimationGUI.currentFrame = AnimationGUI.frames.size()-1;
				playing = false;
				btnStop.setEnabled(false);
				btnPlayLoop.setEnabled(true);
				btnPlayOnce.setEnabled(true);
				btnPlayLaunchpad.setEnabled(true);
				btnNextFrame.setEnabled(false);
				btnPrevFrame.setEnabled(true);
				sliderFPS.setEnabled(true);
				sliderFrames.setEnabled(true);
			}).start();
		});
		btnPlayLaunchpad = new JButton("Play On Launchpad");
		btnPlayLaunchpad.setPreferredSize(new Dimension(203, 30));
		btnPlayLaunchpad.setOpaque(false);
		btnPlayLaunchpad.addActionListener(e -> {
			new Thread(() -> {
				playing = true;
				Soundboard.transitioning = true;
				btnStop.setEnabled(true);
				btnPlayLoop.setEnabled(false);
				btnPlayOnce.setEnabled(false);
				btnPlayLaunchpad.setEnabled(false);
				btnNextFrame.setEnabled(false);
				btnPrevFrame.setEnabled(false);
				sliderFPS.setEnabled(false);
				sliderFrames.setEnabled(false);
				long start = System.currentTimeMillis();
				for(AnimationGUI.currentFrame = 0; AnimationGUI.currentFrame < AnimationGUI.frames.size() && playing; AnimationGUI.currentFrame++) {
					Soundboard.applyFrame(AnimationGUI.frames.get(AnimationGUI.currentFrame), Frame.getCurrent(), Frame.getNext());
					labelCurrentFrame.setText(String.valueOf(AnimationGUI.currentFrame));
					sliderFrames.setValue(AnimationGUI.currentFrame);
					while(System.currentTimeMillis()-start < (AnimationGUI.currentFrame+1)*(1000.0/AnimationGUI.fps)) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				AnimationGUI.currentFrame = AnimationGUI.frames.size()-1;
				playing = false;
				Soundboard.applyFrame(Frame.getNext(), Frame.getNext(), Frame.getNext());
				Soundboard.transitioning = false;
				btnStop.setEnabled(false);
				btnPlayLoop.setEnabled(true);
				btnPlayOnce.setEnabled(true);
				btnPlayLaunchpad.setEnabled(true);
				btnNextFrame.setEnabled(false);
				btnPrevFrame.setEnabled(true);
				sliderFPS.setEnabled(true);
				sliderFrames.setEnabled(true);
			}).start();
		});
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.setPreferredSize(new Dimension(203, 30));
		btnStop.setOpaque(false);
		btnStop.addActionListener(e -> {
			playing = false;
			btnStop.setEnabled(false);
			btnPlayLoop.setEnabled(true);
			btnPlayOnce.setEnabled(true);
		});
		JPanel panelButtonsPlayback = new JPanel(new GridLayout(2, 3));
		panelButtonsPlayback.add(btnPlayLoop);
		panelButtonsPlayback.add(btnPlayOnce);
		panelButtonsPlayback.add(btnPlayLaunchpad);
		panelButtonsPlayback.add(btnStop);
		panelButtonsPlayback.setOpaque(false);
		add(panelButtonsPlayback, c);
	}
}