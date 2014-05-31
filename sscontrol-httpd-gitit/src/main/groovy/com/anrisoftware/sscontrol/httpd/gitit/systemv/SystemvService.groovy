package com.anrisoftware.sscontrol.httpd.gitit.systemv

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04.GititConfigFactory
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory

/**
 * <i>SystemV</i> <i>Gitit</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class SystemvService {

    @Inject
    SystemvServiceLogger logg

    @Inject
    ChangeFileModFactory changeFileModFactory

    /**
     * The parent script that returns the properties.
     */
    Object script

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
        logg.serviceDefaultsFileCreated script, defaultsFile, conf
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
     * Returns the <i>Gitit</i> service name.
     */
    String getServiceName() {
        GititConfigFactory.WEB_NAME
    }

    /**
     * Sets the parent script that returns the properties.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the resources containing the <i>gitit</i> service defaults
     * configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititServiceDefaultsTemplate()

    /**
     * Returns the resources containing the <i>gitit</i> service
     * configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititServiceTemplate()

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
                .append("service name", getServiceName()).toString();
    }
}
