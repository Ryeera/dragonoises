package de.Ryeera.DragoNoises.Soundboard;

import java.awt.BasicStroke;
import java.awt.Color;
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

import javax.swing.JPanel;

import org.json.JSONObject;

import de.Ryeera.DragoNoises.Button;

public class SoundboardPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	
	private static final long serialVersionUID = 1240996840451841451L;
	
	private Image             bufferImage;
	private Graphics2D        bufferGraphics;
	private int               bufferWidth, bufferHeight;
	public Button             selectedButton  = Button.get(0, 0);
	
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
		
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(5));
		g.drawRoundRect(10, 10, 804, 804, 50, 50);
		g.setStroke(new BasicStroke(3));
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				int   px    = x, py = (y - 8) * -1;
				if(x == 8 && y == 8) continue;
				
				if(x != 8 && y != 8) {
					JSONObject colorjson = Soundboard.soundboard.getJSONObject(String.valueOf(x) + String.valueOf(y)).getJSONObject("off-color");
					g.setColor(new Color(colorjson.getInt("r"), colorjson.getInt("g"), colorjson.getInt("b")));
					g.fillRoundRect(60 + px * 80, 60 + py * 80, 64, 64, 15, 15);
				}
				if(selectedButton == Button.get(x, y)) {
					g.setColor(Color.YELLOW);
				} else {
					g.setColor(Color.WHITE);
				}
				if(x == 8 || y == 8) {
					g.drawOval(60 + px * 80, 60 + py * 80, 64, 64);
				} else {
					g.drawRoundRect(60 + px * 80, 60 + py * 80, 64, 64, 15, 15);
				}
			}
		}
		SoundboardTray.soundboardGUI.requestFocusInWindow();
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
		if (button != null) {
			selectedButton = button;
			SoundboardGUI.options.updateColors();
		}
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}
}
