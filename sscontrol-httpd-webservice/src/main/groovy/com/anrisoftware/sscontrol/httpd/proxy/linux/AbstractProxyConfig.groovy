/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.proxy.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject
import javax.measure.MeasureFormat
import javax.measure.unit.SI

import org.apache.commons.io.FileUtils

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.proxy.ProxyService

/**
 * Proxy configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractProxyConfig {

    /**
     * Name of the proxy web service.
     */
    public static final String NAME = "proxy"

    @Inject
    ByteFormatFactory byteFormat

    /**
     * Parent script with the properties.
     */
    LinuxScript script

    /**
     * Deploys the domain specific proxy configuration.
     *
     * @param service
     *            the {@link ProxyService}.
     */
    void deployProxyDomainConfig(ProxyService service) {
        def file = proxyDomainConfigFile service
        def confstr = proxyConfigTemplate.getText(
                true,
                "proxyCacheConf",
                "proxy", service,
                "properties", this)
        FileUtils.write file, confstr, charset
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
     * Returns the proxy configuration file,
     * for example {@code "010-robobee_proxy.conf".}  If the path is
     * not absolute then it is assume to be under the configuration
     * include directory.
     *
     * <ul>
     * <li>profile property {@code "proxy_config_file"}</li>
     * </ul>
     *
     * @see #getConfigIncludeDir()
     */
    File getProxyConfFile() {
        profileFileProperty "proxy_config_file", script.configIncludeDir, defaultProperties
    }

    /**
     * Returns the proxy domain configuration file.
     */
    File proxyDomainConfigFile(ProxyService service) {
        new File(script.configIncludeDir, "020-robobee-${service.proxyName}-proxy.conf")
    }

    /**
     * Returns the proxy cache size,
     * for example {@code "128 MiB".}
     *
     * <ul>
     * <li>profile property {@code "proxy_cache_size"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    String getProxyCacheSize() {
        def size = profileTypedProperty "proxy_cache_size", byteFormat.create(), proxyProperties
        sizeValue size
    }

    /**
     * Returns the timeout for the connection to the upstream server,
     * for example {@code "512 MiB".}
     *
     * <ul>
     * <li>profile property {@code "proxy_cache_maximum_size"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    String getProxyCacheMaximumSize() {
        def size = profileTypedProperty "proxy_cache_maximum_size", byteFormat.create(), proxyProperties
        sizeValue size
    }

    /**
     * Returns the timeout for the connection to the upstream server,
     * for example {@code "30 s".}
     *
     * <ul>
     * <li>profile property {@code "proxy_connect_timeout"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    int getProxyConnectTimeout() {
        def str = profileProperty "proxy_connect_timeout", proxyProperties
        MeasureFormat.getInstance().parseObject(str).intValue SI.SECOND
    }

    /**
     * Returns the timeout for read timeout for the response of
     * the proxied server, for example {@code "120 s".}
     *
     * <ul>
     * <li>profile property {@code "proxy_read_timeout"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    int getProxyReadTimeout() {
        def str = profileProperty "proxy_read_timeout", proxyProperties
        MeasureFormat.getInstance().parseObject(str).intValue SI.SECOND
    }

    /**
     * Returns the timeout for the transfer of request to the upstream
     * server, for example {@code "120 s".}
     *
     * <ul>
     * <li>profile property {@code "proxy_send_timeout"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    int getProxySendTimeout() {
        def str = profileProperty "proxy_send_timeout", proxyProperties
        MeasureFormat.getInstance().parseObject(str).intValue SI.SECOND
    }

    /**
     * Returns the proxy headers to set, for
     * example {@code "X-Real-IP $remote_addr, X-Forwarded-For $proxy_add_x_forwarded_for, Host $host".}
     *
     * <ul>
     * <li>profile property {@code "proxy_set_headers"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    List getProxySetHeaders() {
        profileListProperty "proxy_set_headers", ",", proxyProperties
    }

    /**
     * Returns the proxy headers to set for SSL-domain, for
     * example {@code "X-Real-IP $remote_addr, X-Forwarded-For $proxy_add_x_forwarded_for, Host $host, X-Forwarded-Ssl on, X-Forwarded-Proto https".}
     *
     * <ul>
     * <li>profile property {@code "proxy_set_headers"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    List getProxySetSslHeaders() {
        profileListProperty "proxy_set_ssl_headers", ",", proxyProperties
    }

    /**
     * Returns the time for caching different replies, for
     * example {@code "20 min".}
     *
     * <ul>
     * <li>profile property {@code "proxy_replies_cache_time"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    long getProxyRepliesCacheTime() {
        def str = profileProperty "proxy_replies_cache_time", proxyProperties
        MeasureFormat.getInstance().parseObject(str).longValue SI.SECOND
    }

    /**
     * Returns the time for caching static files, for
     * example {@code "120 min".}
     *
     * <ul>
     * <li>profile property {@code "proxy_static_cache_time"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    long getProxyStaticCacheTime() {
        def str = profileProperty "proxy_static_cache_time", proxyProperties
        MeasureFormat.getInstance().parseObject(str).longValue SI.SECOND
    }

    /**
     * Returns the HTTP header expire time for cached static files, for
     * example {@code "10 d".}
     *
     * <ul>
     * <li>profile property {@code "proxy_expire_time"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    long getProxyExpireTime() {
        def str = profileProperty "proxy_expire_time", proxyProperties
        MeasureFormat.getInstance().parseObject(str).longValue SI.SECOND
    }

    /**
     * Returns the time for cached feeds files, for
     * example {@code "45 min".}
     *
     * <ul>
     * <li>profile property {@code "proxy_feeds_cache_time"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    long getProxyFeedsCacheTime() {
        def str = profileProperty "proxy_feeds_cache_time", proxyProperties
        MeasureFormat.getInstance().parseObject(str).longValue SI.SECOND
    }

    /**
     * Returns is proxy redirect enabled, for
     * example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "proxy_redirect"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    boolean getProxyRedirect() {
        profileBooleanProperty "proxy_redirect", proxyProperties
    }

    /**
     * Returns the size for the specified byte value.
     *
     * @param value
     *            the value in bytes.
     *
     * @return
     *            the size {@link String}.
     */
    abstract String sizeValue(long value)

    /**
     * Returns the proxy service location.
     *
     * @param service
     *            the {@link ProxyService}.
     *
     * @return the location.
     */
    String proxyLocation(ProxyService service) {
        String location = service.alias == null ? "" : service.alias
        if (!location.empty && !location.startsWith("/")) {
            location = "/$location"
        }
        return location
    }

    /**
     * Resource containing the proxy configuration templates.
     */
    abstract TemplateResource getProxyConfigTemplate()

    /**
     * Returns the default Proxy properties.
     */
    abstract ContextProperties getProxyProperties()

    /**
     * Returns the name of the proxy service.
     */
    abstract String getProxyService()

    /**
     * Returns the service name {@code "proxy".}
     */
    String getServiceName() {
        NAME
    }

    /**
     * Sets the parent script with the properties.
     *
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * Returns the parent script with the properties.
     *
     * @return the {@link LinuxScript}.
     */
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
