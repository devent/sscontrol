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
