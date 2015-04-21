/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.measure.Measure
import javax.measure.MeasureFormat
import javax.measure.unit.NonSI

import com.anrisoftware.globalpom.exec.api.ProcessTask
import com.anrisoftware.globalpom.exec.runcommands.RunCommands
import com.anrisoftware.globalpom.exec.runcommands.RunCommandsFactory
import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.globalpom.format.byteformat.UnitMultiplier
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.bindings.BindingFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.domain.DomainImpl
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.scripts.findusedport.FindUsedPortFactory
import com.anrisoftware.sscontrol.scripts.mklink.MkLinkFactory
import com.anrisoftware.sscontrol.scripts.unix.StopServicesFactory

/**
 * Uses Nginx service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class NginxScript extends LinuxScript {

    private static final String NGINX_NAME = "nginx"

    @Inject
    private NginxScriptLogger logg

    @Inject
    private BindingFactory bindingFactory

    @Inject
    ByteFormatFactory byteFormatFactory

    @Inject
    FindUsedPortFactory findUsedPortFactory

    @Inject
    MkLinkFactory mkLinkFactory

    @Inject
    StopServicesFactory stopServicesFactory

    TemplateResource stopServiceTemplate

    RunCommands runCommands

    @Override
    def run() {
        setupDefaultBinding service
        setupDefaultLogging service
    }

    /**
     * Setups the default binding addresses.
     *
     * @param service
     *            the {@link HttpdService} httpd service.
     */
    void setupDefaultBinding(HttpdService service) {
        if (service.bindingAddresses == null) {
            service.bind defaultBindingAddress, ports: defaultBindingPorts
        }
    }

    /**
     * Setups the default debug logging.
     *
     * @param service
     *            the {@link HttpdService} httpd service.
     */
    void setupDefaultLogging(HttpdService service) {
        if (!service.debugLogging("error") || !service.debugLogging("error")["level"]) {
            service.debug "error", level: defaultDebugErrorLevel
        }
        if (!service.debugLogging("error") || !service.debugLogging("error")["storage"]) {
            service.debug "error", storage: defaultDebugErrorStorage
        }
    }

    @Inject
    final void setNginxScriptTemplates(TemplatesFactory factory) {
        def templates = factory.create "NginxScript"
        this.stopServiceTemplate = templates.getResource "stop_service"
    }

    @Inject
    final void setRunCommands(RunCommandsFactory factory) {
        this.runCommands = factory.create this, NginxScript.NGINX_NAME
    }

    @Override
    HttpdService getService() {
        super.getService();
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
     * for example {@code "nginx.conf".} If the path is
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
     * <li>profile property {@code "error_pages_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getErrorPagesDir() {
        profileDirProperty "error_pages_directory", defaultProperties
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
     * Returns the proxy cache directory,
     * for example {@code "/var/cache/nginx".}
     *
     * <ul>
     * <li>profile property {@code "proxy_cache_directory"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    File getProxyCacheDir() {
        profileDirProperty "proxy_cache_directory", defaultProperties
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
     * Returns the <i>Nginx</i> local user name,
     * for example {@code "www-data".}
     *
     * <ul>
     * <li>profile property {@code "nginx_user"}</li>
     * </ul>
     */
    String getNginxUser() {
        profileProperty "nginx_user", defaultProperties
    }

    /**
     * Returns the <i>Nginx</i> local group name,
     * for example {@code "www-data".}
     *
     * <ul>
     * <li>profile property {@code "nginx_group"}</li>
     * </ul>
     */
    String getNginxGroup() {
        profileProperty "nginx_group", defaultProperties
    }

    /**
     * Returns the default bind ports, for example {@code "80, 443"}.
     *
     * <ul>
     * <li>profile property {@code "default_binding_ports"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultBindingPorts() {
        profileListProperty "default_binding_ports", defaultProperties
    }

    /**
     * Returns the default bind address, for example {@code "0.0.0.0"}.
     *
     * <ul>
     * <li>profile property {@code "default_binding_address"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultBindingAddress() {
        profileProperty "default_binding_address", defaultProperties
    }

    /**
     * Returns the default debug error logging level, for example {@code 3}.
     *
     * <ul>
     * <li>profile property {@code "default_debug_error_level"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Integer getDefaultDebugErrorLevel() {
        profileNumberProperty "default_debug_error_level", defaultProperties
    }

    /**
     * Returns the default debug error logging storage, for
     * example {@code "/var/log/nginx/error.log".}
     *
     * <ul>
     * <li>profile property {@code "default_debug_error_storage"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultDebugErrorStorage() {
        profileProperty "default_debug_error_storage", defaultProperties
    }

    /**
     * Returns the number of <i>Nginx</i> worker processes, for
     * example {@code "2".}
     *
     * <ul>
     * <li>profile property {@code "worker_processes"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getWorkerProcesses() {
        profileNumberProperty "worker_processes", defaultProperties
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
     * Returns the server names hash bucked size, for
     * example {@code 128}.
     *
     * <ul>
     * <li>profile property {@code "server_names_hash_bucket_size"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getServerNamesHashBucketSize() {
        profileNumberProperty "server_names_hash_bucket_size", defaultProperties
    }

    /**
     * Returns the server names hash max size, for
     * example {@code 128}.
     *
     * <ul>
     * <li>profile property {@code "server_names_hash_max_size"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getServerNamesHashMaxSize() {
        profileNumberProperty "server_names_hash_max_size", defaultProperties
    }

    /**
     * Returns the proxy headers hash max size, for
     * example {@code 4096}.
     *
     * <ul>
     * <li>profile property {@code "proxy_headers_hash_max_size"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getProxyHeadersHashMaxSize() {
        profileNumberProperty "proxy_headers_hash_max_size", defaultProperties
    }

    /**
     * Returns the proxy headers hash bucket max size, for
     * example {@code 128}.
     *
     * <ul>
     * <li>profile property {@code "proxy_headers_hash_bucket_size"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getProxyHeadersHashBucketSize() {
        profileNumberProperty "proxy_headers_hash_bucket_size", defaultProperties
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
        profileProperty "group_pattern", defaultProperties
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
        def process = mkLinkFactory.create(
                log: log,
                runCommands: runCommands,
                command: linkCommand,
                files: files,
                targets: targets,
                override: true,
                this, threads)()
        logg.enabledSites this, sites
    }

    /**
     * Find the services that use the ports of the specified domains.
     *
     * @param domains
     *            the list of {@link Domain} domains.
     *
     * @return the {@link Map} of services: {@code port:=service}.
     */
    Map findPortsServices(List domains) {
        def ports = domains.inject(new HashSet()) { acc, DomainImpl domain -> acc << domain.port }
        logg.checkingPorts this, ports
        findUsedPortFactory.create(
                log: log,
                runCommands: runCommands,
                command: netstatCommand,
                ports: ports,
                this, threads)().services
    }

    /**
     * Stops the specified service.
     *
     * @param service
     *            the service name.
     *
     * @return the {@link ProcessTask}.
     */
    ProcessTask stopService(String service) {
        stopServicesFactory.create(
                log: log,
                runCommands: runCommands,
                command: serviceStopCommand(service),
                services: serviceStopServices(service),
                flags: serviceStopFlags(service),
                this, threads)()
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
        if (domain.errorPage == null) {
            domain.error page: defaultErrorPage
        }
        if (domain.errorRoot == null) {
            domain.error root: defaultErrorRoot
        }
        if (domain.errorCodes == null) {
            domain.error codes: defaultErrorCodes.join(",")
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
     * Returns the default error page,
     * for example {@code "/50x.html"}.
     *
     * <ul>
     * <li>profile property {@code "default_error_page_uri"}</li>
     * </ul>
     */
    String getDefaultErrorPage() {
        profileProperty "default_error_page_uri", defaultProperties
    }

    /**
     * Returns the default error root,
     * for example {@code "/usr/share/nginx/html"}.
     *
     * <ul>
     * <li>profile property {@code "default_error_page_root"}</li>
     * </ul>
     */
    String getDefaultErrorRoot() {
        profileProperty "default_error_page_root", defaultProperties
    }

    /**
     * Returns the default error codes,
     * for example {@code "500, 502, 503, 504"}.
     *
     * <ul>
     * <li>profile property {@code "default_error_page_codes"}</li>
     * </ul>
     */
    List getDefaultErrorCodes() {
        profileListProperty "default_error_page_codes", defaultProperties
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
     * Returns the profile name.
     */
    abstract String getProfileName()
}
