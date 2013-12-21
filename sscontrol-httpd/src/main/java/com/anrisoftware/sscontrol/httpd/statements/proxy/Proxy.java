package com.anrisoftware.sscontrol.httpd.statements.proxy;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain proxy.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Proxy {

    private static final String SERVICE = "service";

    private static final String ADDRESS = "address";

    private final Domain domain;

    private final ProxyLogger log;

    private String service;

    private String address;

    private String alias;

    @Inject
    Proxy(ProxyLogger logger, ProxyArgs aargs, @Assisted Domain domain,
            @Assisted Map<String, Object> args) {
        this.log = logger;
        this.domain = domain;
        setService(aargs.service(domain, args));
        setAddress(aargs.address(domain, args));
        if (aargs.haveAlias(args)) {
            setAlias(aargs.alias(domain, args));
        }
    }

    public void setService(String service) {
        this.service = service;
        log.serviceSet(domain, service);
    }

    public String getService() {
        return service;
    }

    public void setAddress(String address) {
        this.address = address;
        log.addressSet(domain, address);
    }

    public String getAddress() {
        return address;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        log.aliasSet(domain, alias);
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service)
                .append(ADDRESS, address).toString();
    }
}
