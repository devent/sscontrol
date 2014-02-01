package com.anrisoftware.sscontrol.httpd.service;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicePreScript;
import com.anrisoftware.sscontrol.core.api.ServicePreScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Factory providing pre-script service for <i>Httpd</i> service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServicePreScriptFactory.class)
public class HttpPreScriptFactory implements ServicePreScriptFactory {

    private static final ServiceScriptInfo INFO = new ServiceScriptInfo() {

        @Override
        public String getServiceName() {
            return HttpdFactory.NAME;
        }

        @Override
        public String getProfileName() {
            return null;
        }
    };

    private static final Module[] MODULES = new Module[] { new HttpdPreScriptModule() };

    private Injector injector;

    @Override
    public ServicePreScript getPreScript() throws ServiceException {
        HttpdPreScript service;
        service = injector.getInstance(HttpdPreScript.class);
        return service;
    }

    @Override
    public ServiceScriptInfo getInfo() {
        return INFO;
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

}
