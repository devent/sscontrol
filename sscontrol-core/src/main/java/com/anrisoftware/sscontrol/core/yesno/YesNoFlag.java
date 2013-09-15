package com.anrisoftware.sscontrol.core.yesno;

/**
 * Yes/no flag.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum YesNoFlag {

	yes, no;

	/**
	 * Returns boolean value.
	 * 
	 * @return {@code true} if the flag is {@link #yes}.
	 */
	public boolean asBoolean() {
		return this == yes ? true : false;
	}
}
