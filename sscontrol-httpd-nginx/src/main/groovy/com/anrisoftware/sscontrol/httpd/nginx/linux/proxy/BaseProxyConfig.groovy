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
package com.anrisoftware.sscontrol.httpd.nginx.linux.proxy

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject
import javax.measure.MeasureFormat
import javax.measure.unit.SI

import org.apache.commons.io.FileUtils
import org.apache.commons.math3.util.FastMath

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.globalpom.format.byteformat.UnitMultiplier
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.nginx.linux.nginx.NginxScript
import com.anrisoftware.sscontrol.httpd.nginx.linux.nginx.ProxyConfig
import com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyService

/**
 * Base proxy configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseProxyConfig implements ProxyConfig {

    /**
     * Name of the proxy web service.
     */
    public static final String NAME = "proxy"

    @Inject
    ByteFormatFactory byteFormat

    /**
     * The {@link Templates} for the proxy configuration.
     */
    Templates proxyTemplates

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource proxyConfigTemplate

    /**
     * Parent script with the properties.
     */
    NginxScript script

    /**
     * Deploys the proxy configuration to {@code proxy.conf}.
     */
    void deployProxyConfiguration() {
        def file = proxyConfFile
        def confstr = proxyConfigTemplate.getText(true, "proxyConf", "properties", this)
        FileUtils.write file, confstr, charset
    }

    /**
     * Deploys the domain specific proxy configuration.
     */
    void deployProxyDomainConfig(ProxyService service) {
        def file = proxyDomainConfigFile service
        def confstr = proxyConfigTemplate.getText(true, "proxyDomainConf", "proxy", service, "properties", this)
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
     * @see #getDefaultProperties()
     */
    File getProxyCacheDir() {
        profileProperty("proxy_cache_directory", defaultProperties) as File
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
     * @see #getDefaultProperties()
     */
    File getProxyConfFile() {
        profileFileProperty "proxy_config_file", configIncludeDir, defaultProperties
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
     * @see #getDefaultProperties()
     */
    String getProxyCacheSize() {
        def size = profileTypedProperty "proxy_cache_size", byteFormat.create(), defaultProperties
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
     * @see #getDefaultProperties()
     */
    String getProxyCacheMaximumSize() {
        def size = profileTypedProperty "proxy_cache_maximum_size", byteFormat.create(), defaultProperties
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
     * @see #getDefaultProperties()
     */
    int getProxyConnectTimeout() {
        def str = profileProperty "proxy_connect_timeout", defaultProperties
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
     * @see #getDefaultProperties()
     */
    int getProxyReadTimeout() {
        def str = profileProperty "proxy_read_timeout", defaultProperties
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
     * @see #getDefaultProperties()
     */
    int getProxySendTimeout() {
        def str = profileProperty "proxy_send_timeout", defaultProperties
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
     * @see #getDefaultProperties()
     */
    List getProxySetHeaders() {
        profileListProperty "proxy_set_headers", ",", defaultProperties
    }

    /**
     * Returns the time for caching different replies, for
     * example {@code "20 min".}
     *
     * <ul>
     * <li>profile property {@code "proxy_replies_cache_time"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    long getProxyRepliesCacheTime() {
        def str = profileProperty "proxy_replies_cache_time", defaultProperties
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
     * @see #getDefaultProperties()
     */
    long getProxyStaticCacheTime() {
        def str = profileProperty "proxy_static_cache_time", defaultProperties
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
     * @see #getDefaultProperties()
     */
    long getProxyExpireTime() {
        def str = profileProperty "proxy_expire_time", defaultProperties
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
     * @see #getDefaultProperties()
     */
    long getProxyFeedsCacheTime() {
        def str = profileProperty "proxy_feeds_cache_time", defaultProperties
        MeasureFormat.getInstance().parseObject(str).longValue SI.SECOND
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
    String sizeValue(long value) {
        String u = ""
        if (value > UnitMultiplier.MEBI.value) {
            value = FastMath.round(value/UnitMultiplier.MEBI.value)
            u = "m"
        }
        if (value > UnitMultiplier.KIBI.value) {
            value = FastMath.round(value/UnitMultiplier.KIBI.value)
            u = "k"
        }
        "$value$u"
    }

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
        if (!location.startsWith("/")) {
            location = "/$location"
        }
        return location
    }

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
     *            the {@link NginxScript}.
     */
    void setScript(NginxScript script) {
        this.script = script
        this.proxyTemplates = templatesFactory.create "BaseProxyConfig"
        this.proxyConfigTemplate = proxyTemplates.getResource "config"
    }

    /**
     * Returns the parent script with the properties.
     *
     * @return the {@link NginxScript}.
     */
    NginxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}