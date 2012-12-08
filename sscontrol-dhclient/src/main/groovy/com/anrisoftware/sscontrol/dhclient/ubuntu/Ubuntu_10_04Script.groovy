package com.anrisoftware.sscontrol.dhclient.ubuntu

import groovy.util.logging.Slf4j

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.dhclient.linux.LinuxScript
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenMarker
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory

/**
 * Setups the dhclient service on a Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_10_04Script extends LinuxScript {

	Templates commandTemplates

	@Override
	def run() {
		super.run()
		templates = templatesFactory.create "Dhclient_$name"
		commandTemplates = templatesFactory.create "ScriptCommandTemplates"
		installPackages()
		deployConfiguration()
		restartService()
	}

	/**
	 * Installs the needed dhclient packages.
	 */
	def installPackages() {
		def template = commandTemplates.getResource("install")
		def worker = workers[ScriptCommandWorkerFactory].create(
						template, "installCommand", system.install_command,
						"packages", profile.packages)()
		logInstallPackagesDone(worker)
	}

	private logInstallPackagesDone(def worker) {
		if (log.isDebugEnabled()) {
			log.debug "Installed dhclient service packages for {}: {}.", this, profile.packages
		} else {
			log.info "Installed dhclient service packages: {}.", profile.packages
		}
	}

	/**
	 * Deploys the dhclient configuration to the dhclient configuration file.
	 */
	def deployConfiguration() {
		def configuration = currentConfiguration
		tokenTemplate.each {
			def worker = workers[TokensTemplateWorkerFactory].create(tokens, it, currentConfiguration)()
			configuration = worker.text
			FileUtils.write(configurationFile, worker.text, system.charset)
			logDeployConfiguration(configuration)
		}
	}

	/**
	 * Returns the dhclient configuration directory.
	 */
	File getConfigurationDir() {
		profile.configuration_dir as File
	}

	/**
	 * Returns the dhclient configuration file.
	 */
	File getConfigurationFile() {
		new File(configurationDir, "dhclient.conf")
	}

	/**
	 * Returns the dhclient configuration.
	 */
	List getConfiguration() {
		def res = templates.getResource("dhclient_configuration")
		def list = []
		service.options.inject(list) { it, option ->
			res.invalidate()
			it << res.getText("option", "declaration", option)
		}
		service.sends.inject(list) { it, send ->
			res.invalidate()
			it << res.getText("send", "declaration", send)
		}
		list << res.getText("requests", "requests", service.requests)
		service.prepends.inject(list) { it, prepend ->
			res.invalidate()
			it << res.getText("prepend", "declaration", prepend)
		}
	}

	/**
	 * Returns the dhclient configuration on the server.
	 */
	String getCurrentConfiguration() {
		if (configurationFile.isFile()) {
			FileUtils.readFileToString(configurationFile, system.charset)
		} else {
			logNoConfigurationFound()
			""
		}
	}

	private logNoConfigurationFound() {
		if (log.isDebugEnabled()) {
			log.debug "No dhclient configuration file found {} for {}.", configurationFile, this
		} else {
			log.info "No dhclient configuration found in {}.", configurationFile
		}
	}

	/**
	 * Returns the template tokens for the hostname configuration.
	 */
	TokenMarker getTokens() {
		new TokenMarker("# SSCONTROL-$serviceName", "# SSCONTROL-$serviceName-END\n")
	}

	/**
	 * Returns the token templates for each dhclient configuration.
	 */
	List getTokenTemplate() {
		int i = 0
		def list = []
		service.options.inject([]) { it, option ->
			it << new TokenTemplate("option .*", configuration[i++])
		}
		service.sends.inject(list) { it, send ->
			it << new TokenTemplate("send .*", configuration[i++])
		}
		list << new TokenTemplate("request .*", configuration[i++])
		service.prepends.inject(list) { it, prepend ->
			it << new TokenTemplate("prepend .*", configuration[i++])
		}
	}

	private logDeployConfiguration(def configuration) {
		if (log.isDebugEnabled()) {
			log.debug "Deploy hostname configuration '$configuration' to {} in {}.", configurationFile, this
		} else {
			log.info "Deploy dhclient configuration to {}.", configurationFile
		}
	}

	def restartService() {
	}

	private logRestartDone(def worker) {
		if (log.isDebugEnabled()) {
			log.debug "Restart service done with output for {}: '{}'", this, worker.out
		} else {
			log.info "Restarted dhclient service."
		}
	}
}
