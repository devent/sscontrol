/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04;

import com.anrisoftware.globalpom.exec.runcommands.RunCommandsModule;
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModule;
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;

/**
 * <i>Deadwood Ubuntu 14.04</i> service script module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new LocalUserModule());
        install(new ChangeFileModule());
        install(new RunCommandsModule());
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.UnixScriptsDefaultsModule());
    }
}
