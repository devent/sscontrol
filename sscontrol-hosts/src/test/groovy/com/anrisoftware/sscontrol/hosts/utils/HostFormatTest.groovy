/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.utils

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.sscontrol.hosts.service.Host
import com.anrisoftware.sscontrol.hosts.service.HostFactory
import com.anrisoftware.sscontrol.hosts.service.HostsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test host format.
 * 
 * @see HostFormat
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HostFormatTest {

	static inputsOutputsFormat = [
		inputs: [
			[address: "127.0.0.1", name: "localhost", aliases: [], format: 'address:"127.0.0.1";name:"localhost";aliases:[]'],
			[address: "127.0.0.1", name: "localhost", aliases: ["localdomain"], format: 'address:"127.0.0.1";name:"localhost";aliases:["localdomain"]'],
			[address: "127.0.0.1", name: "localhost", aliases: ["localdomain", "localhost"], format: 'address:"127.0.0.1";name:"localhost";aliases:["localdomain","localhost"]'],
			[address: "127.0.0.1", name: "localhost", aliases: ["localdomain", "localhost"], format: 'address:"127.0.0.1";name:"localhost";aliases:["localdomain","localhost"]'],
		],
		outputs: [
			'address:"127.0.0.1";name:"localhost";aliases:[]',
			'address:"127.0.0.1";name:"localhost";aliases:["localdomain"]',
			'address:"127.0.0.1";name:"localhost";aliases:["localdomain","localhost"]',
			"address:'127.0.0.1';name:'localhost';aliases:['localdomain','localhost']",
		]
	]

	@Test
	void "format host"() {
		inputsOutputsFormat.inputs.eachWithIndex { it, int i ->
			def host = hostFactory.create(it.address)
			host.host(it.name).alias(it.aliases)
			def format = factory.create().format(host)
			log.info "Format: '{}'.", format
			assertStringContent format, inputsOutputsFormat.inputs[i].format
		}
	}

	@Test
	void "parse host"() {
		inputsOutputsFormat.outputs.eachWithIndex { it, int i ->
			Host parsed = factory.create().parse(it)
			log.info "Parsed: '{}'.", parsed
			assert parsed.address == inputsOutputsFormat.inputs[i].address
			assert parsed.hostname == inputsOutputsFormat.inputs[i].name
			assert parsed.aliases == inputsOutputsFormat.inputs[i].aliases
		}
	}

	static Injector injector

	static HostFormatFactory factory

	static HostFactory hostFactory

	@BeforeClass
	static void createFactory() {
		injector = Guice.createInjector(new HostsModule())
		factory = injector.getInstance(HostFormatFactory)
		hostFactory = injector.getInstance(HostFactory)
	}

	static {
		toStringStyle
	}
}
