package com.anrisoftware.sscontrol.httpd.nginx.nginxconfig;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class NginxConfigListModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(NginxConfigList.class,
                NginxConfigList.class).build(NginxConfigListFactory.class));
    }

}
