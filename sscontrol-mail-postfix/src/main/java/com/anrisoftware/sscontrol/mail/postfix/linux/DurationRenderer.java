package com.anrisoftware.sscontrol.mail.postfix.linux;

import static java.lang.String.format;

import java.util.Locale;

import org.joda.time.Duration;

import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * Attribute renderer for time duration.
 * 
 * @see Duration
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DurationRenderer implements AttributeRenderer {

	private static final int MINUTES = 60;
	private static final int HOUR = 60 * MINUTES;
	private static final int DAYS = 24 * HOUR;
	private static final int WEEKS = 7 * DAYS;

	@Override
	public String toString(Object o, String format, Locale locale) {
		Duration duration = (Duration) o;
		long time = duration.getMillis() / 1000;
		if (time > 1 * WEEKS && time % WEEKS == 0) {
			return format("%dw", time / WEEKS);
		}
		if (time > 1 * DAYS && time % DAYS == 0) {
			return format("%dd", time / DAYS);
		}
		if (time > 1 * HOUR && time % HOUR == 0) {
			return format("%dh", time / HOUR);
		}
		if (time > 1 * MINUTES && time % MINUTES == 0) {
			return format("%dm", time / MINUTES);
		}
		return format("%ds", time);
	}

	@Override
	public Class<?> getAttributeType() {
		return Duration.class;
	}

}
