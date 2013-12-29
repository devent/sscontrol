/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.openssh.ubuntu_10_04;

import com.anrisoftware.sscontrol.remote.openssh.users.ubuntu_10_04.Ubuntu_10_04_Module;
import com.google.inject.AbstractModule;

/**
 * Installs the Remote Access/Ubuntu 10.04 script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new Ubuntu_10_04_Module());
    }
}
