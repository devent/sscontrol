/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import javax.inject.Inject
import javax.measure.Measure
import javax.measure.unit.NonSI

import org.apache.commons.io.FileUtils

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.globalpom.format.byteformat.UnitMultiplier
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.DomainImpl;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures php-fcgi for the domain.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FcgiConfig {

    @Inject
    private FcgiConfigLogger log

    @Inject
    ByteFormatFactory byteFormatFactory

    Templates fcgiTemplates

    TemplateResource fcgiConfigTemplate

    LinuxScript script

    /**
     * Sets the parent Apache script.
     *
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(LinuxScript script) {
        this.script = script
        fcgiTemplates = templatesFactory.create "Apache_2_2_Fcgi"
        fcgiConfigTemplate = fcgiTemplates.getResource "config"
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

    /**
     * Sets the defaults for the specified domain.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void setupDefaults(Domain domain) {
        if (domain.memory == null) {
            domain.memory limit: defaultMemoryLimit, upload: defaultMemoryUpload, post: defaultMemoryPost
        }
        if (domain.memory.limit == null) {
            domain.memory.limit = defaultMemoryLimit
        }
        if (domain.memory.upload == null) {
            domain.memory.upload = defaultMemoryUpload
        }
    }

    void createScriptDirectory(Domain domain) {
        def user = domain.domainUser
        def dir = scriptDir domain
        dir.mkdirs()
        changeOwner owner: user.name, ownerGroup: user.group, files: dir, recursive: true
        log.scriptsDirectoryCreated domain, dir
    }

    void deployStarterScript(Domain domain) {
        def user = domain.domainUser
        def string = fcgiConfigTemplate.getText true, "fcgiStarter",
                "properties", this,
                "domain", [scriptDir: scriptDir(domain)]
        def file = scriptStarterFile(domain)
        FileUtils.write file, string
        changeOwner owner: user.name, ownerGroup: user.group, files: file
        changeMod mod: "755", files: file
        log.starterScriptDeployed domain, file, string
    }

    /**
     * Deploys the configuration of the domain specific {@code php.ini} file.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void deployPhpini(Domain domain) {
        def file = phpIniFile domain
        def conf = script.currentConfiguration file
        deployConfiguration script.configurationTokens(";"), conf, phpiniConfigs(domain), file
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
            link files: file, targets: target, override: true
            log.linkPhpconf domain, file, target
        }
    }

    List phpiniConfigs(Domain domain) {
        [
            memoryLimitConfig(domain),
            uploadMaxFilesizeConfig(domain),
            postMaxSizeConfig(domain),
        ]
    }

    def memoryLimitConfig(Domain domain) {
        def search = fcgiConfigTemplate.getText(true, "memoryLimitConfig_search")
        def replace = fcgiConfigTemplate.getText(true, "memoryLimitConfig", "size", toMegabytes(domain.memory.limit))
        new TokenTemplate(search, replace)
    }

    def uploadMaxFilesizeConfig(Domain domain) {
        def search = fcgiConfigTemplate.getText(true, "uploadMaxFilesizeConfig_search")
        def replace = fcgiConfigTemplate.getText(true, "uploadMaxFilesizeConfig", "size", toMegabytes(domain.memory.upload))
        new TokenTemplate(search, replace)
    }

    def postMaxSizeConfig(Domain domain) {
        def search = fcgiConfigTemplate.getText(true, "postMaxSizeConfig_search")
        def replace = fcgiConfigTemplate.getText(true, "postMaxSizeConfig", "size", toMegabytes(domain.memory.post))
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the value converted to megabytes.
     *
     * @param value
     *            the {@link Measure} value.
     */
    String toMegabytes(Measure value) {
        long v = value.value / UnitMultiplier.MEGA.value
        return "${v}M"
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
     * Returns the {@code php.ini} file for the specified domain.
     */
    File phpIniFile(Domain domain) {
        new File(scriptDir(domain), phpiniFileName)
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
     * Returns the default memory limit in megabytes,
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
     * Returns the default memory upload limit in bytes,
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
     * Returns the default memory post limit in bytes,
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

    /**
     * Returns the scripts directory for the specified domain.
     */
    File scriptDir(Domain domain) {
        new File(sitesDirectory, "$scriptsSubdirectory/$domain.name")
    }

    /**
     * Returns the scripts directory for the specified domain.
     */
    File scriptStarterFile(Domain domain) {
        new File(scriptDir(domain), scriptStarterFileName)
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
