package com.anrisoftware.sscontrol.template

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.template.api.TemplateFactory
import com.anrisoftware.sscontrol.template.st.StTemplateModule
import com.google.inject.Guice
import com.google.inject.Injector

class ProcessTemplateTest extends TestUtils {

	Injector injector

	@Before
	void createFactory() {
		injector = createInjector()
		injector.getInstance(TemplateFactory.class)
	}

	Injector createInjector() {
		Guice.createInjector(new StTemplateModule())
	}
}
