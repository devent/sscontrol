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
package com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.ScmInstall
import com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Redmine</i> configuration for <i>Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_12_04_Config extends Redmine_2_5_Config implements ServiceConfig {

    @Inject
    RedminePropertiesProvider propertiesProvider

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    UbuntuRedmineFromArchive redmineFromArchive

    @Inject
    UbuntuThinConfig thinConfig

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        redmineFromArchive.deployDomain domain, refDomain, service, config
        thinConfig.deployDomain domain, refDomain, service, config
        super.deployDomain domain, refDomain, service, config
        setupThinPermissions domain, service
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        thinConfig.thinScriptFile.delete()
        installPackages()
        installScmPackages service
        redmineFromArchive.deployService domain, service, config
        thinConfig.deployService domain, service, config
        super.deployService domain, service, config
        setupThinPermissions domain, service
    }

    /**
     * Installs the <i>Redmine</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: redminePackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Installs the <i>Scm</i> packages.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     */
    void installScmPackages(RedmineService service) {
        if (!service.scmInstall) {
            service.scmInstall = defaultScmInstall
        }
        def list = []
        service.scmInstall.each {
            switch (it) {
                case ScmInstall.all:
                    list.addAll scmPackages(ScmInstall.subversion)
                    list.addAll scmPackages(ScmInstall.darcs)
                    list.addAll scmPackages(ScmInstall.mercurial)
                    list.addAll scmPackages(ScmInstall.cvs)
                    list.addAll scmPackages(ScmInstall.bazaar)
                    list.addAll scmPackages(ScmInstall.git)
                    break
                default:
                    list.addAll scmPackages(it)
                    break
            }
        }
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: list,
                system: systemName,
                this, threads)()
    }

    /**
     * Setup <i>Thin</i> permissions for <i>Redmine</i>.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     */
    void setupThinPermissions(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        def filesDir = new File(dir, 'files')
        def logDir = new File(dir, 'log')
        def tmpDir = new File(dir, 'tmp')
        def tmpPdfDir = new File(dir, 'tmp/pdf')
        def pluginAssetsDir = new File(dir, 'public/plugin_assets')
        changeFileOwnerFactory.create(
                log: log,
                command: chownCommand,
                files: [
                    filesDir,
                    logDir,
                    tmpDir,
                    tmpPdfDir,
                    pluginAssetsDir
                ],
                owner: thinConfig.thinUser,
                ownerGroup: domain.domainUser.group,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                files: [
                    filesDir,
                    logDir,
                    tmpDir,
                    tmpPdfDir,
                    pluginAssetsDir
                ],
                mod: "755",
                recursive: true,
                this, threads)()
    }

    @Override
    ContextProperties getRedmineProperties() {
        propertiesProvider.get()
    }

    @Override
    String getProfile() {
        RedmineConfigFactory.PROFILE_NAME
    }

    @Override
    public void setScript(LinuxScript script) {
        super.setScript(script)
        redmineFromArchive.setScript this
        thinConfig.setScript this
    }
}
