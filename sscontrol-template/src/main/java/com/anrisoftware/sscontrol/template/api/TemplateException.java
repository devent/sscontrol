package com.anrisoftware.sscontrol.template.api;

import org.apache.commons.lang3.exception.ContextedException;

/**
 * Thrown if there was an error processing a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@SuppressWarnings("serial")
public class TemplateException extends ContextedException {

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateException(String message) {
		super(message);
	}

	@Override
	public TemplateException addContextValue(String label, Object value) {
		super.addContextValue(label, value);
		return this;
	}
}
