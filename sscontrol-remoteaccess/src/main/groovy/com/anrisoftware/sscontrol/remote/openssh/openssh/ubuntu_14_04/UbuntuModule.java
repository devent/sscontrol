/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.openssh.ubuntu_14_04;

import com.anrisoftware.sscontrol.remote.openssh.authorizedkeys.ubuntu_14_04.AuthorizedKeysUbuntu_14_04_Module;
import com.anrisoftware.sscontrol.remote.openssh.screen.ubuntu_14_04.ScreenUbuntu_14_04_Module;
import com.anrisoftware.sscontrol.remote.openssh.userkey.ubuntu_14_04.UserKeyUbuntu_14_04_Module;
import com.anrisoftware.sscontrol.remote.openssh.users.ubuntu_14_04.UsersUbuntu_14_04_Module;
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModule;
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;

/**
 * <i>OpenSSH Ubuntu 14.04</i> script module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new LocalUserModule());
        install(new ChangeFileModule());
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.UnixScriptsDefaultsModule());
        install(new UsersUbuntu_14_04_Module());
        install(new UserKeyUbuntu_14_04_Module());
        install(new AuthorizedKeysUbuntu_14_04_Module());
        install(new ScreenUbuntu_14_04_Module());
    }
}
