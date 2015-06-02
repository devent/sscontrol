/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.groovy.languagestatements

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.apache.commons.io.Charsets
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.format.locale.LocaleFormatModule
import com.anrisoftware.globalpom.posixlocale.PosixLocale
import com.anrisoftware.globalpom.posixlocale.PosixLocaleFormatModule
import com.anrisoftware.globalpom.posixlocale.PosixLocaleModule
import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.resources.texts.defaults.TextsResourcesDefaultModule
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapModule
import com.anrisoftware.sscontrol.core.listproperty.ListPropertyModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see LanguageStatements
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class LanguageStatementsTest {

    @Test
    void "language name string"() {
        Bean bean = injector.getInstance Bean
        bean.language "de"
        assert bean.language == Locale.GERMAN
    }

    @Test
    void "language name locale"() {
        Bean bean = injector.getInstance Bean
        bean.language Locale.GERMAN
        assert bean.language == Locale.GERMAN
    }

    @Test
    void "language name string, locale string"() {
        Bean bean = injector.getInstance Bean
        bean.language "de", locales: "de_DE.ISO-8859-1"
        assert bean.language == Locale.GERMAN
        assert bean.locales.size() == 1
        assert bean.locales[0].language == "de"
        assert bean.locales[0].country == "DE"
        assert bean.locales[0].charset == Charsets.ISO_8859_1
    }

    @Test
    void "language name string, locale"() {
        Bean bean = injector.getInstance Bean
        bean.language "de", locales: PosixLocale.US
        assert bean.language == Locale.GERMAN
        assert bean.locales.size() == 1
        assert bean.locales[0].language == "en"
        assert bean.locales[0].country == "US"
        assert bean.locales[0].charset == Charset.defaultCharset()
    }

    static Injector injector

    static LanguageStatementsFactory factory

    @BeforeClass
    static void createFactory() {
        toStringStyle
        this.injector = Guice.createInjector(
                new LanguageStatementsModule(),
                new StatementsMapModule(),
                new LocaleFormatModule(),
                new ResourcesModule(),
                new ListPropertyModule(),
                new PosixLocaleModule(),
                new PosixLocaleFormatModule(),
                new TextsResourcesDefaultModule())
        this.factory = injector.getInstance LanguageStatementsFactory
    }
}
