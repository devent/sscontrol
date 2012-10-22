package com.anrisoftware.sscontrol.hostname.ubuntu

def getHostnameConfiguration() {
	def temp = templates.create "Hostname"
	def res = temp.getResource("configuration_ubuntu_10_04")
	def text = res.getText("hostname", service.hostname)
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
