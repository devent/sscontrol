package com.anrisoftware.sscontrol.httpd.webserviceargs;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @see DefaultWebServiceFactory
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WebServiceArgsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(DefaultWebService.class,
                DefaultWebService.class).build(DefaultWebServiceFactory.class));
    }

}
