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
		def group = new DecimalFormat(groupPattern).format(domainNumber)
		def user = new DecimalFormat(userPattern).format(domainNumber)
		addSiteGroup group
		addSiteUser domain, user, group
		createWebDir domain, user, group
	}

	private createWebDir(Domain domain, String user, String group) {
		webDir(domain).mkdirs()
		script.changeOwner user, group, [webDir(domain)]
	}

	private addSiteUser(Domain domain, String user, String group) {
		int uid = minimumUid + domainNumber
		def home = domainDir domain
		def shell = "/bin/false"
		addUser user, group, uid, home, shell
	}

	private addSiteGroup(String group) {
		int gid = minimumGid + domainNumber
		addGroup group, gid
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
