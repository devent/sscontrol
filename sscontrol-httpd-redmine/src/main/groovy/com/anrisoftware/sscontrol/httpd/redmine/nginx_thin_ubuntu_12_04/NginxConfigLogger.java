package com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04;

import static com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04.NginxConfigLogger._.deploy_upstream_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04.NginxConfigLogger._.deploy_upstream_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link NginxConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class NginxConfigLogger extends AbstractLogger {

    enum _ {

        deploy_upstream_debug(
                "Deploy domain {} upstream configuration to '{}' for {}."),

        deploy_upstream_info(
                "Deploy domain '{}' upstream configuration to '{}' for script '{}'.");

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
     * Sets the context of the logger to {@link NginxConfig}.
     */
    public NginxConfigLogger() {
        super(NginxConfig.class);
    }

    void deployDomainUpstreamConfig(NginxConfig script, Domain domain, File file) {
        if (isDebugEnabled()) {
            debug(deploy_upstream_debug, domain, file, script);
        } else {
            String name = script.getName();
            info(deploy_upstream_info, domain.getName(), file, name);
        }
    }
}
