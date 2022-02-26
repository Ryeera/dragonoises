package de.Ryeera.DragoNoises;

import java.awt.Color;
import java.io.Serializable;

public class Pixel implements Serializable {

	private static final long serialVersionUID = -622416024968386323L;
	private Color color;
	private boolean nextFrame, lastFrame;
	
	public Pixel(int r, int g, int b) {
		this.color = new Color(r, g, b);
	}
	
	public Pixel(Color c) {
		this.color = c;
	}
	
	public Pixel() {
		this.color = new Color(0, 0, 0);
	}
	
	public void makeNextFrame(boolean isNextFrame) {
		this.nextFrame = isNextFrame;
	}
	
	public void makeLastFrame(boolean isLastFrame) {
		this.lastFrame = isLastFrame;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public boolean isNextFrame() {
		return nextFrame;
	}
	
	public boolean isLastFrame() {
		return lastFrame;
	}
	
	public io.cassaundra.rocket.Color getLaunchpadColor() {
		return new io.cassaundra.rocket.Color(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public String toString() {
		return "Pixel [color=" + color.toString() + ", nextFrame=" + nextFrame + ", lastFrame=" + lastFrame + "]";
	}
}
