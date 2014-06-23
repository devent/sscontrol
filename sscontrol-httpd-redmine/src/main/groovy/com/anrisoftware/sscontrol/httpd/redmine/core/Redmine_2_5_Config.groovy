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
package com.anrisoftware.sscontrol.httpd.redmine.core

import groovy.util.logging.Slf4j

import java.util.regex.Pattern

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.AuthenticationMethod
import com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04.RedmineConfigFactory
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * Configures <i>Redmine 2.5</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Redmine_2_5_Config {

    @Inject
    private Redmine_2_5_ConfigLogger logg

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

    @Inject
    TemplatesFactory templatesFactory

    Templates redmineConfigTemplates

    TemplateResource redmineDatabaseConfigTemplate

    TemplateResource redmineConfigTemplate

    TemplateResource redmineGemInstallTemplate

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript script

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List config) {
        installGems domain, service
        deployDatabase domain, service
        deployConfig domain, service
        setupPermissions domain, service
    }

    /**
     * Setups default options.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupDefaults(Domain domain, RedmineService service) {
        setupDefaultOverrideMode service, domain
        setupDefaultDebug service, domain
        setupDefaultDatabase service, domain
        setupDefaultPrefix service, domain
        setupDefaultAlias service, domain
        setupDefaultMail service, domain
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupDefaultDebug(RedmineService service, Domain domain) {
        int level = profileNumberProperty "redmine_default_debug_level", redmineProperties
        service.debug = service.debug == null ? debugLoggingFactory.create(level) : service.debug
    }

    /**
     * Setups the default override mode.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupDefaultOverrideMode(RedmineService service, Domain domain) {
        if (service.overrideMode == null) {
            OverrideMode mode = OverrideMode.valueOf profileProperty("redmine_default_override_mode", redmineProperties)
            service.overrideMode = mode
        }
    }

    /**
     * Setups the default database.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupDefaultDatabase(RedmineService service, Domain domain) {
        def provider = profileProperty "redmine_default_database_provider", redmineProperties
        def encoding = profileProperty "redmine_default_database_encoding", redmineProperties
        service.database.provider = service.database.provider == null ? provider : service.database.provider
        service.database.encoding = service.database.encoding == null ? encoding : service.database.encoding
    }

    /**
     * Setups the default prefix.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupDefaultPrefix(RedmineService service, Domain domain) {
        def prefix = profileProperty "redmine_default_prefix", redmineProperties
        service.prefix = service.prefix == null ? prefix : service.prefix
    }

    /**
     * Setups the default alias.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupDefaultAlias(RedmineService service, Domain domain) {
        service.alias = service.alias == null ? "" : service.alias
    }

    /**
     * Setups the default mail.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupDefaultMail(RedmineService service, Domain domain) {
        if (service.mailHost == null) {
            service.mailHost = domain.name
        }
        if (service.mailDomain == null) {
            service.mailDomain = domain.name
        }
        if (service.mailPort == null) {
            service.mailPort = defaultMailPort
        }
        if (service.mailDeliveryMethod == null) {
            service.mailDeliveryMethod = defaultMailDeliveryMethod
        }
        if (service.mailAuthMethod == null) {
            service.mailAuthMethod = defaultMailAuthenticationMethod
        }
    }

    /**
     * Installs the <i>Ruby</i> gems.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void installGems(Domain domain, RedmineService service) {
        scriptExecFactory.create(
                log: log,
                gemCommand: gemCommand,
                gems: redmineGems,
                this, threads, redmineGemInstallTemplate, "gemInstall")()
    }

    /**
     * Deploys the <i>Redmine</i> database configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * @see #getRedmineDatabaseConfigExampleFile()
     * @see #getRedmineDatabaseConfigFile()
     */
    void deployDatabase(Domain domain, RedmineService service) {
        def exampleFile = redmineConfigFile domain, service, redmineDatabaseConfigExampleFile
        def file = redmineConfigFile domain, service, redmineDatabaseConfigFile
        file.isFile() == false ? FileUtils.copyFile(exampleFile, file) : false
        def conf = currentConfiguration file
        def search = redmineDatabaseConfigTemplate.getText(true, "productionDatabaseSearch")
        def replace = redmineDatabaseConfigTemplate.getText(true, "productionDatabase", "database", service.database)
        def matcher = Pattern.compile(search).matcher(conf)
        conf = matcher.replaceAll(replace)
        FileUtils.writeStringToFile file, conf, charset
        logg.databaseConfigCreated this, file, conf
    }

    /**
     * Deploys the <i>Redmine</i> configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * @see #getRedmineConfigExampleFile()
     * @see #getRedmineConfigFile()
     */
    void deployConfig(Domain domain, RedmineService service) {
        def exampleFile = redmineConfigFile domain, service, redmineConfigExampleFile
        def file = redmineConfigFile domain, service, redmineConfigFile
        file.isFile() == false ? FileUtils.copyFile(exampleFile, file) : false
        def conf = new ArrayList(StringUtils.splitPreserveAllTokens(currentConfiguration(file), '\n') as List)
        int i = 0
        def res
        for (i; i < conf.size(); i++) {
            if (conf[i].startsWith("production:")) {
                res = parseConfig domain, service, conf, ++i
                break
            }
        }
        FileUtils.writeLines file, charset.name(), res
        logg.configCreated this, file, conf
    }

    List parseConfig(Domain domain, RedmineService service, List conf, int i) {
        def res = new LinkedList(conf)
        for (i; i < conf.size(); i++) {
            if (conf[i].empty) {
                def str = StringUtils.splitPreserveAllTokens redmineConfigTemplate.getText(true, "productionEmail", "service", service), '\n'
                str.eachWithIndex { it, int k ->
                    res.add i + k, it
                }
                return res
            }
            if (conf[i].startsWith("  email_delivery:")) {
                return updateEmailDelivery(domain, service, conf, i)
            }
        }
        return res
    }

    List updateEmailDelivery(Domain domain, RedmineService service, List conf, int i) {
        for (i; i < conf.size(); i++) {
            if (conf[i].startsWith("    delivery_method:")) {
                conf[i] = redmineConfigTemplate.getText(true, "emailDeliveryMethod", "service", service)
            }
            if (conf[i].startsWith("      address:")) {
                conf[i] = redmineConfigTemplate.getText(true, "emailAddress", "service", service)
            }
            if (conf[i].startsWith("      port:")) {
                conf[i] = redmineConfigTemplate.getText(true, "emailPort", "service", service)
            }
            if (conf[i].startsWith("      domain:")) {
                conf[i] = redmineConfigTemplate.getText(true, "emailDomain", "service", service)
            }
            if (conf[i].startsWith("      authentication:")) {
                conf[i] = redmineConfigTemplate.getText(true, "emailAuthentication", "service", service)
            }
            if (conf[i].startsWith("      user_name:")) {
                conf[i] = redmineConfigTemplate.getText(true, "emailUser", "service", service)
            }
            if (conf[i].startsWith("      password:")) {
                conf[i] = redmineConfigTemplate.getText(true, "emailPassword", "service", service)
            }
        }
        return conf
    }

    /**
     * Sets the permissions of the <i>Redmine</i> directories.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
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
     * Returns the <i>Redmine</i> installation directory, for example
     * {@code /var/www/domain.com/redmineprefix}
     *
     * @param domain
     *            the {@link Domain} domain of the service.
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
     *            the {@link RedmineService}.
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
     */
    File redmineConfigFile(Domain domain, RedmineService service, String file) {
        def dir = redmineDir domain, service
        new File(file, dir)
    }

    /**
     * Returns the <i>Redmine</i> database configuration file property, for
     * example {@code "config/database.yml".}
     *
     * <ul>
     * <li>profile property {@code "redmine_database_configuration_file"}</li>
     * </ul>
     *
     * @see #gititDir(Domain, RedmineService)
     */
    String getRedmineDatabaseConfigFile() {
        profileProperty "redmine_database_configuration_file", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> database example configuration file
     * property, for example {@code "config/database.yml.example".}
     *
     * <ul>
     * <li>profile property {@code "redmine_database_configuration_example_file"}</li>
     * </ul>
     *
     * @see #gititDir(Domain, RedmineService)
     */
    String getRedmineDatabaseConfigExampleFile() {
        profileProperty "redmine_database_configuration_example_file", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> configuration file property, for
     * example {@code "config/configuration.yml".}
     *
     * <ul>
     * <li>profile property {@code "redmine_configuration_file"}</li>
     * </ul>
     *
     * @see #gititDir(Domain, RedmineService)
     */
    String getRedmineConfigFile() {
        profileProperty "redmine_configuration_file", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> example configuration file property, for
     * example {@code "config/configuration.yml.example".}
     *
     * <ul>
     * <li>profile property {@code "redmine_configuration_example_file"}</li>
     * </ul>
     *
     * @see #gititDir(Domain, RedmineService)
     */
    String getRedmineConfigExampleFile() {
        profileProperty "redmine_configuration_example_file", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> packages, for
     * example {@code "ruby, rake, rubygems, libopenssl-ruby, libmysql-ruby, ruby-dev, libmysqlclient-dev, libmagick-dev, curl, libmagickwand-dev".}
     *
     * <ul>
     * <li>profile property {@code "redmine_packages"}</li>
     * </ul>
     *
     * @see #getRedminePackages()
     */
    List getRedminePackages() {
        profileListProperty "redmine_packages", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> packages, for
     * example {@code "sass, compass, bundler".}
     *
     * <ul>
     * <li>profile property {@code "redmine_gems"}</li>
     * </ul>
     *
     * @see #getRedminePackages()
     */
    List getRedmineGems() {
        profileListProperty "redmine_gems", redmineProperties
    }

    /**
     * Returns the default mail port, for
     * example {@code "25".}
     *
     * <ul>
     * <li>profile property {@code "redmine_default_mail_port"}</li>
     * </ul>
     */
    int getDefaultMailPort() {
        profileNumberProperty "redmine_default_mail_port", redmineProperties
    }

    /**
     * Returns the default mail delivery method, for
     * example {@code "smtp".}
     *
     * <ul>
     * <li>profile property {@code "redmine_default_mail_delivery_method"}</li>
     * </ul>
     */
    DeliveryMethod getDefaultMailDeliveryMethod() {
        def p = profileProperty "redmine_default_mail_delivery_method", redmineProperties
        DeliveryMethod.valueOf(p)
    }

    /**
     * Returns the default mail authentication method, for
     * example {@code "login".}
     *
     * <ul>
     * <li>profile property {@code "redmine_default_mail_authentication_method"}</li>
     * </ul>
     */
    AuthenticationMethod getDefaultMailAuthenticationMethod() {
        def p = profileProperty "redmine_default_mail_authentication_method", redmineProperties
        AuthenticationMethod.valueOf(p)
    }

    /**
     * Returns the <i>Redmine</i> service name.
     */
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

    /**
     * Returns the <i>Redmine</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getRedmineProperties()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
        this.redmineConfigTemplates = templatesFactory.create("Redmine_2_5_Config",
                [renderers: [
                        deliveryMethodAttributeRenderer,
                        authenticationMethodAttributeRenderer
                    ]])
        this.redmineDatabaseConfigTemplate = redmineConfigTemplates.getResource("database_config")
        this.redmineConfigTemplate = redmineConfigTemplates.getResource("config")
        this.redmineGemInstallTemplate = redmineConfigTemplates.getResource("gem_install")
    }

    /**
     * Delegates missing properties to {@link LinuxScript}.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to {@link LinuxScript}.
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
