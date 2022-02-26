package de.Ryeera.DragoNoises.Soundboard;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.Ryeera.DragoNoises.Frame;
import de.Ryeera.DragoNoises.Pixel;
import de.Ryeera.libs.JSONUtils;

public class Animation implements Serializable {
	
	public static Animation STOP;
	public static Animation START;
	public static Animation TRANSITION1;
	public static Animation TRANSITION2;

	private static final long serialVersionUID = 65165165816518L;
	private List<Frame> frames = new ArrayList<>();
	private int mspf;
	private int fps;
	
	public Animation(int fps) {
		this.fps = fps;
		this.mspf = 1000/fps;
	}
	
	public void play() {
		Soundboard.transitioning = true;
		Frame current = Frame.getCurrent();
		Frame next = Frame.getNext();
		long start = System.currentTimeMillis();
		int frameno = 0;
		for(Frame frame : frames) {
			Soundboard.applyFrame(frame, current, next);
			frameno++;
			while(System.currentTimeMillis()-start < frameno*mspf) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Soundboard.transitioning = false;
	}
	
	public void addFrame(Frame frame) {
		frames.add(frame);
	}
	
	public List<Frame> getFrames() {
		return frames;
	}
	
	public Frame removeFrame(int frameID) {
		return frames.remove(frameID);
	}
	
	public int getFPS() {
		return fps;
	}
	
	public void setFPS(int fps) {
		this.fps = fps;
		this.mspf = 1000/fps;
	}
	
	public boolean save(File file) {
		try (
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		){
			objectOut.writeObject(this);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveRaw(File file) {
		JSONObject animation = new JSONObject();
		animation.put("fps", fps);
		JSONObject framesj = new JSONObject();
		for (int i = 0; i < frames.size(); i++) {
			JSONObject frame = new JSONObject();
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					JSONObject pixel = new JSONObject();
					pixel.put("c", frames.get(i).getPixel(x, y).getColor().getRGB());
					pixel.put("a", frames.get(i).getPixel(x, y).isLastFrame());
					pixel.put("b", frames.get(i).getPixel(x, y).isNextFrame());
					frame.put(x + "" + y, pixel);
				}
			}
			framesj.put(String.valueOf(i), frame);
		}
		animation.put("frames", framesj);
		try {
			JSONUtils.writeJSON(animation, file);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Animation importAnimation(File file) {
		Animation in = new Animation(30);
		try (
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		) {
			Object obj = objectIn.readObject();
			in = (Animation) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
	
	public static Animation importAnimation(JSONObject animationJSON) {
		Animation returns = new Animation(animationJSON.getInt("fps"));
		JSONObject frames = animationJSON.getJSONObject("frames");
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
			returns.frames.add(f);
		}
		return returns;
	}
}
