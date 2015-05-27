/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.apache_ubuntu_12_04;

import com.anrisoftware.globalpom.version.VersionModule;
import com.anrisoftware.sscontrol.scripts.versionlimits.VersionLimitsModule;
import com.google.inject.AbstractModule;

/**
 * Binds <i>FUDForum</i> configuration for <i>Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ApacheModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new VersionModule());
        install(new VersionLimitsModule());
    }

}
