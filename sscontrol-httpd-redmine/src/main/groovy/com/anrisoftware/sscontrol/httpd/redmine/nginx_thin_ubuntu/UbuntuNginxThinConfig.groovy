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
package com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.ScmInstall
import com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_Config
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Ubuntu Nginx Thin Redmine 2.6</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class UbuntuNginxThinConfig extends Redmine_2_5_Config {

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

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
        def list = []
        service.scms.each {
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

    /**
     * Returns the packages for the specified <i>Scm</i>.
     *
     * <ul>
     * <li>profile property {@code "redmine_scm_<scm-name>_packages"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    List scmPackages(ScmInstall scm) {
        profileListProperty "redmine_scm_${scm.name()}_packages", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> packages, for
     * example {@code "ruby, rake, rubygems, libopenssl-ruby, libmysql-ruby, ruby-dev, libmysqlclient-dev, libmagick-dev, curl, libmagickwand-dev".}
     *
     * <ul>
     * <li>profile property {@code "redmine_packages"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    List getRedminePackages() {
        profileListProperty "redmine_packages", redmineProperties
    }
}
