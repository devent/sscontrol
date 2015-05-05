package com.anrisoftware.sscontrol.scripts.locale

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuInstallLocaleModule
import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuLocalePackagesFactory
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see UbuntuLocalePackages
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuLocalePackagesTest {

    @Test
    void "return packages for locale"() {
        def cases = [
            [
                locale: "de_DE-ISO-8859-1",
                packages: [
                    "language-pack-de"
                ]
            ],
            [
                locale: "pt_BR.ISO-8859-1",
                packages: [
                    "language-pack-pt"
                ]
            ],
        ]
        cases.each {
            def packages = factory.create(it.locale).packages
            assert packages.size() == it.packages.size()
            assert packages.containsAll(it.packages)
        }
    }

    static Injector injector

    static UbuntuLocalePackagesFactory factory

    @BeforeClass
    static void createFactories() {
        this.injector = Guice.createInjector new UbuntuInstallLocaleModule()
        this.factory = injector.getInstance UbuntuLocalePackagesFactory
    }
}
