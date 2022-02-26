package de.Ryeera.DragoNoises;

import java.awt.Color;
import java.io.Serializable;

import org.json.JSONObject;

import de.Ryeera.DragoNoises.Soundboard.Soundboard;
import io.cassaundra.rocket.Pad;

public class Frame implements Serializable {

	private static final long serialVersionUID = -4853078002882956724L;
	private Pixel[][] pixels = new Pixel[9][9];
	
	public Frame() {
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				pixels[x][y] = new Pixel();
				pixels[x][y].makeLastFrame(true);
			}
		}
	}
	
	public Frame(Pixel[][] pixels) {
		this.pixels = pixels;
	}
	
	public Pixel getPixel(int x, int y) {
		return pixels[x][y];
	}
	
	public void setPixel(int x, int y, Pixel p) {
		pixels[x][y] = p;
	}
	
	public void invertX() {
		Pixel[][] newFrame = new Pixel[9][9];
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				newFrame[x][y] = pixels[(x-8)*-1][y];
			}
		}
		pixels = newFrame;
	}
	
	public void invertY() {
		Pixel[][] newFrame = new Pixel[9][9];
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				newFrame[x][y] = pixels[x][(y-8)*-1];
			}
		}
		pixels = newFrame;
	}
	
	public Frame clone() {
		Frame copy = new Frame();
		for (int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				Pixel pixelCopy = new Pixel(pixels[x][y].getColor().getRed(), pixels[x][y].getColor().getGreen(), pixels[x][y].getColor().getBlue());
				pixelCopy.makeLastFrame(pixels[x][y].isLastFrame());
				pixelCopy.makeNextFrame(pixels[x][y].isNextFrame());
				copy.pixels[x][y] = pixelCopy;
			}
		}
		return copy;
	}
	
	public static Frame getCurrent() {
		Frame f = new Frame();
		for(Pad pad : Pad.getAll()) {
			f.setPixel(pad.getX(), pad.getY(), new Pixel(Soundboard.launchpad.getPadColor(pad).getRed(), Soundboard.launchpad.getPadColor(pad).getGreen(), Soundboard.launchpad.getPadColor(pad).getBlue()));
		}
		for(io.cassaundra.rocket.Button button : io.cassaundra.rocket.Button.getAll()) {
			f.setPixel(button.isTop() ? button.getCoord() : 8, button.isTop() ? 8 : (button.getCoord() - 7) * -1, new Pixel(Soundboard.launchpad.getButtonColor(button).getRed(), Soundboard.launchpad.getButtonColor(button).getGreen(), Soundboard.launchpad.getButtonColor(button).getBlue()));
		}
		return f;
	}
	
	public static Frame getNext() {
		Frame f = new Frame();
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if(x != 8 && y != 8) {
					JSONObject colorjson = Soundboard.soundboard.getJSONObject(String.valueOf(x) + String.valueOf(y)).getJSONObject("off-color");
					Color c = new Color(colorjson.getInt("r"), colorjson.getInt("g"), colorjson.getInt("b"));
					f.setPixel(x, y, new Pixel(c));
				} else {
					if(Soundboard.config.getInt("current-soundboard") == 1 && x == 0)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(Soundboard.config.getInt("current-soundboard") == 2 && x == 1)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(Soundboard.config.getInt("current-soundboard") == 3 && x == 2)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(Soundboard.config.getInt("current-soundboard") == 4 && x == 3)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(Soundboard.config.getInt("current-soundboard") == 5 && x == 4)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(Soundboard.config.getInt("current-soundboard") == 6 && x == 5)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(Soundboard.config.getInt("current-soundboard") == 7 && x == 6)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(Soundboard.config.getInt("current-soundboard") == 8 && x == 7)
						f.setPixel(x, y, new Pixel(new Color(0, 0, 255)));
					else if(y == 3)
						f.setPixel(x, y, new Pixel(new Color(255, 0, 0)));
				}
			}
		}
		return f;
	}
}
