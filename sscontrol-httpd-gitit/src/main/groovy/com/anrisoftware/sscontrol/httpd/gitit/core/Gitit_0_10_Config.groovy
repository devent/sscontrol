package com.anrisoftware.sscontrol.httpd.gitit.core

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04.GititConfigFactory
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures <i>Gitit 0.10.</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Gitit_0_10_Config {

    @Inject
    private Gitit_0_10_ConfigLogger log

    @Inject
    private ExecCommandWorkerFactory execCommandWorkerFactory

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
        createDefaultConfig domain, service
        createService domain, service
        deployConfig domain, service
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
        def search = gititConfigTemplate.getText(true, "configEnding_search")
        def replace = wordpressConfigTemplate.getText(true, "configEnding")
        def temp = new TokenTemplate(search, replace)
        def configs = [temp]
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
        deployConfiguration configurationTokens(), conf, configs, file
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
            log.defaultConfigCreated this, file, config
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
        log.serviceDefaultsFileCreated this, defaultsFile, conf
        args.domainName = domainNameAsFileName domain
        args.gititCommand = gititCommand domain, service
        args.gititConfig = gititConfigFile domain, service
        args.gititDir = gititDir domain, service
        conf = gititServiceTemplate.getText(true, "gititService", "args", args)
        FileUtils.write serviceFile, conf, charset
        log.serviceFileCreated this, serviceFile, conf
        changeMod mod: "+x", files: serviceFile
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
     * @param args the {@link Map} arguments;
     * <ul>
     * <li>{@code packages} the {@link List} of the packages to install.</li>
     * <ul>
     *
     * @see #getCabalCommand()
     * @see #getGititProperties()
     */
    void installCabalPackages(Map args) {
        args.cabalCommand = args.containsKey("cabalCommand") ? args.cabalCommand : cabalCommand
        def worker = scriptCommandFactory.create(gititCommandTemplate, "cabalInstallCommand", "args", args)()
        log.installCabalPackagesDone this, worker, args
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
        def worker = execCommandWorkerFactory.create("$command --print-default-config")()
        worker.out
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
        GititConfigFactory.SERVICE_NAME
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
