/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.openssh.openssh.ubuntu_12_04;

import com.anrisoftware.globalpom.initfileparser.InitFileParserModule;
import com.anrisoftware.sscontrol.remote.openssh.authorizedkeys.ubuntu_12_04.AuthorizedKeysUbuntu_12_04_Module;
import com.anrisoftware.sscontrol.remote.openssh.fail2ban.ubuntu_12_04.Fail2BanUbuntu_12_04_Module;
import com.anrisoftware.sscontrol.remote.openssh.screen.ubuntu_12_04.ScreenUbuntu_12_04_Module;
import com.anrisoftware.sscontrol.remote.openssh.userkey.ubuntu_12_04.UserKeyUbuntu_12_04_Module;
import com.anrisoftware.sscontrol.remote.openssh.users.ubuntu_12_04.UsersUbuntu_12_04_Module;
import com.google.inject.AbstractModule;

/**
 * Installs the Remote Access/Ubuntu 12.04 script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new InitFileParserModule());
        install(new UsersUbuntu_12_04_Module());
        install(new UserKeyUbuntu_12_04_Module());
        install(new AuthorizedKeysUbuntu_12_04_Module());
        install(new ScreenUbuntu_12_04_Module());
        install(new Fail2BanUbuntu_12_04_Module());
    }
}
