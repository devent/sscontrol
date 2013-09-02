/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.statements.auth.Auth
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.domain.SslDomain

/**
 * Configures Apache 2.2 service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Apache_2_2Script extends ApacheScript {

	@Inject
	Apache_2_2ScriptLogger log

	@Inject
	FileAuthProvider fileAuthProvider

	@Inject
	SslDomainConfig sslDomainConfig

	/**
	 * The {@link Templates} for the script.
	 */
	Templates apacheTemplates

	/**
	 * Resource containing the Apache configuration templates.
	 */
	TemplateResource configTemplate

	/**
	 * Resource containing the Apache commands templates.
	 */
	TemplateResource commandsTemplate

	@Override
	def run() {
		apacheTemplates = templatesFactory.create "Apache_2_2"
		configTemplate = apacheTemplates.getResource "config"
		commandsTemplate = apacheTemplates.getResource "commands"
		fileAuthProvider.script = this
		sslDomainConfig.script = this
		super.run()
		deployDefaultConfig()
		deployDomainsConfig()
		deployConfig()
		deployAuths()
		restartServices()
	}

	def deployDefaultConfig() {
		def string = configTemplate.getText true, "defaultConfiguration"
		FileUtils.write defaultConfigFile, string
	}

	def deployDomainsConfig() {
		def string = configTemplate.getText true, "domainsConfiguration", "service", service
		FileUtils.write domainsConfigFile, string
	}

	def deployConfig() {
		service.domains.each {
			domainDir(it).mkdirs()
			def string = configTemplate.getText true, it.class.simpleName, "properties", this, "domain", it
			FileUtils.write new File(sitesAvailableDir, it.fileName), string
			enableDomain it
			if (it.class == SslDomain) {
				sslDomainConfig.deployCertificates(it)
			}
		}
	}

	def enableDomain(Domain domain) {
		def worker = scriptCommandFactory.create commandsTemplate, "enableSite",
				"command", enableSiteCommand,
				"site", domain.fileName
		worker()
		log.enabledDomain this, worker, domain
	}

	def deployAuths() {
		service.domains.each { Domain domain ->
			domain.auths.each { Auth auth ->
				fileAuthProvider.deployAuth domain, auth
			}
		}
	}
}
