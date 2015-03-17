/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu_12_04;

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashModule;
import com.anrisoftware.globalpom.resources.ResourcesModule;
import com.anrisoftware.globalpom.version.VersionModule;
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.anrisoftware.sscontrol.scripts.unpack.UnpackModule;
import com.google.inject.AbstractModule;

/**
 * <i>ClamAV Ubuntu 12.04</i> module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.UnixScriptsDefaultsModule());
        install(new LocalUserModule());
        install(new VersionModule());
        install(new CheckFileHashModule());
        install(new ResourcesModule());
        install(new UnpackModule());
    }
}
