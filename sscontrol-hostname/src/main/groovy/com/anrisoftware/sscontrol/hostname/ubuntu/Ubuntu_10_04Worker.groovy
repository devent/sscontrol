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

import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenMarker
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory


template = templates.create "Hostname_$name"

File getConfigurationFile() {
	profile.configuration_file as File
}

def getConfiguration() {
	def res = template.getResource("hostname_configuration")
	res.getText("hostname", service.hostname)
}

def getText() {
	if (configurationFile.isFile()) {
		def config = configuration
		FileUtils.readFileToString(configurationFile, system.charset)
	} else {
		log.info "No file {} found in {}.", configurationFile, this
		""
	}
}

def getTokens() {
	new TokenMarker("# SSCONTROL-$name", "# SSCONTROL-$name-END\n")
}

def getTokenTemplate() {
	new TokenTemplate(".*", configuration)
}

def deployHostnameConfiguration() {
	def worker = workers[TokensTemplateWorkerFactory].create(tokens, tokenTemplate, text)()
	FileUtils.write(configurationFile, worker.text, system.charset)
	log.info "Deploy hostname configuration '$worker.text' to {} in {}.", configurationFile, this
}

def restartHostnameService() {
	def template = template.getResource("restart_hostname_command")
	def worker = workers[ScriptCommandWorkerFactory].create(template, "prefix", system.prefix)()
	log.info "Restart done with output '{}'.", worker.out
}

String toString() {
	"${service.toString()}: $name"
}

deployHostnameConfiguration()
restartHostnameService()
