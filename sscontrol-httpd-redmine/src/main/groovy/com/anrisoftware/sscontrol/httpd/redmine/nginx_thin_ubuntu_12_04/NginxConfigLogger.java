/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
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
