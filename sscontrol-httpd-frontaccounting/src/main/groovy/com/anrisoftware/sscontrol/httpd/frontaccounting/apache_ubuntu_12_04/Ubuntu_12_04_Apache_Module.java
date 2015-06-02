/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.apache_ubuntu_12_04;

import com.anrisoftware.globalpom.version.VersionModule;
import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuInstallLocaleModule;
import com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleModule;
import com.anrisoftware.sscontrol.scripts.versionlimits.VersionLimitsModule;
import com.google.inject.AbstractModule;

/**
 * <i>Ubuntu 12.04 Apache FrontAccounting</i> module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_Apache_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new VersionModule());
        install(new VersionLimitsModule());
        install(new Ubuntu_12_04_InstallLocaleModule());
        install(new UbuntuInstallLocaleModule());
    }

}
