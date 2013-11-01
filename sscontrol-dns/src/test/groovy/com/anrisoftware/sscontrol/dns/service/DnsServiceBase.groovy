package com.anrisoftware.sscontrol.dns.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.core.service.ServiceModule
import com.anrisoftware.sscontrol.dns.arecord.ARecord
import com.anrisoftware.sscontrol.dns.cnamerecord.CNAMERecord
import com.anrisoftware.sscontrol.dns.mxrecord.MXRecord
import com.anrisoftware.sscontrol.dns.nsrecord.NSRecord
import com.anrisoftware.sscontrol.dns.zone.DnsZone
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * DNS/service base.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsServiceBase {

	static Injector injector

	static ServiceLoaderFactory loaderFactory

	Map variables

	ServicesRegistry registry

	SscontrolServiceLoader loader

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	@Before
	void createRegistry() {
		variables = [tmp: tmp.newFolder()]
		registry = injector.getInstance ServicesRegistry
		loader = loaderFactory.create registry, variables
		loader.setParent injector
	}

	@BeforeClass
	static void createFactories() {
		injector = createInjector()
		loaderFactory = injector.getInstance ServiceLoaderFactory
	}

	static Injector createInjector() {
		Guice.createInjector(
				new CoreModule(), new CoreResourcesModule(), new ServiceModule())
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}

	def DnsServiceImpl assertService(Map args, DnsServiceImpl service) {
		//assert service.generate == args.generate
		assert service.serial == args.serial
		assert service.binding.addresses.containsAll(args.binding)
		service
	}

	def DnsServiceImpl assertServiceGenerateSerial(Map args, DnsServiceImpl service) {
		assert service.serial > args.serial
		assert service.binding.addresses.containsAll(args.binding)
		service
	}

	def DnsZone assertZone(Map args, DnsZone zone) {
		assertStringContent zone.name, args.name
		assertStringContent zone.primaryNameServer, args.primary
		assertStringContent zone.email, args.email
		if (args.containsKey("serial")) {
			assert zone.serial == args.serial
		}
		if (args.containsKey("ttl")) {
			assert zone.ttl == args.ttl
		}
		zone
	}

	def assertARecord(Map args, ARecord arecord) {
		assertStringContent arecord.name, args.name
		assertStringContent arecord.address, args.address
		if (args.containsKey("ttl")) {
			assert arecord.ttl.millis == args.ttl
		}
	}

	def assertCNAMERecord(CNAMERecord cnamerecord, Object... args) {
		assertStringContent cnamerecord.name, args[0]
		assertStringContent cnamerecord.alias, args[1]
		assert cnamerecord.ttl.millis == args[2]*1000
	}

	def assertMXRecord(MXRecord mxrecord, Object... args) {
		assertStringContent mxrecord.name, args[0]
		assert mxrecord.aRecord == args[1]
		assert mxrecord.priority == args[2]
		assert mxrecord.ttl.millis == args[3]*1000
	}

	def assertNSRecord(NSRecord nsrecord, Object... args) {
		assertStringContent nsrecord.name, args[0]
		assert nsrecord.aRecord == args[1]
		assert nsrecord.ttl.millis == args[2]*1000
	}
}
