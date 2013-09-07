package com.anrisoftware.sscontrol.ldap.dbindex

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.ldap.dbindex.IndexType.*

import java.text.Format

import org.junit.BeforeClass
import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see DbIndexFormat
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DbIndexFormatTest {

	@Test
	void "format index"() {
		Format format = injector.getInstance DbIndexFormat
		formats.each {
			def str = format.format it.index
			assertStringContent str, it.format
		}
	}

	@Test
	void "parse index"() {
		Format format = injector.getInstance DbIndexFormat
		formats.each {
			DbIndex index = format.parse it.format
			index.names.size() == it.names.size()
			assert index.names.containsAll(it.names)
			assert index.types.containsAll(it.types)
		}
	}

	static Injector injector

	static DbIndexFactory indexFactory

	static formats

	@BeforeClass
	static void createFactory() {
		injector = Guice.createInjector(new DbIndexModule())
		indexFactory = injector.getInstance DbIndexFactory
		formats = createFormats()
	}

	static createFormats() {
		[
			[format: "foo", index: indexFactory.create(["foo"]as Set, []as Set), names: ["foo"], types: []],
			[format: "foo:equality", index: indexFactory.create(["foo"]as Set, [equality]as Set), names: ["foo"], types: [equality]],
			[format: "foo,bar", index: indexFactory.create(["foo", "bar"]as Set, []as Set), names: ["foo", "bar"], types: []],
			[format: "foo,bar:equality", index: indexFactory.create(["foo", "bar"]as Set, [equality]as Set), names: ["foo", "bar"], types: [equality]],
			[format: "foo:equality,substring", index: indexFactory.create(["foo"]as Set, [equality, substring]as Set), names: ["foo"], types: [equality, substring]],
			[format: "foo,bar:equality,substring", index: indexFactory.create(["foo", "bar"]as Set, [equality, substring]as Set), names: ["foo", "bar"], types: [equality, substring]],
		]
	}
}
