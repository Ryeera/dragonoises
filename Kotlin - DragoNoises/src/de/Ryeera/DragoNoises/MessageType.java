package de.Ryeera.DragoNoises;

/**
 * Types of messages the TrayIcon can display
 *
 * @author Ryeera
 */
public enum MessageType {
	
	/**
	 * An info-message. No problem is being present, it only contains useful information.
	 */
	INFO,
	
	/**
	 * A warning-message. There is a problem, which needs the users attention.
	 */
	WARNING,
	
	/**
	 * An error-message. There is a problem so severe that the server cannot continue to run properly.
	 */
	ERROR;
}