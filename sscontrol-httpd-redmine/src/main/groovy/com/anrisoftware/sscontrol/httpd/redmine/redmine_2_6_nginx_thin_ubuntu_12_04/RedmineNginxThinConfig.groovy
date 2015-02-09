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
package com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu.UbuntuNginxThinConfig
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.mklink.MkLinkFactory

/**
 * <i>Ubuntu 12.04 Nginx Thin Redmine 2.6</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RedmineNginxThinConfig extends UbuntuNginxThinConfig implements ServiceConfig {

    @Inject
    NginxConfigLogger logg

    @Inject
    RedminePropertiesProvider redminePropertiesProvider

    @Inject
    MkLinkFactory mkLinkFactory

    @Inject
    Ubuntu_12_04_Redmine_DatabaseConfig databaseConfig

    @Inject
    Ubuntu_12_04_Redmine_ConfigurationConfig configurationConfig

    @Inject
    Ubuntu_12_04_Thin_Redmine_Config thinConfig

    @Inject
    Ubuntu_12_04_RedmineFromArchiveConfig fromArchiveConfig

    @Inject
    Ubuntu_12_04_FixRedmineGemfileConfig fixRedmineGemfileConfig

    @Inject
    Ubuntu_12_04_RedmineBackup backupConfig

    TemplateResource domainConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        thinConfig.deployDomain domain, refDomain, service, config
        createDomainConfig domain, refDomain, service, config
        createDomainUpstreamConfig domain, refDomain, service
        enableDomainUpstreamConfig domain, refDomain, service
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        stopServices()
        thinConfig.thinScriptFile.delete()
        installPackages()
        installScmPackages service
        backupConfig.backupService domain, service
        fromArchiveConfig.deployService domain, service
        installGems domain, service
        databaseConfig.deployDatabase domain, service
        configurationConfig.deployEmail domain, service
        fixRedmineGemfileConfig.deployGemsFix domain, service
        deployEnvironmentConfig domain, service
        setupPermissions domain, service
        installBundle domain, service
        generateSecretTokens domain, service
        migrateDb domain, service
        loadDefaultData domain, service
        clearTemps domain, service
        thinConfig.deployService domain, service, config
        setupThinPermissions domain, service
        createDomainConfig domain, null, service, config
        createDomainUpstreamConfig domain, null, service
        enableDomainUpstreamConfig domain, null, service
        restartServices()
    }

    /**
     * Stops services.
     */
    void stopServices() {
        if (new File(thinConfig.thinStopCommand).exists()) {
            thinConfig.stopServices()
        }
    }

    /**
     * Restarts services.
     */
    void restartServices() {
        thinConfig.restartServices()
    }

    /**
     * Creates the domain configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param refDomain
     *            the referenced {@link Domain}.
     *
     * @param service
     *            the <i>Redmine</i> {@link RedmineService} service.
     *
     * @param config
     *            the {@link List} configuration.
     */
    void createDomainConfig(Domain domain, Domain refDomain, RedmineService service, List config) {
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDir = serviceDir domain, refDomain, service
        def aliasTarget = service.alias.isEmpty() ? null : service.alias;
        def configStr = domainConfigTemplate.getText(
                true, "domainConfig", "args", [
                    domain: domain,
                    prefix: service.prefix,
                    alias: serviceAliasDir,
                    domainName: domainNameAsFileName(domain),
                    aliasTarget: aliasTarget,
                    redminePublicDirectory: redminePublicDir(domain, service),
                ])
        config << configStr
    }

    /**
     * Creates the domain upstream cluster configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param refDomain
     *            the referenced {@link Domain}.
     *
     * @param service
     *            the <i>Redmine</i> {@link RedmineService} service.
     */
    void createDomainUpstreamConfig(Domain domain, Domain refDomain, RedmineService service) {
        def file = domainUpstreamConfigFile domain, service
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDir = serviceDir domain, refDomain, service
        def sockets = []
        def socketFile = thinConfig.domainSocketFile domain, service
        def socketFileName = FilenameUtils.getBaseName(socketFile.name)
        (0..<thinConfig.serversCount).each { sockets << "${socketFile.parent}/${socketFileName}.${it}.sock" }
        def configStr = domainConfigTemplate.getText(
                true, "upstreamConfig", "args", [
                    domain: domain,
                    prefix: service.prefix,
                    sockets: sockets,
                    domainName: domainNameAsFileName(domain),
                ])
        FileUtils.write file, configStr
        logg.deployDomainUpstreamConfig this, domain, file
    }

    /**
     * Enables the domain upstream cluster configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param refDomain
     *            the referenced {@link Domain}.
     *
     * @param service
     *            the <i>Redmine</i> {@link RedmineService} service.
     */
    void enableDomainUpstreamConfig(Domain domain, Domain refDomain, RedmineService service) {
        def file = domainUpstreamConfigFile domain, service
        def src = new File(sitesAvailableDir, file.name)
        def target = new File(sitesEnabledDir, file.name)
        mkLinkFactory.create(
                log: log,
                runCommands: runCommands,
                command: linkCommand,
                files: src,
                targets: target,
                override: true,
                this, threads)()
        logg.enableDomainUpstreamConfig this, domain, file
    }

    /**
     * Returns the domain upstream configuration file inside the
     * domain directory, for example
     * {@code /etc/nginx/sites-available/100-robobee-domain.com_redmine2-upstream.conf}
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    File domainUpstreamConfigFile(Domain domain, RedmineService service) {
        def file = new ST(upstreamDomainFile).
                add("domainName", domain.name).
                add("servicePrefix", service.prefix).render()
        new File(sitesAvailableDir, file)
    }

    /**
     * Returns the upstream domain configuration file, for
     * example {@code "100-robobee-<domainName>_<servicePrefix>-upstream.conf".}
     *
     * <ul>
     * <li>profile property {@code "redmine_upstream_domain_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getUpstreamDomainFile() {
        profileProperty "redmine_upstream_domain_file", redmineProperties
    }

    @Inject
    final void setNginxTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Redmine_Nginx_Thin_Ubuntu_12_04"
        this.domainConfigTemplate = templates.getResource "domain_config"
    }

    @Override
    ContextProperties getRedmineProperties() {
        redminePropertiesProvider.get()
    }

    String getProfile() {
        RedmineConfigFactory.PROFILE_NAME
    }

    void setScript(LinuxScript script) {
        super.setScript script
        thinConfig.setScript this
        fromArchiveConfig.setScript this
        databaseConfig.setScript this
        configurationConfig.setScript this
        fixRedmineGemfileConfig.setScript this
        backupConfig.setScript this
    }
}
