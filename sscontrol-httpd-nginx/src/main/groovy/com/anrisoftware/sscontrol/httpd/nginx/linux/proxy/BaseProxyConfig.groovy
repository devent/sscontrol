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

import org.apache.commons.math3.util.FastMath

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.globalpom.format.byteformat.UnitMultiplier
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.nginx.linux.nginx.NginxScript
import com.anrisoftware.sscontrol.httpd.nginx.linux.nginx.ProxyConfig
import com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

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

    def proxyCachePathConfig(ProxyService service) {
        def search = proxyConfigTemplate.getText(true, "proxyCachePath_search")
        def replace = proxyConfigTemplate.getText(true,
                "proxyCachePath", "dir", proxyCacheDir,
                "name", proxyCacheName(service),
                "size", proxyCacheSize,
                "maxsize", proxyCacheMaximumSize)
        new TokenTemplate(search, replace)
    }

    def proxyCacheTempPathConfig(ProxyService service) {
        def search = proxyConfigTemplate.getText(true, "proxyCacheTempPath_search")
        def replace = proxyConfigTemplate.getText(true, "proxyCacheTempPath", "dir", proxyCacheDir)
        new TokenTemplate(search, replace)
    }

    def proxyConnectTimeoutConfig(ProxyService service) {
        def search = proxyConfigTemplate.getText(true, "proxyConnectTimeout_search")
        def replace = proxyConfigTemplate.getText(true, "proxyConnectTimeout", "time", proxyConnectTimeout)
        new TokenTemplate(search, replace)
    }

    def proxyReadTimeoutConfig(ProxyService service) {
        def search = proxyConfigTemplate.getText(true, "proxyReadTimeout_search")
        def replace = proxyConfigTemplate.getText(true, "proxyReadTimeout", "time", proxyReadTimeout)
        new TokenTemplate(search, replace)
    }

    def proxySendTimeoutConfig(ProxyService service) {
        def search = proxyConfigTemplate.getText(true, "proxySendTimeout_search")
        def replace = proxyConfigTemplate.getText(true, "proxySendTimeout", "time", proxySendTimeout)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the proxy cache directory,
     * for example {@code "/var/lib/nginx/cache".}
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
     * Returns the proxy cache name.
     *
     * @param service
     *            the {@link ProxyService}.
     */
    abstract String proxyCacheName(ProxyService service)

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
