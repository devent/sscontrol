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
package com.anrisoftware.sscontrol.hostname.ubuntu

import org.apache.commons.io.FileUtils

import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenMarker
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory


template = templates.create "Hosts_$name"

/**
 * Returns the hosts configuration file.
 */
File getConfigurationFile() {
	profile.configuration_file as File
}

/**
 * Returns the hosts configuration.
 */
String getConfiguration() {
	def res = template.getResource("hosts_configuration")
	res.getText("hosts", service.hosts)
}

/**
 * Returns the current hosts configuration.
 */
String getCurrentConfiguration() {
	if (configurationFile.isFile()) {
		def config = configuration
		FileUtils.readFileToString(configurationFile, system.charset)
	} else {
		log.info "No file {} found in {}.", configurationFile, this
		""
	}
}

/**
 * Returns the template tokens for the hosts configuration.
 */
TokenMarker getTokens() {
	new TokenMarker("# SSCONTROL-$name", "# SSCONTROL-$name-END\n")
}

/**
 * Returns the token template for the hosts configuration.
 */
TokenTemplate getTokenTemplate() {
	new TokenTemplate(".*", configuration)
}

/**
 * Deploys the hosts configuration to the hosts configuration file.
 */
def deployConfiguration() {
	def worker = workers[TokensTemplateWorkerFactory].create(tokens, tokenTemplate, text)()
	FileUtils.write(configurationFile, worker.text, system.charset)
	log.info "Deploy hosts configuration '$worker.text' to {} in {}.", configurationFile, this
}

String toString() {
	"${service.toString()}: $name"
}

deployConfiguration()
