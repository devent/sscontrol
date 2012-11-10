/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.tokentemplate

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.workers.text.tokentemplate.worker.TokenMarker
import com.anrisoftware.sscontrol.workers.text.tokentemplate.worker.TokenTemplate
import com.anrisoftware.sscontrol.workers.text.tokentemplate.worker.TokensTemplateWorkerFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.worker.TokensTemplateWorkerModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test tokens template.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TokensTemplateTest {

	Injector injector

	TokensTemplateWorkerFactory factory

	def replace

	def tokens

	@Test
	void "replace template without tokens"() {
		def template = new TokenTemplate("foo", "$replace")
		def worker = factory.create tokens, template,
						"foo\n"
		worker()
		assertStringContent worker.text, """#BEGIN
$replace
#END

"""
	}

	@Test
	void "replace template without tokens with multiple entries"() {
		def template = new TokenTemplate("foo", "$replace")
		def worker = factory.create tokens, template,
						"foo\nbar\n"
		worker()
		assertStringContent worker.text, """#BEGIN
$replace
#END

bar
"""
	}

	@Test
	void "replace template with tokens"() {
		def template = new TokenTemplate("foo", "$replace")
		def worker = factory.create tokens, template,
						"#BEGIN\nfoo\n#END\n"
		worker()
		assertStringContent worker.text, """#BEGIN
$replace
#END
"""
	}

	@Test
	void "replace template with tokens with multiple entries"() {
		def template = new TokenTemplate("foo", "$replace")
		def worker = factory.create tokens, template,
						"#BEGIN\nfoo\n#END\nbar\n"
		worker()
		assertStringContent worker.text, """#BEGIN
$replace
#END
bar
"""
	}

	@Test
	void "append template with tokens to empty configuration"() {
		def template = new TokenTemplate("foo", "$replace")
		def worker = factory.create tokens, template,
						"\n"
		worker()
		assertStringContent worker.text, """
#BEGIN
$replace
#END
"""
	}

	@Test
	void "append template with tokens to configuration"() {
		def template = new TokenTemplate("foo", "$replace")
		def worker = factory.create tokens, template,
						"bar\n"
		worker()
		assertStringContent worker.text, """bar
#BEGIN
$replace
#END
"""
	}

	@Test
	void "serialize and append template with tokens to empty configuration"() {
		def template = new TokenTemplate("foo", "$replace")
		def worker = factory.create tokens, template,
						"\n"
		def workerB = reserialize worker
		workerB()
		assertStringContent workerB.text, """
#BEGIN
$replace
#END
"""
	}

	@Before
	void createFactories() {
		toStringStyle
		injector = createInjector()
		factory = injector.getInstance TokensTemplateWorkerFactory
		replace = "bar"
		tokens = new TokenMarker("#BEGIN", "#END\n")
	}

	Injector createInjector() {
		Guice.createInjector(new TokensTemplateWorkerModule())
	}
}
