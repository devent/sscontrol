package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*

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
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Creates the test environment for Apache on Ubuntu system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuTestUtil {

	static Injector injector

	static ServiceLoaderFactory loaderFactory

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	ServicesRegistry registry

	SscontrolServiceLoader loader

	File tmpdir

	Map variables

	@Before
	void createTemp() {
		tmpdir = tmp.newFolder("apache-ubuntu-10-04")
		variables = [tmp: tmpdir.absoluteFile]
		copyUbuntuFiles tmpdir
	}

	@Before
	void createRegistry() {
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
		Guice.createInjector(new CoreModule(), new CoreResourcesModule(), new ServiceModule())
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}
}
