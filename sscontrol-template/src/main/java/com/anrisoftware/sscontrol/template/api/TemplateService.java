package com.anrisoftware.sscontrol.template.api;

/**
 * Service that offers the template processing.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface TemplateService {

	void setParent(Object injector);

	TemplateServiceInfo getInfo();

	TemplateFactory getTemplate();
}
