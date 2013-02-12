package com.anrisoftware.sscontrol.database.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before

import com.anrisoftware.sscontrol.core.activator.CoreModule
import com.google.inject.Guice
import com.google.inject.Injector

class DatabaseServiceUtil {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", DatabaseServiceUtil)

	static databaseScript = resourceURL("Database.groovy", DatabaseServiceUtil)

	static databaseScriptWithDefaults = resourceURL("DatabaseWithDefaults.groovy", DatabaseServiceUtil)

	Injector injector

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
}
