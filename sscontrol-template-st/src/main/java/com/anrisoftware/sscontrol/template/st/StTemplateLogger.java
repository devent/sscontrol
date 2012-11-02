/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-template-st.
 *
 * sscontrol-template-st is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-template-st is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-template-st. If not, see <http://www.gnu.org/licenses/>.
 */
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
