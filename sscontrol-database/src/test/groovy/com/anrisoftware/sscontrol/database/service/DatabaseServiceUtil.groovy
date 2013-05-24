package com.anrisoftware.sscontrol.database.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

class DatabaseServiceUtil {

	static ubuntu1004Profile = DatabaseServiceUtil.class.getResource("Ubuntu_10_04Profile.groovy")

	static databaseScript = DatabaseServiceUtil.class.getResource("Database.groovy")

	static databaseScriptWithDefaults = DatabaseServiceUtil.class.getResource("DatabaseWithDefaults.groovy")

	Injector injector

	File tmpdir

	Map variables

	@Before
	void createTemp() {
		tmpdir = File.createTempDir this.class.simpleName, null
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
		Guice.createInjector(new CoreModule(), new CoreResourcesModule())
	}
}
