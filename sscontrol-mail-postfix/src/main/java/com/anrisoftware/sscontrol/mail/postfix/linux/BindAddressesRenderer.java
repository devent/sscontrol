package com.anrisoftware.sscontrol.mail.postfix.linux;

import static com.anrisoftware.sscontrol.mail.statements.BindAddresses.ALL;
import static com.anrisoftware.sscontrol.mail.statements.BindAddresses.LOOPBACK;
import static org.apache.commons.lang3.StringUtils.join;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.mail.statements.BindAddresses;

/**
 * Renderer for postfix bind addresses.
 * 
 * @see BindAddresses#ALL
 * @see BindAddresses#LOOPBACK
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class BindAddressesRenderer implements AttributeRenderer {

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		return format((BindAddresses) o);
	}

	private String format(BindAddresses addresses) {
		if (addresses == ALL) {
			return "all";
		}
		if (addresses == LOOPBACK) {
			return "loopback-only";
		}
		return join(addresses.getAddresses(), ',');
	}

	@Override
	public Class<?> getAttributeType() {
		return BindAddresses.class;
	}

}
