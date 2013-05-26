/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Uses the postfix service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class PostfixScript extends LinuxScript {

	@Inject
	PostfixScriptLogger log

	@Inject
	@Named("postfix-properties")
	ContextProperties postfixProperties

	@Inject
	BindAddressesRenderer bindAddressesRenderer

	/**
	 * The {@link Templates} for the script.
	 */
	Templates postfixTemplates

	TemplateResource mainTemplate

	@Override
	def run() {
		super.run()
		postfixTemplates = templatesFactory.create "Postfix", templatesAttributes
		mainTemplate = postfixTemplates.getResource("main_configuration")
		deployMailname()
		deployMain()
		distributionSpecificConfiguration()
	}

	/**
	 * Returns additional template attributes.
	 */
	Map getTemplatesAttributes() {
		["renderers": [bindAddressesRenderer]]
	}

	/**
	 * Sets the domain name of the mail server. This is done usually in 
	 * the {@code /etc/mailname} file.
	 * 	
	 * @see #getMailname()
	 * @see #getCurrentMailnameConfiguration()
	 * @see #getMailnameConfiguration()
	 */
	void deployMailname() {
		deployConfiguration configurationTokens(), currentMailnameConfiguration, mailnameConfiguration, mailname
	}

	/**
	 * Returns the current mail name configuration. This is usually the 
	 * configuration file {@code /etc/mailname}.
	 * 	
	 * @see #getMailname()
	 */
	String getCurrentMailnameConfiguration() {
		currentConfiguration mailname
	}

	/**
	 * Returns the mail name configuration.
	 */
	List getMailnameConfiguration() {
		[
			new TokenTemplate(".*",
			postfixTemplates.getResource("mailname_configuration").
			getText(true, "name", service.domainName))
		]
	}

	/**
	 * Returns the mail name configuration file.
	 */
	abstract File getMailname()

	/**
	 * Sets the main configuration of the mail server. This is done usually in 
	 * the {@code /etc/postfix/main.cf} file.
	 * 	
	 * @see #getMain()
	 * @see #getCurrentMainConfiguration()
	 * @see #getMainConfiguration()
	 */
	void deployMain() {
		deployConfiguration configurationTokens(), currentMainConfiguration, mainConfiguration, main
	}

	/**
	 * Returns the current postfix main configuration. This is usually the
	 * configuration file {@code /etc/postfix/main.cf}.
	 *
	 * @see #getMain()
	 */
	String getCurrentMainConfiguration() {
		currentConfiguration main
	}

	/**
	 * Returns the current postfix main configuration.
	 * 
	 * @see #getBanner()
	 */
	List getMainConfiguration() {
		[
			new TokenTemplate("\\#?myhostname.*", mainTemplate.getText(true, "hostname", "mail", service)),
			new TokenTemplate("\\#?myorigin.*", mainTemplate.getText(true, "origin", "mail", service)),
			new TokenTemplate("\\#?smtpd_banner.*", mainTemplate.getText(true, "banner", "banner", banner)),
			new TokenTemplate("\\#?relayhost.*", mainTemplate.getText(true, "relayhost", "mail", service)),
			new TokenTemplate("\\#?inet_interfaces.*", mainTemplate.getText(true, "interfaces", "mail", service)),
		]
	}

	/**
	 * Returns the banner text that follows the 220 status code in the SMTP 
	 * greeting banner.
	 *
	 * <ul>
	 * <li>profile property {@code "banner"}</li>
	 * </ul>
	 */
	String getBanner() {
		profileProperty("banner", postfixProperties)
	}

	/**
	 * Returns the {@code main.cf} file.
	 */
	abstract File getMain()

	/**
	 * Run the distribution specific configuration.
	 */
	void distributionSpecificConfiguration() {
	}
}
