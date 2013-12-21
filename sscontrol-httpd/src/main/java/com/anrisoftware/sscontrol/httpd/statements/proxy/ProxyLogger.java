package com.anrisoftware.sscontrol.httpd.statements.proxy;

import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.address_null;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.address_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.address_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.alias_null;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.alias_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.alias_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.service_null;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.service_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyLogger._.service_set_info;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Logging for {@link Proxy}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProxyLogger extends AbstractLogger {

    enum _ {

        address_null("Proxy address cannot be null or empty for %s."),

        address_set_debug("Proxy address '{}' set for {}."),

        address_set_info("Proxy address '{}' set for domain '{}'."),

        service_null("Proxy service cannot be null or empty for %s."),

        service_set_debug("Proxy service '{}' set for {}."),

        service_set_info("Proxy service '{}' set for domain '{}'."),

        alias_null("Proxy alias cannot be null or empty for %s."),

        alias_set_debug("Proxy alias '{}' set for {}."),

        alias_set_info("Proxy alias '{}' set for domain '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link Proxy}.
     */
    public ProxyLogger() {
        super(Proxy.class);
    }

    void checkAddress(Domain domain, Object address) {
        notNull(address, address_null.toString(), domain);
        notBlank(address.toString(), address_null.toString(), domain);
    }

    void addressSet(Domain domain, String address) {
        if (isDebugEnabled()) {
            debug(address_set_debug, address, domain);
        } else {
            info(address_set_info, address, domain.getName());
        }
    }

    void checkService(Domain domain, Object service) {
        notNull(service, service_null.toString(), domain);
        notBlank(service.toString(), service_null.toString(), domain);
    }

    void serviceSet(Domain domain, String service) {
        if (isDebugEnabled()) {
            debug(service_set_debug, service, domain);
        } else {
            info(service_set_info, service, domain.getName());
        }
    }

    void checkAlias(Domain domain, Object alias) {
        notNull(alias, alias_null.toString(), domain);
        notBlank(alias.toString(), alias_null.toString(), domain);
    }

    public void aliasSet(Domain domain, String alias) {
        if (isDebugEnabled()) {
            debug(alias_set_debug, alias, domain);
        } else {
            info(alias_set_info, alias, domain.getName());
        }
    }
}
