package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Roundcube mail host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Host {

    private static final String DOMAIN = "domain";

    private static final String ALIAS = "alias";

    private static final String HOST = "host";

    private String host;

    private String alias;

    private String domain;

    /**
     * @see HostFactory#create(Object, Map)
     */
    @Inject
    Host(HostArgs argss, @Assisted Object service,
            @Assisted Map<String, Object> args) {
        setHost(argss.host(service, args));
        if (argss.haveAlias(args)) {
            setAlias(argss.alias(args));
        }
        if (argss.haveDomain(args)) {
            setDomain(argss.domain(args));
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).append(HOST, host);
        builder = alias != null ? builder.append(ALIAS, alias) : builder;
        builder = domain != null ? builder.append(DOMAIN, domain) : builder;
        return builder.toString();
    }
}
