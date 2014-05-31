/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.AuthMethod
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.LoginRequired
import com.anrisoftware.sscontrol.httpd.gitit.RepositoryType
import com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04.GititConfigFactory
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * Configures <i>Gitit 0.10.</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Gitit_0_10_Config {

    @Inject
    private Gitit_0_10_ConfigLogger logg

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    DebugLoggingFactory debugLoggingFactory

    @Inject
    DebugLevelRenderer debugLevelRenderer

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
        setupDefaults domain, service
        installPackages service
        createDefaultConfig domain, service
        createService domain, service
        deployConfig domain, service
    }

    /**
     * Setups default options.
     *
     * @param service
     *            the {@link GititService}.
     */
    void setupDefaults(Domain domain, GititService service) {
        setupDefaultDebug service, domain
        def address = profileProperty("gitit_default_bind_address", gititProperties)
        def port = profileNumberProperty("gitit_default_bind_port", gititProperties).intValue()
        def loginRequired = LoginRequired.valueOf(profileProperty("gitit_default_login_required", gititProperties))
        def authMethod = AuthMethod.valueOf(profileProperty("gitit_default_auth_method", gititProperties))
        def pageType = profileProperty("gitit_default_page_type", gititProperties)
        def math = profileProperty("gitit_default_math", gititProperties)
        def frontpage = profileProperty("gitit_default_frontpage", gititProperties)
        def nodeletePages = profileListProperty("gitit_default_nodelete_pages", gititProperties)
        def noeditPages = profileListProperty("gitit_default_noedit_pages", gititProperties)
        def summary = profileProperty("gitit_default_summary", gititProperties)
        def tableofcontents = profileProperty("gitit_default_tableofcontents", gititProperties)
        def enableCache = profileBooleanProperty("gitit_default_enable_cache", gititProperties)
        def allowIdlegc = profileBooleanProperty("gitit_default_allow_idle_gc", gititProperties)
        def memoryUpload = profileProperty "gitit_default_memory_upload", gititProperties
        def memoryPage = profileProperty "gitit_default_memory_page", gititProperties
        def compressResponses = profileBooleanProperty("gitit_default_compress_responses", gititProperties)
        def recaptchaEnabled = profileBooleanProperty("gitit_default_recaptcha_enabled", gititProperties)
        def accessQuestion = profileProperty("gitit_default_access_question", gititProperties)
        def accessAnswer = profileProperty("gitit_default_access_answer", gititProperties)
        def feedsEnabled = profileBooleanProperty("gitit_default_feeds_enabled", gititProperties)
        def feedsDuration = profileProperty("gitit_default_feeds_duration", gititProperties)
        def feedsRefresh = profileProperty("gitit_default_feeds_refresh", gititProperties)
        if (service.binding.addresses.size() == 0) {
            service.bind address: address, port: port
        }
        if (service.loginRequired == null) {
            service.loginRequired = loginRequired
        }
        if (service.authMethod == null) {
            service.authMethod = authMethod
        }
        if (service.pageType == null) {
            service.pageType = pageType
        }
        if (service.math == null) {
            service.math = math
        }
        if (service.frontPage == null) {
            service.frontPage = frontpage
        }
        if (service.noDeletePages == null) {
            service.noDeletePages = nodeletePages
        }
        if (service.noEditPages == null) {
            service.noEditPages = noeditPages
        }
        if (service.defaultSummary == null) {
            service.defaultSummary = summary
        }
        if (service.tableOfContents == null) {
            service.tableOfContents = tableofcontents
        }
        if (service.caching == null) {
            service.caching = enableCache
        }
        if (service.idleGc == null) {
            service.idleGc = allowIdlegc
        }
        if (service.memoryUpload == null) {
            service.memoryUpload = memoryUpload
        }
        if (service.memoryPage == null) {
            service.memoryPage = memoryPage
        }
        if (service.compressResponses == null) {
            service.compressResponses = compressResponses
        }
        if (service.recaptchaEnable == null) {
            service.recaptchaEnable = recaptchaEnabled
        }
        if (service.recaptchaPrivateKey == null) {
            service.recaptchaPrivateKey = accessQuestion
        }
        if (service.recaptchaPublicKey == null) {
            service.recaptchaPublicKey = accessAnswer
        }
        if (service.feedsEnabled == null) {
            service.feedsEnabled = feedsEnabled
        }
        if (service.feedsDuration == null) {
            service.feedsDuration = feedsDuration
        }
        if (service.feedsRefresh == null) {
            service.feedsRefresh = feedsRefresh
        }
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void setupDefaultDebug(GititService service, Domain domain) {
        def level = profileNumberProperty "gitit_default_debug_level", gititProperties
        def file = gititDefaultDebugFile domain, service
        def infoEnabled = profileBooleanProperty "gitit_default_debug_information_enabled", gititProperties
        service.debug = service.debug == null ? debugLoggingFactory.create(level) : service.debug
        service.debug.args.file = service.debug.args.file == null ? file : service.debug.args.file
        service.debug.args.infoEnabled = service.debug.args.infoEnabled == null ? infoEnabled : service.debug
    }

    /**
     * Installs the needed <i>Gitit</i> repository type packages.
     *
     * @param service
     *            the {@link GititService}.
     */
    void installPackages(GititService service) {
        def packages
        switch (service.type) {
            case RepositoryType.git:
                packages = gitPackages
                break
            case RepositoryType.darcs:
                packages = darcsPackages
                break
            case RepositoryType.mercurial:
                packages = mercurialPackages
                break
        }
        installPackagesFactory.create(
                log: log, command: script.installCommand, packages: packages,
                this, threads)()
    }

    /**
     * Deploys the <i>Gitit</i> configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     *
     * @see #gititConfigFile(Domain, GititService)
     */
    void deployConfig(Domain domain, GititService service) {
        def configs = [
            configToken("addressConfig", "address", service.binding.addresses[0].address),
            configToken("portConfig", "port", service.binding.addresses[0].port),
            configToken("wikititleConfig", "title", service.wikiTitle),
            configToken("repositorytypeConfig", "type", service.type),
            configToken("requireauthenticationConfig", "type", service.loginRequired),
            configToken("authenticationmethodConfig", "method", service.authMethod),
            configToken("defaultpagetypeConfig", "type", service.pageType),
            configToken("mathConfig", "math", service.math),
            configToken("logfileConfig", "file", service.debug.args.file),
            configToken("loglevelConfig", "level", debugLevelRenderer.toString(service.debug.level)),
            configToken("frontpageConfig", "page", service.frontPage),
            configToken("nodeleteConfig", "pages", service.noDeletePages),
            configToken("noeditConfig", "pages", service.noEditPages),
            configToken("defaultsummaryConfig", "summary", service.defaultSummary),
            configToken("tableofcontentsConfig", "enabled", service.tableOfContents),
            configToken("usecacheConfig", "enabled", service.caching),
            configToken("maxuploadsizeConfig", "size", service.memoryUpload),
            configToken("maxpagesizeConfig", "size", service.memoryPage),
            configToken("debugmodeConfig", "enabled", service.debug.args.infoEnabled),
            configToken("compressresponsesConfig", "enabled", service.compressResponses),
            configToken("userecaptchaConfig", "enabled", service.recaptchaEnable),
            configToken("recaptchaprivatekeyConfig", "key", service.recaptchaPrivateKey),
            configToken("recaptchapublickeyConfig", "key", service.recaptchaPublicKey),
            configToken("accessquestionConfig", "question", service.accessQuestion),
            configToken("accessquestionanswersConfig", "answers", service.accessAnswers),
            configToken("usefeedConfig", "enabled", service.feedsEnabled),
            configToken("feeddaysConfig", "days", service.feedsDuration),
            configToken("feedrefreshtimeConfig", "minutes", service.feedsRefresh),
        ]
        def file = gititConfigFile domain, service
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    TokenTemplate configToken(Object[] args) {
        def search = gititConfigTemplate.getText(true, "${args[0]}Search")
        def replace = gititConfigTemplate.getText(true, args)
        new TokenTemplate(search, replace)
    }

    /**
     * Creates the default <i>Gitit</i> configuration file.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void createDefaultConfig(Domain domain, GititService service) {
        def config = defaultConfig domain, service
        def file = gititConfigFile domain, service
        if (!file.isFile()) {
            FileUtils.write file, config, charset
            logg.defaultConfigCreated this, file, config
        }
    }

    /**
     * Creates the <i>Gitit</i> service file.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void createService(Domain domain, GititService service) {
        def gitit = gititCommand domain, service
        def config = gititConfigFile domain, service
        def serviceFile = gititServiceFile domain
        def defaultsFile = gititServiceDefaultsFile domain
        def args = [:]
        args.gititScript = serviceFile
        args.userName = domain.domainUser.name
        def conf = gititServiceDefaultsTemplate.getText(true, "gititDefaults", "args", args)
        FileUtils.write defaultsFile, conf, charset
        logg.serviceDefaultsFileCreated this, defaultsFile, conf
        args.domainName = domainNameAsFileName domain
        args.gititCommand = gititCommand domain, service
        args.gititConfig = gititConfigFile domain, service
        args.gititDir = gititDir domain, service
        conf = gititServiceTemplate.getText(true, "gititService", "args", args)
        FileUtils.write serviceFile, conf, charset
        logg.serviceFileCreated this, serviceFile, conf
        changeFileModFactory.create(
                log: log, mod: "+x", files: serviceFile,
                command: script.chmodCommand,
                this, threads)()
    }

    /**
     * Returns the <i>Gitit</i> service configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititConfigTemplate()

    /**
     * Returns the <i>Gitit</i> service defaults template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititServiceDefaultsTemplate()

    /**
     * Returns the <i>Gitit</i> service template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititServiceTemplate()

    /**
     * Returns the list of needed packages for <i>Gitit</i>.
     *
     * <ul>
     * <li>profile property {@code "gitit_packages"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    List getGititPackages() {
        profileListProperty "gitit_packages", gititProperties
    }

    /**
     * Returns the <i>cabal</i> command, for example {@code "/usr/bin/cabal".}
     *
     * <ul>
     * <li>profile property {@code "cabal_command"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    String getCabalCommand() {
        profileProperty "cabal_command", gititProperties
    }

    /**
     * Installs the <i>cabal</i> packages.
     *
     * @see #getCabalCommand()
     */
    void installCabalPackages(def packages) {
        def task = scriptExecFactory.create(
                log: log, command: cabalCommand, packages: packages,
                bashCommand: bashCommand, timeout: cabalInstallTimeout,
                this, threads, gititCommandTemplate, "cabalInstallCommand")()
        logg.installCabalPackagesDone this, task, packages
    }

    /**
     * Returns the <i>Gitit</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see GititService#getPrefix()
     */
    File gititDir(Domain domain, GititService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Creates the default <i>Gitit</i> configuration file.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     *
     * @return the {@link String} configuration.
     */
    String defaultConfig(Domain domain, GititService service) {
        def command = gititCommand domain, service
        def task = scriptExecFactory.create(
                log: log, command: command, outString: true,
                this, threads, gititCommandTemplate, "printDefaultConfigCommand")()
        return task.out
    }

    /**
     * Returns the <i>Gitit</i> configuration file inside the domain, for
     * example {@code "/var/www/domain.com/gitit/gitit.conf".}
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #gititDir(Domain, GititService)
     */
    File gititConfigFile(Domain domain, GititService service) {
        def dir = gititDir domain, service
        new File(gititConfigFileName, dir)
    }

    /**
     * Returns the <i>Gitit</i> configuration file property, for
     * example {@code "gitit.conf".}
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @return the configuration {@link File} file.
     *
     * <ul>
     * <li>profile property {@code "gitit_configuration_file"}</li>
     * </ul>
     *
     * @see #gititDir(Domain, GititService)
     */
    String getGititConfigFileName() {
        profileProperty "gitit_configuration_file_name", gititProperties
    }

    /**
     * Returns the <i>Gitit</i> service file, for
     * example {@code "/etc/init.d/<domainName>_gititd".} The placeholder
     * {@code "domainName"} is replaced by the specified domain name.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @return the configuration {@link File} file.
     *
     * <ul>
     * <li>profile property {@code "gitit_service_file"}</li>
     * </ul>
     */
    File gititServiceFile(Domain domain) {
        def str = profileProperty "gitit_service_file", gititProperties
        def name = domainNameAsFileName domain
        new File(new ST(str).add("domainName", name).render())
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
     * Returns the <i>Gitit</i> service defaults file, for
     * example {@code "/etc/default/<domainName>_gititd".} The placeholder
     * {@code "domainName"} is replaced by the specified domain name.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @return the configuration {@link File} file.
     *
     * <ul>
     * <li>profile property {@code "gitit_service_defaults_file"}</li>
     * </ul>
     */
    File gititServiceDefaultsFile(Domain domain) {
        def str = profileProperty "gitit_service_defaults_file", gititProperties
        def name = domainNameAsFileName domain
        new File(new ST(str).add("domainName", name).render())
    }

    /**
     * Returns the <i>Gitit</i> service default debug file, for
     * example {@code "<gititDir>/gitit.log".} The placeholder
     * {@code "gititDir"} is replaced by the specified <i>Gitit</i> directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @return the default debug {@link File} file.
     *
     * <ul>
     * <li>profile property {@code "gitit_default_debug_file"}</li>
     * </ul>
     */
    File gititDefaultDebugFile(Domain domain, GititService service) {
        def str = profileProperty "gitit_default_debug_file", gititProperties
        def dir = gititDir domain, service
        new File(new ST(str).add("gititDir", dir).render())
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link GititService}.
     *
     * @return the location.
     */
    String serviceLocation(GititService service) {
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
     *            the <i>Gitit</i> {@link GititService} service.
     *
     * @see #serviceDir(Domain, Domain, GititService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, GititService service) {
        def serviceDir = serviceDir domain, refDomain, service
        service.alias.empty ? "$serviceDir/" : serviceDir
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
     *            the <i>Gitit</i> {@link GititService} service.
     *
     * @see #gititDir(Domain, GititService)
     */
    String serviceDir(Domain domain, Domain refDomain, GititService service) {
        refDomain == null ? gititDir(domain, service).absolutePath :
                gititDir(refDomain, service).absolutePath
    }

    /**
     * Returns the timeout duration to install <i>cabal</i> packages, for
     * example {@code "PT4H".}
     *
     * <ul>
     * <li>profile property {@code "hsenv_cabal_install_timeout"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    Duration getCabalInstallTimeout() {
        profileDurationProperty "cabal_install_timeout", gititProperties
    }

    /**
     * Returns the <i>bash</i> command, for
     * example {@code "/bin/bash".}
     *
     * <ul>
     * <li>profile property {@code "bash_command"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    String getBashCommand() {
        profileProperty "bash_command", gititProperties
    }

    /**
     * Returns the <i>git</i> packages, for
     * example {@code "git".}
     *
     * <ul>
     * <li>profile property {@code "gitit_git_packages"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    List getGitPackages() {
        profileListProperty "gitit_git_packages", gititProperties
    }

    /**
     * Returns the <i>darcs</i> packages, for
     * example {@code "darcs".}
     *
     * <ul>
     * <li>profile property {@code "gitit_darcs_packages"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    List getDarcsPackages() {
        profileListProperty "gitit_darcs_packages", gititProperties
    }

    /**
     * Returns the <i>mercurial</i> packages, for
     * example {@code "mercurial".}
     *
     * <ul>
     * <li>profile property {@code "gitit_mercurial_packages"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    List getMercurialPackages() {
        profileListProperty "gitit_mercurial_packages", gititProperties
    }

    /**
     * Returns the <i>Gitit</i> command.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @return the {@link String} command.
     */
    abstract String gititCommand(Domain domain, GititService service)

    /**
     * Returns the <i>Gitit</i> commands template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititCommandTemplate()

    /**
     * Returns the default <i>Gitit</i> properties.
     *
     * @return the <i>Gitit</i> {@link ContextProperties} properties.
     */
    abstract ContextProperties getGititProperties()

    /**
     * Returns the <i>Gitit</i> service name.
     */
    String getServiceName() {
        GititConfigFactory.WEB_NAME
    }

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
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
