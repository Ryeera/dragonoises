package de.Ryeera.DragoNoises.AnimationCreator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.Ryeera.DragoNoises.Button;
import de.Ryeera.DragoNoises.Frame;

public class AnimationPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	
	private static final long serialVersionUID = 1240996840451841451L;
	
	private Image             bufferImage;
	private Graphics2D        bufferGraphics;
	private int               bufferWidth, bufferHeight;
	public List<Button>       selectedButtons  = new ArrayList<>();
	private boolean           ctrlDown         = false;
	private boolean           shiftDown        = false;
	
	public AnimationPanel() {
		AnimationGUI.frames.add(new Frame());
	}
	
	private Button getClickedButton(int x, int y) {
		int bx = (int) Math.ceil((x - 135.0) / 80.0);
		int by = (int) Math.ceil((((y - 100.0) / 80.0) - 8.0) * -1.0);
		if (bx < 0 || bx > 8 || by < 0 || by > 8)return null;
		if (bx == 8 && by == 8)
			return null;
		else
			return Button.get(bx, by);
	}
	
	public void paintBuffer(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(64, 64, 64));
		g.fillRect(0, 0, bufferWidth, bufferHeight);
		g.setColor(Color.BLACK);
		g.fillRoundRect(10, 10, 804, 804, 50, 50);
		Frame frame = AnimationGUI.frames.get(AnimationGUI.currentFrame);
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (x == 8 && y == 8)
					continue;
				int   px    = x, py = (y - 8) * -1;
				if (frame.getPixel(x, y).isLastFrame()) {
					g.setColor(Color.WHITE);
					g.setFont(new Font("impact", Font.BOLD, 24));
					g.drawString("<", 86 + px * 80, 102 + py * 80);
				} else if(frame.getPixel(x, y).isNextFrame()) { 
					g.setColor(Color.WHITE);
					g.setFont(new Font("impact", Font.BOLD, 24));
					g.drawString(">", 85 + px * 80, 102 + py * 80);
				} else {
					g.setColor(frame.getPixel(x, y).getColor());
					if (x == 8 || y == 8)
						g.fillOval(60 + px * 80, 60 + py * 80, 64, 64);
					else
						g.fillRoundRect(60 + px * 80, 60 + py * 80, 64, 64, 15, 15);
				}
			}
		}
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(5));
		g.drawRoundRect(10, 10, 804, 804, 50, 50);
		g.setStroke(new BasicStroke(3));
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				int   px    = x, py = (y - 8) * -1;
				if(x == 8 && y == 8) continue;
				if(selectedButtons.contains(Button.get(x, y)))
					g.setColor(Color.YELLOW);
				else
					g.setColor(Color.WHITE);
				if(x == 8 || y == 8)
					g.drawOval(60 + px * 80, 60 + py * 80, 64, 64);
				else
					g.drawRoundRect(60 + px * 80, 60 + py * 80, 64, 64, 15, 15);
			}
		}
		AnimationCreator.gui.requestFocusInWindow();
	}
	
	@Override
	public void update(Graphics g) {
		paintComponent(g);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (bufferWidth != getSize().width || bufferHeight != getSize().height || bufferImage == null || bufferGraphics == null)
			resetBuffer();
		if (bufferGraphics != null) {
			bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);
			paintBuffer(bufferGraphics);
			g.drawImage(bufferImage, 0, 0, this);
		}
	}
	
	/**
	 * Resets the Buffer if {@code bufferWidth} or {@code bufferHeight} have changed in order to not disorient the resulting image.
	 */
	private void resetBuffer() {
		bufferWidth  = getSize().width;
		bufferHeight = getSize().height;
		if (bufferGraphics != null) {
			bufferGraphics.dispose();
			bufferGraphics = null;
		}
		if (bufferImage != null) {
			bufferImage.flush();
			bufferImage = null;
		}
		System.gc();
		bufferImage    = createImage(bufferWidth, bufferHeight);
		bufferGraphics = (Graphics2D) bufferImage.getGraphics();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		Button button = getClickedButton(e.getX(), e.getY());
		if (button == null)
			selectedButtons.clear();
		else if (shiftDown) {
			if(selectedButtons.size() == 1) {
				Button last = selectedButtons.get(0);
				selectedButtons.clear();
				for(int x = last.getX() > button.getX() ? button.getX() : last.getX(); x <= (last.getX() > button.getX() ? last.getX() : button.getX()); x++)
					for(int y = last.getY() > button.getY() ? button.getY() : last.getY(); y <= (last.getY() > button.getY() ? last.getY() : button.getY()); y++)
						selectedButtons.add(Button.get(x, y));
			} else {
				selectedButtons.clear();
				selectedButtons.add(button);
			}
		} else if (ctrlDown) {
			if(selectedButtons.contains(button))
				selectedButtons.remove(button);
			else
				selectedButtons.add(button);
		} else {
			selectedButtons.clear();
			selectedButtons.add(button);
		}
		AnimationGUI.options.btnPickColor.setEnabled(selectedButtons.size() == 1);
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			ctrlDown = true;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			shiftDown = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			ctrlDown = false;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			shiftDown = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
}
