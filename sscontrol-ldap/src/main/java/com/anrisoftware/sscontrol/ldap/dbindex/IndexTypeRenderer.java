package com.anrisoftware.sscontrol.ldap.dbindex;

import java.io.Serializable;
import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;

/**
 * Renderer for database index type.
 * 
 * @see IndexType
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class IndexTypeRenderer implements AttributeRenderer, Serializable {

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		IndexType type = (IndexType) o;
		switch (type) {
		case approx:
			return "approx";
		case equality:
			return "eq";
		case present:
			return "pres";
		case substring:
			return "sub";
		case none:
			return "none";
		}
		return o.toString();
	}

}
