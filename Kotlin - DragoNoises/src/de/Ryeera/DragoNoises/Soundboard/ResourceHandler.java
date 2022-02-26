package de.Ryeera.DragoNoises.Soundboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceHandler {
	public void copyFileFromResource(String file){
		try (
				InputStream inputStream = getClass().getResourceAsStream("/de/Ryeera/DragoNoises/Resources/" + file);
				OutputStream outputStream = new FileOutputStream(new File(System.getenv("APPDATA") + "/DragoNoises/" + file));
		) {
			int read = 0;
			byte[] bytes = new byte[1024];
			while((read = inputStream.read(bytes)) != -1)
				outputStream.write(bytes, 0, read);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
