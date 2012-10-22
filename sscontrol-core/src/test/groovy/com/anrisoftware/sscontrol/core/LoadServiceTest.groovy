/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.core.activator.CoreModule
import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.core.api.ProfileService
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Load a service from a groovy script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class LoadServiceTest extends TestUtils {

	static ubuntu1004Profile = TestUtils.resourceURL("Ubuntu_10_04Profile.groovy", LoadServiceTest)

	Injector injector

	@Before
	void setupInjector() {
		injector = createInjector()
	}

	def createInjector() {
		Guice.createInjector(new CoreModule())
	}

	@Test
	void "load profile script"() {
		def variables = [one: "one", two: "two", three: "three"]
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry)
		assert registry.serviceNames.toString() == "[profile]"
		assert registry.getService("profile").size() == 1
		ProfileService profile = registry.getService("profile")[0]
		assert profile.entryNames.toString() == "[system]"
		ProfileProperties system = profile.getEntry("system")
		system.propertyKeys.size() == 8
		assert system.getProperty("echo_command") == "echo"
		assert system.getProperty("install_command") == "aptitude update && aptitude install {}"
		assert system.getProperty("set_enabled") == true
		assert system.getProperty("set_gstring") == "gstring test"
		assert system.getProperty("set_multiple") == ["aaa", "bbb"]
		assert system.getProperty("set_number") == 11
		assert system.getProperty("set_method_enabled") == true
		assert system.getProperty("property_with_variables") == "one two three"
	}

}
