/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.unix;

import com.anrisoftware.globalpom.exec.command.DefaultCommandLineModule;
import com.anrisoftware.globalpom.exec.core.DefaultProcessModule;
import com.anrisoftware.globalpom.exec.logoutputs.LogOutputsModule;
import com.anrisoftware.globalpom.exec.pipeoutputs.PipeOutputsModule;
import com.anrisoftware.globalpom.exec.script.ScriptProcessModule;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @see InstallPackagesFactory
 * @see RestartServicesFactory
 * @see StopServicesFactory
 * @see ScriptExecFactory
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UnixScriptsModule extends AbstractModule {

    /**
     * Installs needed command exec modules.
     * 
     * @author Erwin Mueller, erwin.mueller@deventm.org
     * @since 1.0
     */
    public static class ExecCommandModule extends AbstractModule {

        @Override
        protected void configure() {
            install(new DefaultCommandLineModule());
            install(new DefaultProcessModule());
            install(new LogOutputsModule());
            install(new PipeOutputsModule());
            install(new ScriptProcessModule());
        }

    }

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(InstallPackages.class,
                InstallPackages.class).build(InstallPackagesFactory.class));
        install(new FactoryModuleBuilder().implement(RestartServices.class,
                RestartServices.class).build(RestartServicesFactory.class));
        install(new FactoryModuleBuilder().implement(ScriptExec.class,
                ScriptExec.class).build(ScriptExecFactory.class));
        install(new FactoryModuleBuilder().implement(StopServices.class,
                StopServices.class).build(StopServicesFactory.class));
    }

}
