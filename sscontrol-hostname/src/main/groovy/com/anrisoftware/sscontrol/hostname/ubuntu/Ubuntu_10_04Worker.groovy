package com.anrisoftware.sscontrol.hostname.ubuntu

import com.anrisoftware.resources.api.TemplateResource
import com.anrisoftware.resources.api.Templates

def getHostnameConfiguration() {
	Templates temp = templates.create "Hostname"
	TemplateResource res = temp.getResource("configuration_ubuntu_10_04")
	println properties
	def text = res.getText("hostname", properties.hostname)
	println text
}

def getHostnameSet() {
	getHostnameConfiguration()
	return true
}

def deployHostnameConfiguration() {
}

def checkHostnameConfiguration() {
}

if (hostnameSet) {
	return
}

deployHostnameConfiguration()
checkHostnameConfiguration()
