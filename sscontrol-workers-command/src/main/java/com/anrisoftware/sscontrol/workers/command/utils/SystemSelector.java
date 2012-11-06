package com.anrisoftware.sscontrol.workers.command.utils;

/**
 * Selects the operating system based on the system property.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public enum SystemSelector {

	/**
	 * Unix operating system.
	 */
	UNIX("unix"),

	/**
	 * Windows operating system.
	 */
	WINDOWS("windows"),

	/**
	 * MacOS operating system.
	 */
	MAC("max"),

	/**
	 * Solaris operating system.
	 */
	SOLARIS("solaris"),

	/**
	 * Unknown operating system.
	 */
	UNKNOWN("unknown");

	/**
	 * Selects the operating system based on the system property.
	 * 
	 * @return one of the {@link SystemSelector} enumeration.
	 */
	public static SystemSelector getSystem() {
		String os = System.getProperty("os.name").toLowerCase();
		if (isWindows(os)) {
			return WINDOWS;
		} else if (isMac(os)) {
			return MAC;
		} else if (isUnix(os)) {
			return UNIX;
		} else if (isSolaris(os)) {
			return SOLARIS;
		} else {
			return UNKNOWN;
		}
	}

	private static boolean isSolaris(String string) {
		return (string.indexOf("sunos") >= 0);
	}

	private static boolean isUnix(String string) {
		return (string.indexOf("nix") >= 0 || string.indexOf("nux") >= 0);
	}

	private static boolean isMac(String string) {
		return (string.indexOf("mac") >= 0);
	}

	private static boolean isWindows(String string) {
		return (string.indexOf("win") >= 0);
	}

	private final String name;

	private SystemSelector(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
