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
package com.anrisoftware.sscontrol.workers.text.match

import static com.anrisoftware.globalpom.utils.TestUtils.*

import java.util.regex.Pattern

import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.workers.text.match.worker.MatchTextWorker
import com.anrisoftware.sscontrol.workers.text.match.worker.MatchTextWorkerFactory
import com.anrisoftware.sscontrol.workers.text.match.worker.MatchTextWorkerModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test compare text files and resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MatchTextTest {

	Injector injector

	MatchTextWorkerFactory factory

	@Test
	void "compare text files matching"() {
		MatchTextWorker worker
		def text = "aaa"
		def pattern = Pattern.compile(/aaa/)
		def file
		withFiles "match", {
			worker = factory.create file, pattern, charset
			worker()
			assert worker.matches == true
		}, {
			file = new File(it.dir, "file_a")
			FileUtils.write file, text
		}
	}

	@Test
	void "compare text files not matching"() {
		MatchTextWorker worker
		def text = "aaa"
		def pattern = Pattern.compile(/bbb/)
		def file
		withFiles "match", {
			worker = factory.create file, pattern, charset
			worker()
			assert worker.matches == false
		}, {
			file = new File(it.dir, "file_a")
			FileUtils.write file, text
		}
	}

	@Test
	void "serialize and compare text files matching"() {
		MatchTextWorker worker
		def text = "aaa"
		def pattern = Pattern.compile(/aaa/)
		def file
		withFiles "match", {
			worker = factory.create file, pattern, charset
			def workerB = reserialize worker
			workerB()
			assert workerB.matches == true
		}, {
			file = new File(it.dir, "file_a")
			FileUtils.write file, text
		}
	}

	@Test
	void "compare text resource URI matching"() {
		MatchTextWorker worker
		def text = "aaa"
		def pattern = Pattern.compile(/aaa/)
		def file
		withFiles "match", {
			worker = factory.create file, pattern, charset
			worker()
			assert worker.matches == true
		}, {
			file = new File(it.dir, "file_a")
			FileUtils.write file, text
			file = file.toURI()
		}
	}

	@Test
	void "compare text resource URL matching"() {
		MatchTextWorker worker
		def text = "aaa"
		def pattern = Pattern.compile(/aaa/)
		def file
		withFiles "match", {
			worker = factory.create file, pattern, charset
			worker()
			assert worker.matches == true
		}, {
			file = new File(it.dir, "file_a")
			FileUtils.write file, text
			file = file.toURI().toURL()
		}
	}

	@Before
	void createFactories() {
		toStringStyle
		injector = createInjector()
		factory = injector.getInstance MatchTextWorkerFactory
	}

	Injector createInjector() {
		Guice.createInjector(new MatchTextWorkerModule())
	}
}
