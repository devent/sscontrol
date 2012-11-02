package com.anrisoftware.sscontrol.template.st;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.compiler.STException;
import org.stringtemplate.v4.misc.STMessage;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.template.api.TemplateException;

/**
 * Logging messages for {@link StTemplate}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class StTemplateLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link StTemplate}.
	 */
	public StTemplateLogger() {
		super(StTemplate.class);
	}

	TemplateException errorOpenGroupFile(StTemplate template, STException e) {
		TemplateException ex = new TemplateException("Error open group file", e);
		ex.addContextValue("template", template);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	TemplateException errorProcessTemplate(StTemplate template, STMessage msg) {
		TemplateException ex = new TemplateException(
				"Error processing template", msg.cause);
		ex.addContextValue("template", template);
		ex.addContextValue("message", msg.toString());
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	void templateProcessed(StTemplate template) {
		log.trace("Processed template {}.", template);
	}

	void checkTemplateCreated(StTemplate template, ST st)
			throws TemplateException {
		if (st == null) {
			TemplateException ex = new TemplateException("No template created");
			ex.addContextValue("template", template);
			throw ex;
		}
	}
}
