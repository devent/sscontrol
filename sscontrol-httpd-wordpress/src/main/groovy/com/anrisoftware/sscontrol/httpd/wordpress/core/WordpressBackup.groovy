/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.core

import static org.apache.commons.lang3.StringUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.ISODateTimeFormat
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.scripts.pack.PackFactory

/**
 * Backups the <i>Wordpress</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class WordpressBackup {

    @Inject
    private WordpressBackupLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    PackFactory packFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    Templates backupTemplates

    TemplateResource backupTemplate

    Object parent

    /**
     * Backups the <i>Wordpress</i> service.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WordpressService} <i>Wordpress</i> service.
     */
    void backupService(Domain domain, WordpressService service) {
        if (service.backupTarget != null) {
            backupWordpress domain, service
            backupDatabase domain, service
        }
    }

    /**
     * Backups the <i>Wordpress</i> service files.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WordpressService} <i>Wordpress</i> service.
     */
    void backupWordpress(Domain domain, WordpressService service) {
        File dir = wordpressDir domain, service
        if (dir.exists()) {
            def output = createArchiveFile domain, service
            output.parentFile.mkdirs()
            packFactory.create(
                    log: log,
                    files: dir,
                    commands: unpackCommands,
                    output: output,
                    timeout: backupWordpressTimeout,
                    parent, threads)()
        }
    }

    /**
     * Backups the <i>Wordpress</i> database.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WordpressService} <i>Wordpress</i> service.
     */
    void backupDatabase(Domain domain, WordpressService service) {
        File dir = wordpressDir domain, service
        if (dir.exists() && new File(mysqldumpCommand).exists()) {
            def archiveFile = createDatabaseArchiveFile domain, service
            archiveFile.parentFile.mkdirs()
            scriptExecFactory.create(
                    log: log,
                    mysqldumpCommand: mysqldumpCommand,
                    gzipCommand: gzipCommand,
                    archiveFile: archiveFile,
                    user: service.database.user,
                    password: service.database.password,
                    database: service.database.database,
                    timeout: backupWordpressTimeout,
                    parent, threads, backupTemplate, "backupDatabase")()
        }
    }

    /**
     * Returns the backup archive file.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WordpressService} <i>Wordpress</i> service.
     *
     * @return the archive {@link File} file.
     */
    File createArchiveFile(Domain domain, WordpressService service) {
        def parent = new File(service.backupTarget)
        def st = new ST(backupArchive)
        st.add "args", [domain: domain, timestamp: timestamp]
        new File(parent, st.render())
    }

    /**
     * Returns the database backup archive file.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WordpressService} <i>Wordpress</i> service.
     *
     * @return the archive {@link File} file.
     */
    File createDatabaseArchiveFile(Domain domain, WordpressService service) {
        def parent = new File(service.backupTarget)
        def st = new ST(backupDatabaseArchive)
        st.add "args", [domain: domain, timestamp: timestamp]
        new File(parent, st.render())
    }

    /**
     * Returns the timestamp for the archive file.
     */
    String getTimestamp() {
        def date = ISODateTimeFormat.dateHourMinuteSecondMillis().print DateTime.now()
        def time = replaceChars(date, ':', '-')
        replaceChars(time, '.', '-')
    }

    /**
     * <i>Wordpress</i> backup archive template, for
     * example {@code "wordpress_<args.domain.name>_<args.timestamp>.tar.gz"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_backup_archive"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getBackupArchive() {
        profileProperty "wordpress_backup_archive", wordpressProperties
    }

    /**
     * <i>Wordpress</i> backup database archive template, for
     * example {@code "wordpress_database_<args.domain.name>_<args.timestamp>.gz"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_backup_database_archive"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getBackupDatabaseArchive() {
        profileProperty "wordpress_backup_database_archive", wordpressProperties
    }

    /**
     * <i>mysqldump</i> command, for
     * example {@code "/usr/bin/mysqldump"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_mysqldump_command"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getMysqldumpCommand() {
        profileProperty "wordpress_mysqldump_command", wordpressProperties
    }

    /**
     * <i>gzip</i> command, for
     * example {@code "/bin/gzip"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_gzip_command"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getGzipCommand() {
        profileProperty "wordpress_gzip_command", wordpressProperties
    }

    /**
     * The timeout to backup the <i>Wordpress</i> service, for
     * example {@code "PT1H"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_backup_timeout"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    Duration getBackupWordpressTimeout() {
        profileDurationProperty "wordpress_backup_timeout", wordpressProperties
    }

    void setScript(Object parent) {
        this.parent = parent
        this.backupTemplates = templatesFactory.create("Wordpress_3")
        this.backupTemplate = backupTemplates.getResource("backup")
    }

    def propertyMissing(String name) {
        parent.getProperty name
    }

    def methodMissing(String name, def args) {
        parent.invokeMethod name, args
    }
}
