package com.anrisoftware.sscontrol.hostname

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.core.activator.CoreModule
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.google.inject.Guice
import com.google.inject.Injector

class HostnameServiceTest extends TestUtils {

	static ubuntu1004Profile = TestUtils.resourceURL("Ubuntu_10_04Profile.groovy", HostnameServiceTest)

	static hostnameService = TestUtils.resourceURL("HostnameService.groovy", HostnameServiceTest)

	Injector injector

	Ubuntu_10_04Profile ubuntu_10_04Profile

	File tmp

	Map variables

	@Before
	void setupInjector() {
		injector = createInjector()
		tmp = createTempDirectory()
		variables = [tmp: tmp.absoluteFile]
	}

	def createInjector() {
		Guice.createInjector(new CoreModule())
	}

	@Test
	void "load hostname service"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(hostnameService, variables, registry, profile)
	}
}
