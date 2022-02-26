package de.Ryeera.DragoNoises;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

import de.Ryeera.DragoNoises.Soundboard.Soundboard;

public class Utils {

	/**
	 * Converts a number of bytes to a more readable form, i.e., 17843 Bytes
	 * will be converted to 17.4 kB.
	 *
	 * @param bytes
	 *            The byte count to be converted
	 * @return The formatted and shortened version of this number with suffix
	 */
	public static String convertBytes(long bytes) {
		int unit = 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		char pre = "kMGTPE".charAt(exp - 1);
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	/**
	 * Calculates and formats the time in milliseconds to a readable date.
	 *
	 * @param millisecs
	 *            Acquired from {@link System#currentTimeMillis()}
	 * @return A timestamp in the form of dd.MM.yy HH:mm:ss
	 */
	public static String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}
	
	public static Image createImage(String path) {
		URL imageURL = Soundboard.class.getResource(path);
		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else
			return new ImageIcon(imageURL, "").getImage();
	}
	
	public static ImageIcon createImageIcon(String path) {
		URL imageURL = Soundboard.class.getResource(path);
		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else
			return new ImageIcon(imageURL, "");
	}
	
	/**
	 * Copies a file from the specified source to the specified destination.
	 * 
	 * @param  source      The source-file
	 * @param  dest        The destination
	 * @throws IOException if either the source of the destination has a problem (No permission, file does not exist etc.)
	 */
	public static void copyFile(File source, File dest) throws IOException {
		dest.getParentFile().mkdirs();
		try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
			byte[] buffer = new byte[1024];
			int    length;
			while ((length = is.read(buffer)) > 0)
				os.write(buffer, 0, length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}