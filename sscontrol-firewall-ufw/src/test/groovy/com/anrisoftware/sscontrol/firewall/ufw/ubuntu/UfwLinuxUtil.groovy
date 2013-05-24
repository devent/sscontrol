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
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Utilities to test the UFW service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UfwLinuxUtil {

	static firewallAllowService = UfwLinuxUtil.class.getResource("FirewallAllow.groovy")

	static firewallDenyService = UfwLinuxUtil.class.getResource("FirewallDeny.groovy")

	static echoCommand = UfwLinuxUtil.class.getResource("echo_command.txt")

	Injector injector

	File tmpdir

	Map variables

	File aptitude

	File ufw

	@Before
	void createTemp() {
		tmpdir = File.createTempDir this.class.simpleName, null
		aptitude = new File(tmpdir, "/usr/bin/aptitude")
		ufw = new File(tmpdir, "/usr/sbin/ufw")
		variables = [tmp: tmpdir.absoluteFile]
	}

	@After
	void deleteTemp() {
		tmpdir.deleteDir()
	}

	@Before
	void createFactories() {
		injector = createInjector()
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}

	static Injector createInjector() {
		Guice.createInjector(new CoreModule(), new CoreResourcesModule(),
				new UfwModule())
	}
}
