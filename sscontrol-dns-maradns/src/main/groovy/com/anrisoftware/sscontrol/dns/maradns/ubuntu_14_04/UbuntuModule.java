/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_14_04;

import com.anrisoftware.sscontrol.scripts.enableaptrepository.EnableAptRepositoryModule;
import com.anrisoftware.sscontrol.scripts.repositoryaptenabled.RepositoryAptEnabledModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;

/**
 * MaraDNS/Ubuntu 14.04 service script module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new EnableAptRepositoryModule());
        install(new RepositoryAptEnabledModule());
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.ExecCommandModule());
    }
}
