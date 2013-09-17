package com.anrisoftware.sscontrol.mail.debuglogging;

/**
 * Debug logging level.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DebugLoggingLevel extends Number {

	/**
	 * Deactivates all logging.
	 */
	public static final DebugLoggingLevel OFF = new DebugLoggingLevel(0);

	private final int level;

	public DebugLoggingLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public int intValue() {
		return level;
	}

	@Override
	public long longValue() {
		return level;
	}

	@Override
	public float floatValue() {
		return level;
	}

	@Override
	public double doubleValue() {
		return level;
	}

}
