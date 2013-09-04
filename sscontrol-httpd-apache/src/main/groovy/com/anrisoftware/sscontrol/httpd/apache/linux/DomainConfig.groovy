package com.anrisoftware.sscontrol.httpd.apache.linux

import static java.lang.String.format

import java.text.DecimalFormat

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

class DomainConfig {

	int domainNumber

	ApacheScript script

	DomainConfig() {
		this.domainNumber = 0
	}

	def deployDomain(Domain domain) {
		domainNumber++
		int gid = minimumGid + domainNumber
		def group = new DecimalFormat(groupPattern).format(domainNumber)
		int uid = minimumUid + domainNumber
		def user = new DecimalFormat(userPattern).format(domainNumber)
		def home = domainDir domain
		def shell = "/bin/false"
		webDir(domain).mkdirs()
		addGroup group, gid
		addUser user, group, uid, home, shell
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
