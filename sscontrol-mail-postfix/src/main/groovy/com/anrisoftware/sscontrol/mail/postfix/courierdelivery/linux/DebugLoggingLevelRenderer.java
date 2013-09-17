package com.anrisoftware.sscontrol.mail.postfix.courierdelivery.linux;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.mail.debuglogging.DebugLoggingLevel;

/**
 * Courier debug logging level.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DebugLoggingLevelRenderer implements AttributeRenderer {

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		switch (((DebugLoggingLevel) o).intValue()) {
		case 0:
			return "0";
		case 1:
			return "1";
		default:
			return "2";
		}
	}

	@Override
	public Class<?> getAttributeType() {
		return DebugLoggingLevel.class;
	}

}
