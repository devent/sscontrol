package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigFactory;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>Gitit</i> configuration factory for <i>Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceConfigFactory.class)
public class GititConfigFactory implements ServiceConfigFactory {

    /**
     * <i>Ubuntu 12.04</i> profile name.
     */
    public static final String PROFILE_NAME = "ubuntu_12_04";

    /**
     * <i>Gitit</i> service name.
     */
    public static final String SERVICE_NAME = "gitit";

    /**
     * <i>Gitit</i> service information.
     */
    @SuppressWarnings("serial")
    public static final ServiceConfigInfo INFO = new ServiceConfigInfo() {

        @Override
        public String getServiceName() {
            return SERVICE_NAME;
        }

        @Override
        public String getProfileName() {
            return PROFILE_NAME;
        }
    };

    private static final Module[] MODULES = new Module[] { new UbuntuModule() };

    private Injector injector;

    public GititConfigFactory() {
    }

    @Override
    public ServiceConfig getScript() throws ServiceException {
        return injector.getInstance(UbuntuConfig.class);
    }

    @Override
    public ServiceConfigInfo getInfo() {
        return INFO;
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(INFO).toString();
    }
}
