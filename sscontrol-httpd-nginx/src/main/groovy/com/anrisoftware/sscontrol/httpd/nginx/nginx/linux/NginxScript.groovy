/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject
import javax.measure.Measure
import javax.measure.MeasureFormat
import javax.measure.unit.NonSI

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.globalpom.format.byteformat.UnitMultiplier
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.bindings.BindingFactory
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.domain.DomainImpl
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigInfo
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker
import com.google.inject.Injector

/**
 * Uses Nginx service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class NginxScript extends LinuxScript {

    private static final String NGINX_NAME = "nginx"

    @Inject
    private NginxScriptLogger log

    @Inject
    private BindingFactory bindingFactory

    @Inject
    private DebugLoggingProperty debugLoggingProperty

    @Inject
    Injector injector

    @Inject
    ByteFormatFactory byteFormatFactory

    @Inject
    Map<String, ServiceConfig> serviceConfigs

    @Inject
    WebServicesConfigProvider webServicesConfigProvider

    @Override
    def run() {
        super.run()
        setupDefaultBinding()
        setupDefaultLogging()
    }

    /**
     * Setups the default binding addresses.
     */
    void setupDefaultBinding() {
        if (service.binding.size() == 0) {
            defaultBinding.each { service.binding.addAddress(it) }
        }
    }

    /**
     * Setups the default debug logging.
     */
    void setupDefaultLogging() {
        if (service.debug == null) {
            service.debug = debugLoggingProperty.defaultDebug this
        }
        if (!service.debug.args.containsKey("storage")) {
            service.debug.args.storage = loggingStorage
        }
    }

    @Override
    HttpdService getService() {
        super.getService();
    }

    /**
     * Finds the service configuration for the specified profile and service.
     *
     * @param profile
     *            the profile {@link String} name.
     *
     * @param service
     *            the {@link WebService}.
     *
     * @return the {@link ServiceConfig}.
     */
    ServiceConfig findServiceConfig(String profile, WebService service) {
        def config = serviceConfigs["${profile}.${service.name}"]
        config = config != null ? config : findWebServicesConfigProvider(profile, service)
        log.checkServiceConfig config, service, profile
        return config
    }

    private ServiceConfig findWebServicesConfigProvider(String profile, WebService service) {
        def factory = webServicesConfigProvider.find(
                [getServiceName: { NGINX_NAME }, getWebName: { service.name }, getProfileName: { profile }] as ServiceConfigInfo)
        factory.setParent injector
        def script = factory.getScript()
        script.setScript this
        return script
    }

    /**
     * Returns the restart command for the Nginx service.
     *
     * <ul>
     * <li>property key {@code nginx_restart_command}</li>
     * <li>property key {@code restart_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    @Override
    String getRestartCommand() {
        if (containsKey("nginx_restart_command")) {
            profileProperty "nginx_restart_command", defaultProperties
        } else {
            profileProperty "restart_command", defaultProperties
        }
    }

    /**
     * Returns the path of the Nginx configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_configuration_directory"}</li>
     * <li>profile property {@code "configuration_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    @Override
    File getConfigurationDir() {
        if (containsKey("nginx_configuration_directory")) {
            profileDirProperty "nginx_configuration_directory", defaultProperties
        } else {
            profileDirProperty "configuration_directory", defaultProperties
        }
    }

    /**
     * Returns the directory for the included configuration, for
     * example {@code "conf.d".} If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_config_include_directory"}</li>
     * <li>profile property {@code "config_include_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getConfigIncludeDir() {
        if (containsKey("nginx_config_include_directory")) {
            profileFileProperty "nginx_config_include_directory", configurationDir, defaultProperties
        } else {
            profileFileProperty "config_include_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the directory for the available sites, for
     * example {@code "sites-available".} If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_sites_available_directory"}</li>
     * <li>profile property {@code "sites_available_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getSitesAvailableDir() {
        if (containsKey("nginx_sites_available_directory")) {
            profileFileProperty "nginx_sites_available_directory", configurationDir, defaultProperties
        } else {
            profileFileProperty "sites_available_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the directory for the enabled sites, for
     * example {@code "sites-enabled".} If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_sites_enabled_directory"}</li>
     * <li>profile property {@code "sites_enabled_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getSitesEnabledDir() {
        if (containsKey("nginx_sites_enabled_directory")) {
            profileFileProperty "nginx_sites_enabled_directory", configurationDir, defaultProperties
        } else {
            profileFileProperty "sites_enabled_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the path for the parent directory containing the sites,
     * for example {@code "/var/www".}
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
     * Returns the path for the Nginx configuration file,
     * for example {@code "nginx.conf".}  If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_config_file"}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     * @see #getDefaultProperties()
     */
    File getNginxConfigFile() {
        profileFileProperty "nginx_config_file", configurationDir, defaultProperties
    }

    /**
     * Returns the path for the included configuration file,
     * for example {@code "000-robobee_defaults.conf".}  If the path is
     * not absolute then it is assume to be under the configuration
     * include directory.
     *
     * <ul>
     * <li>profile property {@code "config_include_file"}</li>
     * </ul>
     *
     * @see #getConfigIncludeDir()
     * @see #getDefaultProperties()
     */
    File getConfigIncludeFile() {
        profileFileProperty "config_include_file", configIncludeDir, defaultProperties
    }

    /**
     * Returns the path for the sites configuration file,
     * for example {@code "999-robobee_sites.conf".}  If the path is
     * not absolute then it is assume to be under the configuration
     * include directory.
     *
     * <ul>
     * <li>profile property {@code "config_sites_file"}</li>
     * </ul>
     *
     * @see #getConfigIncludeDir()
     * @see #getDefaultProperties()
     */
    File getConfigSitesFile() {
        profileFileProperty "config_sites_file", configIncludeDir, defaultProperties
    }

    /**
     * Returns the directory of static 50x-error files,
     * for example {@code "/usr/share/nginx/html".}
     *
     * <ul>
     * <li>profile property {@code "error_pages_dir"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getErrorPagesDir() {
        profileProperty("error_pages_dir", defaultProperties) as File
    }

    /**
     * Returns the name of the directory to store SSL/certificates files.
     *
     * <ul>
     * <li>profile property {@code "ssl_subdirectory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSslSubdirectory() {
        profileProperty "ssl_subdirectory", defaultProperties
    }

    /**
     * Returns the name of the directory to store the site web files.
     *
     * <ul>
     * <li>profile property {@code "web_subdirectory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getWebSubdirectory() {
        profileProperty "web_subdirectory", defaultProperties
    }

    /**
     * Returns the SSL/certificates directory for the domain.
     *
     * @see #domainDir(Domain)
     * @see #getSslSubdirectory()
     */
    File sslDir(Domain domain) {
        new File(domainDir(domain), sslSubdirectory)
    }

    /**
     * Returns the directory for the domain web files.
     *
     * @see #domainDir(Domain)
     * @see #getWebSubdirectory()
     */
    File webDir(Domain domain) {
        new File(domainDir(domain), webSubdirectory)
    }

    /**
     * Returns the domain site directory.
     *
     * @see #getSitesDirectory()
     */
    File domainDir(Domain domain) {
        new File(sitesDirectory, domain.name)
    }

    /**
     * Returns the default bind addresses.
     *
     * <ul>
     * <li>profile property {@code "default_binding"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultBinding() {
        profileListProperty "default_binding", defaultProperties
    }

    /**
     * Returns the index files for
     * example {@code "index.php,index.html,index.htm".}
     *
     * <ul>
     * <li>profile property {@code "index_files"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getIndexFiles() {
        profileListProperty "index_files", defaultProperties
    }

    /**
     * Returns the SSL protocols, for
     * example {@code "SSLv3,TLSv1".}
     *
     * <ul>
     * <li>profile property {@code "ssl_protocols"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getSslProtocols() {
        profileListProperty "ssl_protocols", defaultProperties
    }

    /**
     * Returns the SSL ciphers, for
     * example {@code "ALL,!ADH,!EXPORT56,RC4+RSA,+HIGH,+MEDIUM,+EXP".}
     *
     * <ul>
     * <li>profile property {@code "ssl_ciphers"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getSslCiphers() {
        profileListProperty "ssl_ciphers", defaultProperties
    }

    /**
     * Returns the SSL session timeout, for
     * example {@code "5m".}
     *
     * <ul>
     * <li>profile property {@code "ssl_session_timeout"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSslSessionTimeout() {
        profileProperty "ssl_session_timeout", defaultProperties
    }

    /**
     * Returns the logging storage, for
     * example {@code "/var/log/nginx/error.log".}
     *
     * <ul>
     * <li>profile property {@code "logging_storage"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getLoggingStorage() {
        profileProperty "logging_storage", defaultProperties
    }

    /**
     * Returns to enable compression, for example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "domain_compression"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getDomainCompression() {
        profileProperty "domain_compression", defaultProperties
    }

    /**
     * Returns the compression level, for example {@code "1".}
     *
     * <ul>
     * <li>profile property {@code "domain_compression_level"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getCompressionLevel() {
        profileNumberProperty "domain_compression_level", defaultProperties
    }

    /**
     * Returns the compression minimum size, for example {@code "1400".}
     *
     * <ul>
     * <li>profile property {@code "compression_min_size"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getCompressionMinSize() {
        def str = profileProperty "compression_min_size", defaultProperties
        MeasureFormat.getInstance().parseObject(str).intValue NonSI.BYTE
    }

    /**
     * Returns to enable sending {@code "Vary: Accept-Encoding"}, for
     * example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "compression_vary"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getCompressionVary() {
        profileBooleanProperty "compression_vary", defaultProperties
    }

    /**
     * Returns the MIME types for compression, for
     * example {@code "text/plain, text/css, image/png, image/gif, image/jpeg, application/x-javascript, text/xml, application/xml, application/xml+rss, text/javascript".}
     *
     * <ul>
     * <li>profile property {@code "compression_types"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getCompressionTypes() {
        profileListProperty "compression_types", defaultProperties
    }

    /**
     * Returns the compression exception, can be a regular expression, for
     * example {@code "MSIE [1-6]\\.(?!.*SV1)".}
     *
     * <ul>
     * <li>profile property {@code "compression_exception"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getCompressionException() {
        profileProperty "compression_exception", defaultProperties
    }

    /**
     * Returns the compression HTTP version, for
     * example {@code "1.1".}
     *
     * <ul>
     * <li>profile property {@code "compression_http_version"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getCompressionHttpVersion() {
        profileProperty "compression_http_version", defaultProperties
    }

    /**
     * Returns the group name pattern for site users.
     *
     * <ul>
     * <li>profile property {@code "group_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getGroupPattern() {
        profileProperty("group_pattern", defaultProperties)
    }

    /**
     * Returns the user name pattern for site users.
     *
     * <ul>
     * <li>profile property {@code "user_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUserPattern() {
        profileProperty("user_pattern", defaultProperties)
    }

    /**
     * Returns the minimum group ID for site users.
     *
     * <ul>
     * <li>profile property {@code "minimum_gid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumGid() {
        profileNumberProperty("minimum_gid", defaultProperties)
    }

    /**
     * Returns the minimum user ID for site users.
     *
     * <ul>
     * <li>profile property {@code "minimum_uid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumUid() {
        profileNumberProperty("minimum_uid", defaultProperties)
    }

    /**
     * Returns the {@code nginx} service name that listens to the open ports,
     * for example {@code "nginx.conf".}
     *
     * <ul>
     * <li>profile property {@code "nginx_service"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getNginxService() {
        profileProperty "nginx_service", defaultProperties
    }

    /**
     * Returns unique domains. The domains are identified by their name.
     */
    List getUniqueDomains() {
        List domains = []
        Set names = []
        service.domains.each { DomainImpl domain ->
            if (!names.contains(domain.name)) {
                names.add domain.name
                domains.add domain
            }
        }
        return domains
    }

    /**
     * Enable the specified sites.
     */
    def enableSites(List sites) {
        def files = sites.inject([]) { acc, val ->
            acc << new File(sitesAvailableDir, val)
        }
        def targets = sites.inject([]) { acc, val ->
            acc << new File(sitesEnabledDir, val)
        }
        link files: files, targets: targets, override: true
        log.enabledSites this, sites
    }

    /**
     * Find the services that use the ports of the specified domains.
     *
     * @param domains
     *            the list of {@link Domain} domains.
     *
     * @return the {@link Map} of services: {@code port:=service}.
     *
     * @see #checkPortsInUse(java.util.List)
     */
    Map findPortsServices(List domains) {
        def ports = domains.inject(new HashSet()) { acc, DomainImpl domain -> acc << domain.port }
        log.checkingPorts this, ports
        checkPortsInUse ports
    }

    /**
     * Stops the specified service.
     *
     * @param service
     *            the service name.
     *
     * @return the {@link ExecCommandWorker}.
     */
    ExecCommandWorker stopService(String service) {
        def command = serviceStopCommand service
        def worker = execCommandFactory.create(command)()
        log.stopService this, service, worker
        return worker
    }

    /**
     * Returns the command to stop the specified service.
     *
     * <ul>
     * <li>profile property {@code "<service>_stop_command"}</li>
     * </ul>
     *
     * @param service
     *            the service name.
     *
     * @see #getDefaultProperties()
     */
    String serviceStopCommand(String service) {
        def property = "${service}_stop_command"
        def command = profileProperty property, defaultProperties
        log.checkServiceRestartCommand this, command, service
        return command
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

    /**
     * Returns the default memory limit in megabytes,
     * for example {@code "2 MB"}.
     *
     * <ul>
     * <li>profile property {@code "default_memory_limit"}</li>
     * </ul>
     */
    Measure getDefaultMemoryLimit() {
        def bytes = profileProperty "default_memory_limit", defaultProperties
        bytes = byteFormatFactory.create().parse(bytes)
        Measure.valueOf(bytes, NonSI.BYTE)
    }

    /**
     * Returns the default memory upload limit in bytes,
     * for example {@code "2 MB"}.
     *
     * <ul>
     * <li>profile property {@code "default_memory_upload"}</li>
     * </ul>
     */
    Measure getDefaultMemoryUpload() {
        def bytes = profileProperty "default_memory_upload", defaultProperties
        bytes = byteFormatFactory.create().parse(bytes)
        Measure.valueOf(bytes, NonSI.BYTE)
    }

    /**
     * Returns the default memory post limit in bytes,
     * for example {@code "2 MB"}.
     *
     * <ul>
     * <li>profile property {@code "default_memory_post"}</li>
     * </ul>
     */
    Measure getDefaultMemoryPost() {
        def bytes = profileProperty "default_memory_post", defaultProperties
        bytes = byteFormatFactory.create().parse(bytes)
        Measure.valueOf(bytes, NonSI.BYTE)
    }

    /**
     * Returns the value in megabytes.
     *
     * @param value
     *            the {@link Measure} value.
     */
    String toMegabytes(Measure value) {
        long v = value.value / UnitMultiplier.MEGA.value
        return "${v}M"
    }

    /**
     * Returns the template resource containing commands.
     */
    abstract TemplateResource getNginxCommandsTemplate()

    /**
     * Returns the profile name.
     */
    abstract String getProfileName()
}
