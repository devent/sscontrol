/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.locale.core;

import org.junit.BeforeClass
import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see LocaleInfo
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 *
 * @since 1.0
 */
class LocaleInfoTest {

    @Test
    void "parse locale"() {
        parseLocalesCases.each {
            def info = factory.create(it.name).parseLocale()
            assert info.language == it.language
            assert info.country == it.country
            assert info.charset == it.charset
        }
    }

    static parseLocalesCases = [
        [name: "de", language: "de", country: null, charset: null],
        [name: "de_DE", language: "de", country: "de", charset: null],
        [name: "de_DE.UTF-8", language: "de", country: "de", charset: "utf-8"],
        [name: "de_DE.ISO-8859-15", language: "de", country: "de", charset: "iso-8859-15"],
    ]

    static Injector injector

    static LocaleInfoFactory factory

    @BeforeClass
    static void createFactory() {
        this.injector = Guice.createInjector new LocaleCoreModule()
        this.factory = injector.getInstance LocaleInfoFactory
    }
}
