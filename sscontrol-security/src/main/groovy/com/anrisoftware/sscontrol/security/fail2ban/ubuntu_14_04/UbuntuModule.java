/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ubuntu_14_04;

import com.anrisoftware.globalpom.initfileparser.InitFileParserModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.anrisoftware.sscontrol.security.fail2ban.ufw_ubuntu_12_04.UfwFail2BanUbuntu_12_04_Module;
import com.google.inject.AbstractModule;

/**
 * Installs the fail2ban script for Ubuntu 14.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.ExecCommandModule());
        install(new InitFileParserModule());
        install(new UfwFail2BanUbuntu_12_04_Module());
    }
}
