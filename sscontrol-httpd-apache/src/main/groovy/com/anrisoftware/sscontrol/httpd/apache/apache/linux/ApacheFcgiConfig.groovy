/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.apache.linux

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.measure.Measure
import javax.measure.unit.NonSI

import org.apache.commons.io.FileUtils

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fcgi.FcgiConfig
import com.anrisoftware.sscontrol.httpd.php.config.Php_5_Config
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory;
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory;
import com.anrisoftware.sscontrol.scripts.mklink.MkLinkFactory

/**
 * Configures <i>php-fcgi</i> for the domain.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ApacheFcgiConfig extends Php_5_Config implements FcgiConfig {

    @Inject
    private ApacheFcgiConfigLogger logg

    @Inject
    ByteFormatFactory byteFormatFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    MkLinkFactory mkLinkFactory

    TemplateResource fcgiConfigTemplate

    @Inject
    final void setApacheFcgiConfigTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Apache_2_2_Fcgi"
        this.fcgiConfigTemplate = templates.getResource "config"
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List config) {
        def configStr = fcgiConfigTemplate.getText(
                true, "domainConfig",
                "domain", domain,
                "properties", this)
        config << configStr
    }

    /**
     * Enables the {@code fcgid} Apache/mod.
     */
    void enableFcgi() {
        enableMod "fcgid"
    }

    /**
     * Setups the domain for php-fcgi.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void deployConfig(Domain domain) {
        setupDefaults domain
        createScriptDirectory domain
        deployStarterScript domain
        deployPhpini domain
    }

    void createScriptDirectory(Domain domain) {
        def user = domain.domainUser
        def dir = scriptDir domain
        dir.mkdirs()
        changeFileOwnerFactory.create(
                log: log,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: dir,
                recursive: true,
                this, threads)()
        logg.scriptsDirectoryCreated domain, dir
    }

    void deployStarterScript(Domain domain) {
        def user = domain.domainUser
        def string = fcgiConfigTemplate.getText true, "fcgiStarter",
                "properties", this,
                "domain", [scriptDir: scriptDir(domain)]
        def file = scriptStarterFile(domain)
        FileUtils.write file, string
        changeFileOwnerFactory.create(
                log: log,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: file,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                mod: "755",
                files: file,
                this, threads)()
        logg.starterScriptDeployed domain, file, string
    }

    /**
     * Deploys the configuration of the domain specific {@code php.ini} file.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void deployPhpini(Domain domain) {
        super.deployPhpini domain
        linkPhpconf domain
    }

    /**
     * Links PHP configurations to the domain directory.
     */
    void linkPhpconf(Domain domain) {
        def targetdir = scriptDir(domain)
        def sourcedir = phpconfDir
        FileUtils.iterateFiles(sourcedir, null, false).each { File file ->
            def target = new File(targetdir, file.name)
            mkLinkFactory.create(
                    log: log,
                    files: file,
                    targets: target,
                    override: true,
                    command: linkCommand,
                    this, threads)()
            logg.linkPhpconf domain, file, target
        }
    }

    /**
     * Returns the path for the parent directory containing the sites.
     * For example {@code /var/www}.
     *
     * <ul>
     * <li>profile property {@code "sites_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getSitesDirectory() {
        profileProperty("sites_directory", defaultProperties) as File
    }

    /**
     * Returns the maximum requests for php/fcgi.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_max_requests"}</li>
     * </ul>
     */
    int getMaxRequests() {
        profileNumberProperty("php_fcgi_max_requests", defaultProperties)
    }

    /**
     * Returns the sub-directory to save the php/fcgi scripts.
     * For example {@code "php-fcgi-scripts"}.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_scripts_subdirectory"}</li>
     * </ul>
     */
    String getScriptsSubdirectory() {
        profileProperty("php_fcgi_scripts_subdirectory", defaultProperties)
    }

    /**
     * Returns the name of the fcgi/starter script.
     * For example {@code "php-fcgi-starter"}.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_scripts_file"}</li>
     * </ul>
     */
    String getScriptStarterFileName() {
        profileProperty("php_fcgi_scripts_file", defaultProperties)
    }

    /**
     * Returns the directory for domain custom {@code php.ini} files,
     * for example {@code "/etc/php5/cgi/conf.d"}.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_php_conf_directory"}</li>
     * </ul>
     */
    File getPhpconfDir() {
        profileDirProperty "php_fcgi_php_conf_directory", defaultProperties
    }

    /**
     * Returns the file pattern for domain custom {@code php.ini} files,
     * for example {@code "%s_php.ini".}
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_domain_php_ini_file"}</li>
     * </ul>
     */
    String getPhpiniFileName() {
        profileProperty "php_fcgi_domain_php_ini_file", defaultProperties
    }

    /**
     * Returns the scripts directory for the specified domain.
     */
    File scriptStarterFile(Domain domain) {
        new File(scriptDir(domain), scriptStarterFileName)
    }

    /**
     * Returns the default memory limit,
     * for example {@code "2 MB"}.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_default_memory_limit"}</li>
     * </ul>
     */
    Measure getDefaultMemoryLimit() {
        def bytes = profileProperty "php_fcgi_default_memory_limit", defaultProperties
        bytes = byteFormatFactory.create().parse(bytes)
        Measure.valueOf(bytes, NonSI.BYTE)
    }

    /**
     * Returns the default memory upload limit,
     * for example {@code "2 MB"}.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_default_memory_upload"}</li>
     * </ul>
     */
    Measure getDefaultMemoryUpload() {
        def bytes = profileProperty "php_fcgi_default_memory_upload", defaultProperties
        bytes = byteFormatFactory.create().parse(bytes)
        Measure.valueOf(bytes, NonSI.BYTE)
    }

    /**
     * Returns the default memory post limit,
     * for example {@code "2 MB"}.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_default_memory_post"}</li>
     * </ul>
     */
    Measure getDefaultMemoryPost() {
        def bytes = profileProperty "php_fcgi_default_memory_post", defaultProperties
        bytes = byteFormatFactory.create().parse(bytes)
        Measure.valueOf(bytes, NonSI.BYTE)
    }
}
