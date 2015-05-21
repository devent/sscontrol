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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.sscontrol.scripts.locale.core.LocaleCoreModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see UbuntuLocaleSettings
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 *
 * @since 1.0
 */
class UbuntuLocaleSettingsTest {

    @Test
    void "parse locale"() {
        parseLocalesCases.each {
            def settings = factory.create(it.name).parseLocale()
            assert settings.packages.size() == it.packages.size()
            assert settings.packages.containsAll(it.packages)
            assert settings.supportedLocaleFileName == it.supportedLocaleFile
            assert settings.convertName == it.convertName
        }
    }

    static parseLocalesCases = [
        [name: "de", convertName: "de_DE.UTF-8", packages: ["language-pack-de"], supportedLocaleFile: "de"],
        [name: "de_DE", convertName: "de_DE.UTF-8", packages: ["language-pack-de"], supportedLocaleFile: "de"],
        [name: "de_DE.UTF-8", convertName: "de_DE.UTF-8", packages: ["language-pack-de"], supportedLocaleFile: "de"],
        [name: "de_DE.ISO-8859-15", convertName: "de_DE.ISO-8859-15", packages: ["language-pack-de"], supportedLocaleFile: "de"],
        [name: "pt_BR.ISO-8859-1", convertName: "pt_BR.ISO-8859-1", packages: ["language-pack-pt"], supportedLocaleFile: "pt"],
    ]

    static Injector injector

    static UbuntuLocaleSettingsFactory factory

    @BeforeClass
    static void createFactory() {
        this.injector = Guice.createInjector new UbuntuInstallLocaleModule(), new LocaleCoreModule()
        this.factory = injector.getInstance UbuntuLocaleSettingsFactory
    }
}
