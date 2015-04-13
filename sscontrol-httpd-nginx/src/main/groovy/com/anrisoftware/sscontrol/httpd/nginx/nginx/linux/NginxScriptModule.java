/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import com.anrisoftware.globalpom.exec.runcommands.RunCommandsModule;
import com.anrisoftware.globalpom.format.durationsimpleformat.DurationSimpleFormatModule;
import com.anrisoftware.sscontrol.httpd.nginx.nginxconfig.NginxConfigListModule;
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModule;
import com.anrisoftware.sscontrol.scripts.findusedport.FindUsedPortModule;
import com.anrisoftware.sscontrol.scripts.killprocess.KillProcessModule;
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserModule;
import com.anrisoftware.sscontrol.scripts.mklink.MkLinkModule;
import com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>Nginx</i> service module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class NginxScriptModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new NginxConfigListModule());
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.UnixScriptsDefaultsModule());
        install(new ChangeFileModule());
        install(new LocalUserModule());
        install(new FindUsedPortModule());
        install(new MkLinkModule());
        install(new ProcessInfoModule());
        install(new KillProcessModule());
        install(new RunCommandsModule());
        install(new DurationSimpleFormatModule());
        install(new FactoryModuleBuilder().implement(
                FindServiceConfigWorker.class, FindServiceConfigWorker.class)
                .build(FindServiceConfigWorkerFactory.class));
    }
}
