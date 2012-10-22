package com.anrisoftware.sscontrol.hostname.ubuntu

def getHostnameConfiguration() {
}

def isHostnameSet() {
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
