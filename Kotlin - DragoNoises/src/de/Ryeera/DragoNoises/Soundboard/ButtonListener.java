package de.Ryeera.DragoNoises.Soundboard;

import java.io.File;

import org.json.JSONObject;

import io.cassaundra.rocket.Button;
import io.cassaundra.rocket.Color;
import io.cassaundra.rocket.LaunchpadListener;
import io.cassaundra.rocket.Pad;

public class ButtonListener implements LaunchpadListener {

	@Override
	public void onButtonDown(Button button) {
		if(button.isTop()) {
			Soundboard.switchSoundboard(button.getCoord()+1);
		} else {
			if(button.getCoord() == 0) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Volume");
			} else if(button.getCoord() == 1) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Pan");
			} else if(button.getCoord() == 2) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Send A");
			} else if(button.getCoord() == 3) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Send B");
			} else if(button.getCoord() == 4) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Stop");
				Soundboard.exit();
			} else if(button.getCoord() == 5) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Mute");
				for(AudioThread t : Soundboard.currentlyPlaying)
					t.cancel();
			} else if(button.getCoord() == 6) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Solo");
			} else if(button.getCoord() == 7) {
				Soundboard.log("ButtonListener", LogLevel.DEBUG, "Button Pressed: Record Aim");
			} 
		}
	}

	@Override
	public void onButtonUp(Button button) {}

	@Override
	public void onPadDown(Pad pad) {
		JSONObject buttonconf = Soundboard.soundboard.getJSONObject(String.valueOf(pad.getX()) + String.valueOf(pad.getY()));
		JSONObject color = buttonconf.getJSONObject("on-color");
		Soundboard.launchpad.setPad(pad, new Color(color.getInt("r"), color.getInt("g"), color.getInt("b")));
		if(!buttonconf.getString("soundfile").equals("")) {
			try {
				Soundboard.playSound(new File(buttonconf.getString("soundfile")), buttonconf.getInt("vol")/100.0f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPadUp(Pad pad) {
		JSONObject buttonconf = Soundboard.soundboard.getJSONObject(String.valueOf(pad.getX()) + String.valueOf(pad.getY()));
		JSONObject color = buttonconf.getJSONObject("off-color");
		Soundboard.launchpad.setPad(pad, new Color(color.getInt("r"), color.getInt("g"), color.getInt("b")));
	}
}
