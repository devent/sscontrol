/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.backups.mysql

import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.joda.time.DateTime.now
import static org.joda.time.format.ISODateTimeFormat.dateHourMinuteSecondMillis
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.backups.database.DatabaseBackup
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Backups the <i>MySQL</i> database of a service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Mysql_5_DatabaseBackup extends DatabaseBackup {

    @Inject
    ScriptExecFactory scriptExecFactory

    TemplateResource backupTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create("Mysql_5_DatabaseBackup")
        this.backupTemplate = templates.getResource("backup_mysql_5")
    }

    @Override
    void execBackupScript(WebService service, File archiveFile) {
        scriptExecFactory.create(
                log: log,
                mysqldumpCommand: mysqldumpCommand,
                gzipCommand: gzipCommand,
                archiveFile: archiveFile,
                user: service.database.user,
                password: service.database.password,
                database: service.database.database,
                timeout: backupTimeout,
                script, threads, backupTemplate, "backupMysqlDatabase")()
    }

    /**
     * Returns the <i>mysqldump</i> command, for
     * example {@code "/usr/bin/mysqldump"}.
     */
    abstract String getMysqldumpCommand()

    /**
     * Returns the <i>gzip</i> command, for
     * example {@code "/bin/gzip"}.
     */
    abstract String getGzipCommand()
}
