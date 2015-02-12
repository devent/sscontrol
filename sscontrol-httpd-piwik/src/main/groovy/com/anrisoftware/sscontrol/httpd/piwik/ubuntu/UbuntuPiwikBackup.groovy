/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.ubuntu

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService

/**
 * <i>Ubuntu Piwik</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuPiwikBackup {

    private Object script

    /**
     * Backups the <i>Piwik</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void backupService(Domain domain, PiwikService service) {
        if (service.backupTarget != null) {
            File source = piwikDir domain, service
            serviceBackup.backupService domain, service, source
            switch (service.database.schema) {
                case "Mysql":
                    mysqlDatabaseBackup.backupDatabase domain, service, source
                    break
            }
        }
    }

    /**
     * Returns the service backup.
     */
    abstract UbuntuPiwikArchiveServiceBackup getServiceBackup()

    /**
     * Returns the <i>MySQL</i> database backup.
     */
    abstract UbuntuPiwikMysqlDatabaseBackup getMysqlDatabaseBackup()

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
        serviceBackup.setScript script
        mysqlDatabaseBackup.setScript script
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        script
    }

    /**
     * Returns the service name.
     */
    String getServiceName() {
        "piwik"
    }

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
