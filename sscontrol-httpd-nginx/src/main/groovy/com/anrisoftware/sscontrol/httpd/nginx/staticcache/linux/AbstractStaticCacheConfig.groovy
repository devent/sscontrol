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
package com.anrisoftware.sscontrol.httpd.nginx.staticcache.linux

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.FindServiceConfigWorkerFactory
import com.anrisoftware.sscontrol.httpd.nginx.nginxconfig.NginxConfigListFactory
import com.anrisoftware.sscontrol.httpd.staticservice.StaticCacheService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Static files cache configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractStaticCacheConfig {

    private LinuxScript parent

    @Inject
    private FindServiceConfigWorkerFactory findServiceConfigWorkerFactory

    @Inject
    private NginxConfigListFactory configListFactory

    /**
     * Setups the defaults properties of the service.
     *
     * @param service
     *            the {@link StaticCacheService} service.
     *
     * @see #getDefaultIndexFiles()
     * @see #getDefaultExpiresDuration()
     */
    void setupDefaults(StaticCacheService service) {
        if (service.indexFiles == null) {
            service.index files: defaultIndexFiles.join(",")
        }
        if (service.expiresDuration == null) {
            service.expires defaultExpiresDuration
        }
    }

    /**
     * Creates the domain configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the referenced {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * Creates the domain configuration and configures the service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    abstract void deployService(Domain domain, WebService service, List config)

    /**
     * Attaches the references services configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link StaticCacheService} service.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    void attachReferencedServices(Domain domain, StaticCacheService service, List config) {
        if (service.includeRefs == null) {
            return
        }
        service.includeRefs.each { String ref ->
            WebService refservice = domain.services.find { WebService it ->
                it.id == ref
            }
            if (refservice != null) {
                attachReferencedService domain, service, refservice, config
            }
        }
    }

    void attachReferencedService(Domain domain, StaticCacheService service, WebService refservice, List config) {
        def location = getLocationConfig domain, service
        def webconfig = findServiceConfigWorkerFactory.create(script).findServiceConfig profile, refservice
        def list = []
        webconfig.deployDomain domain, null, refservice, list
        String[] split = StringUtils.split list.get(0), "\n"
        int i = 0
        for (; i < split.length; i++) {
            if (split[i].startsWith("location")) {
                break
            }
        }
        if (i == split.length - 1) {
            return
        }
        split[i] = location
        def str = StringUtils.join split, "\n"
        config << str
    }

    /**
     * Returns the static files service location.
     *
     * @param service
     *            the {@link StaticCacheService}.
     *
     * @return the location.
     */
    String staticLocation(StaticCacheService service) {
        String location = service.alias == null ? "" : service.alias
        if (!location.empty && !location.startsWith("/")) {
            location = "/$location"
        }
        return location
    }

    /**
     * Returns the location configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link StaticCacheService} service.
     */
    abstract String getLocationConfig(Domain domain, StaticCacheService service)

    /**
     * Returns the default index file names, for
     * example {@code "index.$geo.html, index.htm, index.html".}
     *
     * <ul>
     * <li>profile property {@code "static_cache_default_index_files"}</li>
     * </ul>
     *
     * @see #getStaticCacheProperties()
     */
    List getDefaultIndexFiles() {
        profileListProperty "static_cache_default_index_files", staticCacheProperties
    }

    /**
     * Returns the default expires duration, for
     * example {@code "P1D".}
     *
     * <ul>
     * <li>profile property {@code "static_cache_default_expires_duration"}</li>
     * </ul>
     *
     * @see #getStaticCacheProperties()
     */
    Duration getDefaultExpiresDuration() {
        profileDurationProperty "static_cache_default_expires_duration", staticCacheProperties
    }

    /**
     * Returns the static files names, for
     * example {@code "jpg, png, gif, jpeg, svg, html, htm, css, js, mp3, wav, swf, mov, doc, pdf, xls, ppt, docx, pptx, xlsx"}
     *
     * <ul>
     * <li>profile property {@code "static_cache_files"}</li>
     * </ul>
     *
     * @see #getStaticCacheProperties()
     */
    List getStaticFiles() {
        profileListProperty "static_cache_files", staticCacheProperties
    }

    /**
     * Returns the static files cache properties.
     *
     * @return the {@link ContextProperties}.
     */
    abstract ContextProperties getStaticCacheProperties()

    /**
     * Returns the service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(LinuxScript script) {
        this.parent = script
    }

    /**
     * Returns the parent script.
     */
    LinuxScript getScript() {
        parent
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        parent.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        parent.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
