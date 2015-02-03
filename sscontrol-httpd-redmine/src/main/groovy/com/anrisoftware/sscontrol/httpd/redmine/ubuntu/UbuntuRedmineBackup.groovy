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
package com.anrisoftware.sscontrol.httpd.redmine.ubuntu

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04.RedmineConfigFactory

/**
 * <i>Ubuntu Redmine</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuRedmineBackup {

    private Object script

    /**
     * Backups the <i>Redmine</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void backupService(Domain domain, RedmineService service) {
        if (service.backupTarget != null) {
            File source = redmineDir domain, service
            serviceBackup.backupService domain, service, source
            switch (service.database.provider) {
                case "mysql2":
                    mysqlDatabaseBackup.backupDatabase domain, service, source
                    break
            }
        }
    }

    /**
     * Returns the service backup.
     */
    abstract UbuntuRedmineArchiveServiceBackup getServiceBackup()

    /**
     * Returns the <i>MySQL</i> database backup.
     */
    abstract UbuntuRedmineMysqlDatabaseBackup getMysqlDatabaseBackup()

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
        RedmineConfigFactory.WEB_NAME
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
