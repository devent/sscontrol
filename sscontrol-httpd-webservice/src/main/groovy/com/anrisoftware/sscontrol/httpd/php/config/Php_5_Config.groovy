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
package com.anrisoftware.sscontrol.httpd.php.config

import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.joda.time.DateTime.now
import static org.joda.time.format.ISODateTimeFormat.dateHourMinuteSecondMillis
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.measure.Measure

import com.anrisoftware.globalpom.format.byteformat.UnitMultiplier
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain

/**
 * <i>PHP 5</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Php_5_Config {

    @Inject
    private Php_5_ConfigLogger logg

    TemplateResource phpConfigTemplate

    Object parent

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
        if (domain.memory.post == null) {
            domain.memory.post = defaultMemoryPost
        }
    }

    /**
     * Deploys the configuration of the domain specific {@code php.ini} file.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void deployPhpini(Domain domain) {
        def file = phpIniFile domain
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(";"), conf, phpiniConfigs(domain), file
        logg.phpinitDeployed domain, file
    }

    /**
     * Returns the list of configurations for the {@code php.ini} file.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @return the {@link List}.
     */
    List phpiniConfigs(Domain domain) {
        [
            memoryLimitConfig(domain),
            uploadMaxFilesizeConfig(domain),
            postMaxSizeConfig(domain),
            logErrorsConfig(domain),
            errorReportingConfig(domain),
            magicQuotesGpcConfig(domain),
            magicQuotesRuntimeConfig(domain),
            magicQuotesSybaseConfig(domain),
            fileUploadsConfig(domain),
            sessionAutoStartConfig(domain),
            zendZe1CompatibilityModeConfig(domain),
            suhosinSessionEncryptConfig(domain),
            mbstringFuncOverloadConfig(domain),
        ]
    }

    def memoryLimitConfig(Domain domain) {
        if (domain.memory == null || domain.memory.limit == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "memoryLimitConfig_search")
        def replace = phpConfigTemplate.getText(true, "memoryLimitConfig", "size", toMegabytes(domain.memory.limit))
        new TokenTemplate(search, replace)
    }

    def uploadMaxFilesizeConfig(Domain domain) {
        if (domain.memory == null || domain.memory.upload == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "uploadMaxFilesizeConfig_search")
        def replace = phpConfigTemplate.getText(true, "uploadMaxFilesizeConfig", "size", toMegabytes(domain.memory.upload))
        new TokenTemplate(search, replace)
    }

    def postMaxSizeConfig(Domain domain) {
        if (domain.memory == null || domain.memory.post == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "postMaxSizeConfig_search")
        def replace = phpConfigTemplate.getText(true, "postMaxSizeConfig", "size", toMegabytes(domain.memory.post))
        new TokenTemplate(search, replace)
    }

    def logErrorsConfig(Domain domain) {
        if (domain.debug?.php == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "logErrorsConfig_search")
        def replace = phpConfigTemplate.getText(true, "logErrorsConfig", "enabled", domain.debug.php > 0)
        new TokenTemplate(search, replace)
    }

    def errorReportingConfig(Domain domain) {
        if (domain.debug?.php == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "errorReportingConfig_search")
        def replace = phpConfigTemplate.getText(true, "errorReportingConfig", "level", domain.debug.php)
        new TokenTemplate(search, replace)
    }

    def magicQuotesGpcConfig(Domain domain) {
        if (magicQuotesGpcEnabled == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "magicQuotesGpcConfig_search")
        def replace = phpConfigTemplate.getText(true, "magicQuotesGpcConfig", "enabled", magicQuotesGpcEnabled)
        new TokenTemplate(search, replace)
    }

    def magicQuotesRuntimeConfig(Domain domain) {
        if (magicQuotesRuntimeEnabled == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "magicQuotesRuntimeConfig_search")
        def replace = phpConfigTemplate.getText(true, "magicQuotesRuntimeConfig", "enabled", magicQuotesRuntimeEnabled)
        new TokenTemplate(search, replace)
    }

    def magicQuotesSybaseConfig(Domain domain) {
        if (magicQuotesSybaseEnabled == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "magicQuotesSybaseConfig_search")
        def replace = phpConfigTemplate.getText(true, "magicQuotesSybaseConfig", "enabled", magicQuotesSybaseEnabled)
        new TokenTemplate(search, replace)
    }

    def fileUploadsConfig(Domain domain) {
        if (fileUploadsEnabled == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "fileUploadsConfig_search")
        def replace = phpConfigTemplate.getText(true, "fileUploadsConfig", "enabled", fileUploadsEnabled)
        new TokenTemplate(search, replace)
    }

    def sessionAutoStartConfig(Domain domain) {
        if (sessionAutoStartEnabled == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "sessionAutoStartConfig_search")
        def replace = phpConfigTemplate.getText(true, "sessionAutoStartConfig", "enabled", sessionAutoStartEnabled)
        new TokenTemplate(search, replace)
    }

    def zendZe1CompatibilityModeConfig(Domain domain) {
        if (zendZe1CompatibilityModeEnabled == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "zendZe1CompatibilityModeConfig_search")
        def replace = phpConfigTemplate.getText(true, "zendZe1CompatibilityModeConfig", "enabled", zendZe1CompatibilityModeEnabled)
        new TokenTemplate(search, replace)
    }

    def suhosinSessionEncryptConfig(Domain domain) {
        if (suhosinSessionEncryptEnabled == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "suhosinSessionEncryptConfig_search")
        def replace = phpConfigTemplate.getText(true, "suhosinSessionEncryptConfig", "enabled", suhosinSessionEncryptEnabled)
        new TokenTemplate(search, replace)
    }

    def mbstringFuncOverloadConfig(Domain domain) {
        if (mbstringFuncOverload == null) {
            return []
        }
        def search = phpConfigTemplate.getText(true, "mbstringFuncOverloadConfig_search")
        def replace = phpConfigTemplate.getText(true, "mbstringFuncOverloadConfig", "value", mbstringFuncOverload)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the {@code php.ini} file for the specified domain.
     */
    File phpIniFile(Domain domain) {
        new File(scriptDir(domain), phpiniFileName)
    }

    /**
     * Returns the scripts directory for the specified domain.
     */
    File scriptDir(Domain domain) {
        new File(sitesDirectory, "$scriptsSubdirectory/$domain.name")
    }

    @Inject
    final void setPhp_5_ConfigTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create("Php_5_Config")
        this.phpConfigTemplate = templates.getResource("phpconfig")
    }

    /**
     * Returns the value converted to megabytes.
     *
     * @param value
     *            the {@link Measure} value.
     */
    final String toMegabytes(Measure value) {
        long v = value.value / UnitMultiplier.MEGA.value
        return "${v}M"
    }

    /**
     * Returns the file pattern for domain custom {@code php.ini} files,
     * for example {@code "%s_php.ini".}
     */
    abstract String getPhpiniFileName()

    /**
     * Returns the sub-directory to save the php/fcgi scripts.
     * For example {@code "php-fcgi-scripts"}.
     */
    abstract String getScriptsSubdirectory()

    /**
     * Returns the path for the parent directory containing the sites.
     * For example {@code "/var/www"}.
     */
    abstract File getSitesDirectory()

    /**
     * Returns the default memory limit,
     * for example {@code "2 MB"}.
     *
     * @see <a href="http://php.net/manual/en/ini.core.php#ini.memory-limit">memory_limit</a>
     */
    Measure getDefaultMemoryLimit() {
        null
    }

    /**
     * Returns the default memory upload limit in bytes,
     * for example {@code "2 MB"}.
     *
     * @see <a href="http://php.net/manual/en/ini.core.php#ini.upload-max-filesize">upload_max_filesize</a>
     */
    Measure getDefaultMemoryUpload() {
        null
    }

    /**
     * Returns the default memory post limit in bytes,
     * for example {@code "2 MB"}.
     *
     * @see <a href="http://php.net/manual/en/ini.core.php#ini.post-max-size">post_max_size</a>
     */
    Measure getDefaultMemoryPost() {
        null
    }

    /**
     * Returns enabled the magic_quotes state for GPC (Get/Post/Cookie)
     * operations, for example {@code "false".}
     *
     * @see <a href="http://php.net/manual/de/info.configuration.php#ini.magic-quotes-gpc">magic_quotes_gpc</a>
     */
    Boolean getMagicQuotesGpcEnabled() {
        null
    }

    /**
     * Returns enabled the quotes escaped with a backslash,
     * for example {@code "false".}
     *
     * @see <a href="http://php.net/manual/en/info.configuration.php#ini.magic-quotes-runtime">magic_quotes_runtime</a>
     */
    Boolean getMagicQuotesRuntimeEnabled() {
        null
    }

    /**
     * Returns enabled the single-quote escape,
     * for example {@code "false".}
     *
     * @see <a href="http://php.net/manual/en/sybase.configuration.php#ini.magic-quotes-sybase">magic_quotes_sybase</a>
     */
    Boolean getMagicQuotesSybaseEnabled() {
        null
    }

    /**
     * Returns allowed HTTP file uploads,
     * for example {@code "true".}
     *
     * @see <a href="http://php.net/manual/en/ini.core.php#ini.file-uploads">file_uploads</a>
     */
    Boolean getFileUploadsEnabled() {
        null
    }

    /**
     * Returns enabled that the session module starts a session automatically,
     * for example {@code "false".}
     *
     * @see <a href="http://php.net/manual/en/session.configuration.php#ini.session.auto-start">session.auto_start</a>
     */
    Boolean getSessionAutoStartEnabled() {
        null
    }

    /**
     * Returns enabled the compatibility mode with Zend Engine 1,
     * for example {@code "false".}
     *
     * @see <a href="http://php.net/manual/en/ini.core.php#ini.zend.ze1-compatibility-mode">zend.ze1_compatibility_mode</a>
     */
    Boolean getZendZe1CompatibilityModeEnabled() {
        null
    }

    /**
     * Returns enabled {@code suhosin.session.encrypt},
     * for example {@code "false".}
     */
    Boolean getSuhosinSessionEncryptEnabled() {
        null
    }

    /**
     * Returns the overloads a set of single byte functions by the
     * mbstring counterparts,
     * for example {@code "0".}
     *
     * @see <a href="http://php.net/manual/en/mbstring.configuration.php#ini.mbstring.func-overload">mbstring.func_overload</a>
     */
    Integer getMbstringFuncOverload() {
        null
    }

    void setScript(Object parent) {
        this.parent = parent
    }

    def propertyMissing(String name) {
        parent.getProperty name
    }

    def methodMissing(String name, def args) {
        parent.invokeMethod name, args
    }
}
