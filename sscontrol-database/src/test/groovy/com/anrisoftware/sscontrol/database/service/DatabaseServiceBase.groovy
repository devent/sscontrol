/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Database service test base.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseServiceBase {

    static ubuntu1004Profile = DatabaseServiceBase.class.getResource("Ubuntu_10_04Profile.groovy")

    static databaseScript = DatabaseServiceBase.class.getResource("Database.groovy")

    static databaseBindLocalScript = DatabaseServiceBase.class.getResource("DatabaseBindLocal.groovy")

    static Injector injector

    static ServiceLoaderFactory loaderFactory

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Map variables

    ServicesRegistry registry

    SscontrolServiceLoader loader

    @Before
    void createRegistry() {
        variables = [tmp: tmp.newFolder()]
        registry = injector.getInstance ServicesRegistry
        loader = loaderFactory.create registry, variables
        loader.setParent injector
    }

    @BeforeClass
    static void createFactories() {
        injector = createInjector()
        loaderFactory = injector.getInstance ServiceLoaderFactory
    }

    static Injector createInjector() {
        Guice.createInjector(new CoreModule(), new CoreResourcesModule())
    }

    @BeforeClass
    static void setupToStringStyle() {
        toStringStyle
    }
}
