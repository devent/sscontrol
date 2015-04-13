/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.AuthenticationMethod
import com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.ScmInstall
import com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04.RedmineConfigFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Redmine 2.5</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Redmine_2_5_Config {

    @Inject
    private Redmine_2_5_ConfigLogger logg

    private LinuxScript script

    @Inject
    AuthenticationMethodAttributeRenderer authenticationMethodAttributeRenderer

    @Inject
    DeliveryMethodAttributeRenderer deliveryMethodAttributeRenderer

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    DebugLoggingFactory debugLoggingFactory

    TemplateResource gemInstallTemplate

    TemplateResource bundleInstallTemplate

    TemplateResource rakeCommandsTemplate

    TemplateResource environmentConfigTemplate

    /**
     * Setups default options.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void setupDefaults(Domain domain, RedmineService service) {
        setupDefaultOverrideMode domain, service
        setupDefaultDebug domain, service
        setupDefaultDatabase domain, service
        setupDefaultPrefix domain, service
        setupDefaultAlias domain, service
        setupDefaultMail domain, service
        setupDefaultLanguage domain, service
        setupDefaultScms domain, service
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultDebug(Domain domain, RedmineService service) {
        if (!service.debugLogging("level") || !service.debugLogging("level")["redmine"]) {
            int level = profileNumberProperty "redmine_default_debug_redmine_level", redmineProperties
            service.debug "redmine", level: level
        }
    }

    /**
     * Setups the default override mode.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultOverrideMode(Domain domain, RedmineService service) {
        if (!service.overrideMode) {
            OverrideMode mode = OverrideMode.valueOf profileProperty("redmine_default_override_mode", redmineProperties)
            service.override mode: mode
        }
    }

    /**
     * Setups the default database.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultDatabase(Domain domain, RedmineService service) {
        if (!service.database || !service.database.provider) {
            def provider = profileProperty "redmine_default_database_provider", redmineProperties
            service.database provider: provider
        }
        if (!service.database || !service.database.encoding) {
            def provider = profileProperty "redmine_default_database_encoding", redmineProperties
            service.database encoding: provider
        }
    }

    /**
     * Setups the default prefix.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultPrefix(Domain domain, RedmineService service) {
        if (!service.prefix) {
            def prefix = profileProperty "redmine_default_prefix", redmineProperties
            service.prefix = prefix
        }
    }

    /**
     * Setups the default alias.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultAlias(Domain domain, RedmineService service) {
        service.alias = service.alias == null ? "" : service.alias
    }

    /**
     * Setups the default mail.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultMail(Domain domain, RedmineService service) {
        if (!service.mail || !service.mail.host) {
            service.mail domain.name
        }
        if (!service.mail || !service.mail.port) {
            int value = profileNumberProperty "redmine_default_mail_port", redmineProperties
            service.mail port: value
        }
        if (!service.mail || !service.mail.method) {
            def value = DeliveryMethod.valueOf profileProperty("redmine_default_mail_delivery_method", redmineProperties)
            service.mail method: value
        }
        if (!service.mail || !service.mail.auth) {
            def value = AuthenticationMethod.valueOf profileProperty("redmine_default_mail_authentication_method", redmineProperties)
            service.mail auth: value
        }
        if (!service.mail || !service.mail.ssl) {
            def value = profileBooleanProperty "redmine_default_mail_ssl_enabled", redmineProperties
            service.mail ssl: value
        }
        if (!service.mail || !service.mail.startTlsAuto) {
            def value = profileBooleanProperty "redmine_default_mail_enable_starttls_auto", redmineProperties
            service.mail startTlsAuto: value
        }
        if (!service.mail || !service.mail.opensslVerifyMode) {
            def value = profileProperty "redmine_default_mail_openssl_verify_mode", redmineProperties
            service.mail opensslVerifyMode: value
        }
    }

    /**
     * Setups the default language.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultLanguage(Domain domain, RedmineService service) {
        if (service.languageName == null) {
            def name = profileProperty "redmine_default_language_name", redmineProperties
            service.language name: name
        }
    }

    /**
     * Setups the default SCMs to install.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    void setupDefaultScms(Domain domain, RedmineService service) {
        if (service.scms == null) {
            service.scm install: defaultScmInstall
        }
    }

    /**
     * Installs the <i>Ruby</i> gems.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     */
    void installGems(Domain domain, RedmineService service) {
        def task = scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                gemCommand: gemCommand,
                gems: redmineGems,
                timeout: gemInstallTimeout,
                this, threads, gemInstallTemplate, "gemInstall")()
        logg.gemsInstalled this, task, redmineGems
    }

    /**
     * Deploys the <i>Redmine</i> environment configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineEnvironmentFile()
     */
    void deployEnvironmentConfig(Domain domain, RedmineService service) {
        def configs = [
            redmineAppConfigToken(domain, service),
        ]
        def file = redmineConfigFile domain, service, redmineEnvironmentFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    List redmineAppConfigToken(Domain domain, RedmineService service) {
        if (service.alias.empty) {
            return []
        }
        def search = environmentConfigTemplate.getText(true, "redmineAppConfigSearch", "service", service)
        def replace = environmentConfigTemplate.getText(true, "redmineAppConfig", "service", service)
        [
            new TokenTemplate(search, replace)
        ]
    }

    /**
     * Sets the permissions of the <i>Redmine</i> directories.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void setupPermissions(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        def user = domain.domainUser
        def filesDir = new File(dir, "files")
        def logDir = new File(dir, "log")
        def tmpDir = new File(dir, "tmp")
        def tmpPdfDir = new File(dir, "tmp/pdf")
        def pluginAssetsDir = new File(dir, "public/plugin_assets")
        tmpDir.mkdirs()
        tmpPdfDir.mkdirs()
        pluginAssetsDir.mkdirs()
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                files: [
                    filesDir,
                    logDir,
                    tmpDir,
                    tmpPdfDir,
                    pluginAssetsDir
                ],
                recursive: true,
                command: script.chownCommand,
                owner: user.name, ownerGroup: user.group,
                this, threads)()
    }

    /**
     * Installs the <i>Redmine</i> bundle.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void installBundle(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        def gemsUpdated = []
        def task = installBundle domain, service, dir
        if (task.exitValue == 7) {
            updateGems domain, service, dir
            installBundle domain, service, dir
        }
    }

    /**
     * Updates any locked <i>Ruby</i> gems.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    def installBundle(Domain domain, RedmineService service, File dir) {
        scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                bundleCommand: bundleCommand,
                workDir: dir,
                excludedBundles: productionExcludedBundles,
                timeout: bundleInstallTimeout,
                checkExitCodes: false,
                this, threads, bundleInstallTemplate, "bundleInstall")()
    }

    /**
     * Updates any locked <i>Ruby</i> gems.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void updateGems(Domain domain, RedmineService service, File dir) {
        def task = scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                bundleCommand: bundleCommand,
                workDir: dir,
                timeout: bundleInstallTimeout,
                this, threads, bundleInstallTemplate, "updateGems")()
        logg.gemsUpdated this, task
    }

    /**
     * Generates the secret tokens.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void generateSecretTokens(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                rakeCommand: rakeCommand,
                workDir: dir,
                this, threads, rakeCommandsTemplate, "rakeGenerateSecretTokens")()
    }

    /**
     * Create database schema objects.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void migrateDb(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                rakeCommand: rakeCommand,
                workDir: dir,
                rails: railsProduction,
                language: service.languageName,
                timeout: migrateDatabaseTimeout,
                this, threads, rakeCommandsTemplate, "rakeMigrateDb")()
    }

    /**
     * Clears the temp data.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void clearTemps(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                rakeCommand: rakeCommand,
                workDir: dir,
                this, threads, rakeCommandsTemplate, "rakeClearTemps")()
    }

    /**
     * Load database default data set.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void loadDefaultData(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                rakeCommand: rakeCommand,
                workDir: dir,
                language: service.languageName,
                rails: railsProduction,
                this, threads, rakeCommandsTemplate, "rakeLoadDefaultData")()
    }

    /**
     * Returns the <i>Redmine</i> installation directory, for example
     * {@code /var/www/domain.com/redmineprefix}
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see RedmineService#getPrefix()
     */
    File redmineDir(Domain domain, RedmineService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the <i>Redmine</i> public directory, for example
     * {@code "/var/www/domain.com/redmineprefix/public".} If the path is not
     * absolute, than the directory is assumed under the prefix directory.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see RedmineService#getPrefix()
     * @see #getRedmineProperties()
     */
    File redminePublicDir(Domain domain, RedmineService service) {
        def dir = new File(domainDir(domain), service.prefix)
        File publicDir = profileDirProperty "redmine_public_directory", redmineProperties
        if (publicDir.absolute) {
            return publicDir
        } else {
            return new File(dir, publicDir.path)
        }
    }

    /**
     * Returns the domain name as a file name.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @return the file name.
     */
    String domainNameAsFileName(Domain domain) {
        domain.name.replaceAll(/\./, "_")
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @return the location.
     */
    String serviceLocation(RedmineService service) {
        String location = service.alias == null ? "" : service.alias
        if (!location.startsWith("/")) {
            location = "/$location"
        }
        return location
    }

    /**
     * Returns the service alias directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the <i>Redmine</i> {@link RedmineService} service.
     *
     * @see #serviceDir(Domain, Domain, RedmineService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, RedmineService service) {
        def serviceDir = serviceDir domain, refDomain, service
        service.alias.empty ? "/" : "/${service.alias}"
    }

    /**
     * Returns the service directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the <i>Redmine</i> {@link RedmineService} service.
     *
     * @see #gititDir(Domain, RedmineService)
     */
    String serviceDir(Domain domain, Domain refDomain, RedmineService service) {
        refDomain == null ? redmineDir(domain, service).absolutePath :
                redmineDir(refDomain, service).absolutePath
    }

    /**
     * Returns the <i>bundle</i> command, for
     * example {@code "/usr/local/bin/bundle".}
     *
     * <ul>
     * <li>profile property {@code "redmine_bundle_command"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getBundleCommand() {
        profileProperty "redmine_bundle_command", redmineProperties
    }

    /**
     * Returns the <i>rake</i> command, for
     * example {@code "/usr/local/bin/rake".}
     *
     * <ul>
     * <li>profile property {@code "redmine_rake_command"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRakeCommand() {
        profileProperty "redmine_rake_command", redmineProperties
    }

    /**
     * Returns the <i>gem</i> command, for
     * example {@code "/usr/bin/gem".}
     *
     * <ul>
     * <li>profile property {@code "redmine_gem_command"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getGemCommand() {
        profileProperty "redmine_gem_command", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> configuration file inside the
     * domain, for example {@code "/var/www/domain.com/redmineprefix/config/file.yml".}
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @param file
     *            the {@link String} file.
     *
     * @return the configuration {@link File} file.
     *
     * @see #redmineDir(Domain, RedmineService)
     * @see #getRedmineDatabaseConfigFile()
     * @see #getRedmineDatabaseConfigExampleFile()
     * @see #getRedmineConfigFile()
     * @see #getRedmineConfigExampleFile()
     * @see #getRedmineEnvironmentFile()
     */
    File redmineConfigFile(Domain domain, RedmineService service, String file) {
        def dir = redmineDir domain, service
        new File(file, dir)
    }

    /**
     * Returns the <i>Redmine</i> environment file, for
     * example {@code "config/environment.rb".}
     *
     * <ul>
     * <li>profile property {@code "redmine_environment_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRedmineEnvironmentFile() {
        profileProperty "redmine_environment_file", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> packages, for
     * example {@code "sass, compass, bundler".}
     *
     * <ul>
     * <li>profile property {@code "redmine_gems"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    List getRedmineGems() {
        profileListProperty "redmine_gems", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> packages, for
     * example {@code "development, test".}
     *
     * <ul>
     * <li>profile property {@code "redmine_production_excluded_bundles"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    List getProductionExcludedBundles() {
        profileListProperty "redmine_production_excluded_bundles", redmineProperties
    }

    /**
     * Returns the <i>Rails</i> production environment name, for
     * example {@code "production".}
     *
     * <ul>
     * <li>profile property {@code "redmine_rails_production"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRailsProduction() {
        profileProperty "redmine_rails_production", redmineProperties
    }

    /**
     * Returns the <i>gem</i> install timeout duration, for
     * example {@code "PT1H".}
     *
     * <ul>
     * <li>profile property {@code "redmine_gem_install_timeout"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    Duration getGemInstallTimeout() {
        profileDurationProperty "redmine_gem_install_timeout", redmineProperties
    }

    /**
     * Returns the <i>bundle</i> install timeout duration, for
     * example {@code "PT1H".}
     *
     * <ul>
     * <li>profile property {@code "redmine_bundle_install_timeout"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    Duration getBundleInstallTimeout() {
        profileDurationProperty "redmine_bundle_install_timeout", redmineProperties
    }

    /**
     * Returns the migrate database timeout duration, for
     * example {@code "PT20M".}
     *
     * <ul>
     * <li>profile property {@code "redmine_migrate_database_timeout"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    Duration getMigrateDatabaseTimeout() {
        profileDurationProperty "redmine_migrate_database_timeout", redmineProperties
    }

    /**
     * Returns the default SCMs to install, for
     * example {@code "all".}
     *
     * <ul>
     * <li>profile property {@code "redmine_default_scms"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    List getDefaultScmInstall() {
        def list = profileListProperty "redmine_default_scms", redmineProperties
        def res = []
        list.each {
            res << ScmInstall.valueOf(it)
        }
        return res
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Redmine_2_5_Config"
        this.gemInstallTemplate = templates.getResource("gem_install")
        this.bundleInstallTemplate = templates.getResource("bundle_install")
        this.rakeCommandsTemplate = templates.getResource("rake_commands")
        this.environmentConfigTemplate = templates.getResource("environment_config")
    }

    /**
     * Returns the <i>Redmine</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getRedmineProperties()

    /**
     * Returns the <i>Redmine</i> service name.
     */
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    LinuxScript getScript() {
        script
    }

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
