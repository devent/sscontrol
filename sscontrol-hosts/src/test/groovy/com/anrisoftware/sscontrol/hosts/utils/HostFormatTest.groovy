package com.anrisoftware.sscontrol.hosts.utils

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.sscontrol.hosts.service.HostFactory
import com.anrisoftware.sscontrol.hosts.service.HostsModule
import com.google.inject.Guice
import com.google.inject.Injector

@Slf4j
class HostFormatTest {

	static inputsOutputsFormat = [
		inputs: [
			[address: "127.0.0.1", name: "localhost", aliases: []],
			[address: "127.0.0.1", name: "localhost", aliases: ["localdomain"]],
			[address: "127.0.0.1", name: "localhost", aliases: ["localdomain", "localhost"]],
		],
		outputs: [
			'address:"127.0.0.1",name:"localhost",aliases:[]',
			'address:"127.0.0.1",name:"localhost",aliases:["localdomain"]',
			'address:"127.0.0.1",name:"localhost",aliases:["localdomain","localhost"]',
		]
	]

	@Test
	void "format host"() {
		inputsOutputsFormat.inputs.eachWithIndex { it, int i ->
			def host = hostFactory.create(it.address).hostname(it.name).alias(it.aliases)
			def format = factory.create().format(host)
			log.info "Format: '{}'.", format
			assertStringContent format, inputsOutputsFormat.outputs[i]
		}
	}

	static inputsOutputsParse = [
		inputs: [
			'address:"127.0.0.1",name:"localhost",aliases:[]',
			'address:"127.0.0.1",name:"localhost",aliases:["localdomain"]',
			'address:"127.0.0.1",name:"localhost",aliases:["localdomain","localhost"]',
		]]

	@Test
	void "parse host"() {
		inputsOutputsParse.inputs.eachWithIndex { it, int i ->
			def parsed = factory.create().parse(it)
			log.info "Parsed: '{}'.", parsed
			assert parsed == inputsOutputsParse.outputs[i]
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
		inputsOutputsParse << [outputs: [
				hostFactory.create("127.0.0.1").hostname("localhost").alias(),
				hostFactory.create("127.0.0.1").hostname("localhost").alias("localdomain"),
				hostFactory.create("127.0.0.1").hostname("localhost").alias("localdomain","localhost"),
			]
		]
	}

	static {
		toStringStyle
	}
}
