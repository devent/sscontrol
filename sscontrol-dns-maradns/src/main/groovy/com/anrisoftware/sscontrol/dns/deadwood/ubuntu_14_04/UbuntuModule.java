/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModModule;
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerModule;
import com.anrisoftware.sscontrol.scripts.localgroupadd.LocalGroupAddModule;
import com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddModule;
import com.anrisoftware.sscontrol.scripts.localuserinfo.LocalUserInfoModule;
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
        install(new LocalUserAddModule());
        install(new LocalGroupAddModule());
        install(new LocalUserInfoModule());
        install(new ChangeFileModModule());
        install(new ChangeFileOwnerModule());
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.ExecCommandModule());
    }
}
