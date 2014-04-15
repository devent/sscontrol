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
    }

}
