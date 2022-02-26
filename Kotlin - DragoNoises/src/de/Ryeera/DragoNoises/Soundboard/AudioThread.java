package de.Ryeera.DragoNoises.Soundboard;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioThread extends Thread {

	private File soundFile;
	private float volume;
	private boolean running;
	
	public AudioThread(File soundFile, float volume) {
		this.soundFile = soundFile;
		this.volume = volume;
		this.running = true;
	}
	
	@Override
	public void run() {
		try (AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile.getAbsoluteFile())) {
			try (Clip clip = AudioSystem.getClip(Soundboard.mixer)){
				clip.open(inputStream);
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
				clip.start();
				do {
					sleep(100);
				} while (clip.isRunning() && running);
			} catch (IllegalArgumentException e) {
				Soundboard.reloadAudioSystem();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancel() {
		this.running = false;
	}
	
}
