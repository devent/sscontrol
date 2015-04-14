/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin;

import com.anrisoftware.globalpom.resources.ResourcesModule;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapModule;
import com.anrisoftware.sscontrol.core.list.ListModule;
import com.anrisoftware.sscontrol.security.service.SecService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the <i>Spamassassin</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SpamassassinModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new StatementsMapModule());
        install(new ListModule());
        install(new ResourcesModule());
        install(new FactoryModuleBuilder().implement(SecService.class,
                SpamassassinServiceImpl.class).build(
                SpamassassinServiceFactory.class));
        install(new FactoryModuleBuilder().implement(Header.class,
                HeaderImpl.class).build(HeaderFactory.class));
    }

}
