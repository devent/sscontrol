/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-template-st.
 *
 * sscontrol-template-st is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-template-st is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-template-st. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.template

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.template.api.TemplateFactory
import com.anrisoftware.sscontrol.template.api.TemplateService
import com.anrisoftware.sscontrol.template.service.StTemplateService
import com.google.inject.Guice
import com.google.inject.Injector

class ProcessTemplateTest extends TestUtils {

	static URL testTemplate = TestUtils.resourceURL("testtemplate.stg", ProcessTemplateTest)

	Injector injector

	TemplateFactory factory

	@Test
	void "process template"() {
		def arg1 = "one"
		def arg2 = "two"
		def template = factory.create(testTemplate)
		def process = template.process("test", "arg1", arg1, "arg2", arg2)
		assertStringContent process, "$arg1:$arg2"
	}

	@Test
	void "serialize and process template"() {
		def arg1 = "one"
		def arg2 = "two"
		def template = factory.create(testTemplate)
		def templateB = reserialize template
		def process = templateB.process("test", "arg1", arg1, "arg2", arg2)
		assertStringContent process, "$arg1:$arg2"
	}

	@Before
	void createFactory() {
		injector = createInjector()
		TemplateService service = ServiceLoader.load(TemplateService).find {
			it.info == StTemplateService.NAME
		}
		factory = service.getTemplate()
	}

	Injector createInjector() {
		Guice.createInjector()
	}
}
