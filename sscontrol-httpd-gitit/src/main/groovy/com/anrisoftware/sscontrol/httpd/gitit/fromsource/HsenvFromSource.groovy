/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit.fromsource

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Installs and configures <i>Gitit</i> with the help of <i>hsenv</i> from source.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class HsenvFromSource {

    @Inject
    private HsenvFromSourceLogger log

    Object script

    Templates hsenvTemplates

    TemplateResource hsenvCommandTemplate

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List config) {
        unpackGititArchive domain, service
        installHsenv()
        installHsenvCabalPackages domain, service
        installGitit domain, service
    }

    /**
     * Downloads and unpacks the <i>Gitit</i> source archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @see #getGititArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackGititArchive(Domain domain, GititService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(gititArchive.path).name
        def dest = new File(tmpDirectory, "gitit-0.10.3.1-$name")
        copyURLToFile gititArchive.toURL(), dest
        unpackArchive domain, service, dest
    }

    /**
     * Returns if it needed to download and unpack the <i>Gitit</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see WordpressService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, GititService service) {
        service.overrideMode != OverrideMode.no || !gititCommandExist(domain, service)
    }

    /**
     * Unpacks the <i>Gitit</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, WebService service, File archive) {
        def dir = gititSourceDir domain, service
        dir.isDirectory() ? false : dir.mkdirs()
        unpack file: archive, type: archiveType(file: archive), output: dir, override: true, strip: true
        log.unpackArchiveDone this, gititArchive
    }

    /**
     * Installs <i>hsenv</i>.
     */
    void installHsenv() {
        installCabalPackages packages: hsenvCabalPackages
        log.installHsenvDone this, hsenvCabalPackages
    }

    /**
     * Installs the <i>cabal</i> packages inside the <i>hsenv</i> environment.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     */
    void installHsenvCabalPackages(Domain domain, GititService service) {
        def args = [:]
        args.gititDir = gititDir domain, service
        args.hsenvCommand = hsenvCommand
        args.cabalCommand = hsenvCabalCommand domain, service
        args.activateCommand = hsenvActivateCommand domain, service
        args.packages = hsenvGititPackages
        def worker = scriptCommandFactory.create(hsenvCommandTemplate, "hsenvInstallCommand", "args", args)()
        log.installHsenvCabalPackagesDone this, worker, args
    }

    /**
     * Installs <i>gitit</i> inside the <i>hsenv</i> environment.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     */
    void installGitit(Domain domain, GititService service) {
        def args = [:]
        args.gititDir = gititDir domain, service
        args.hsenvCommand = hsenvCommand
        args.cabalCommand = hsenvCabalCommand domain, service
        args.activateCommand = hsenvActivateCommand domain, service
        args.gititSourceDir = gititSourceDir domain, service
        def worker = scriptCommandFactory.create(hsenvCommandTemplate, "hsenvCompileCommand", "args", args)()
        log.installGititDone this, worker, args
    }

    /**
     * Returns the <i>Gitit</i> archive.
     *
     * <ul>
     * <li>profile property {@code "hsenv_gitit_archive"}</li>
     * </ul>
     *
     * @see #getHsenvProperties()
     */
    URI getGititArchive() {
        profileURIProperty "hsenv_gitit_archive", hsenvProperties
    }

    /**
     * Returns the <i>cabal</i> packages to install <i>hsenv</i>, for example
     * {@code "hsenv".}
     *
     * <ul>
     * <li>profile property {@code hsenv_cabal_packages}</li>
     * </ul>
     *
     * @return the {@link List} of packages.
     *
     * @see #getHsenvProperties()
     */
    List getHsenvCabalPackages() {
        profileListProperty "hsenv_cabal_packages", hsenvProperties
    }

    /**
     * Returns the <i>cabal</i> packages to install <i>Gitit</i> inside
     * the <i>hsenv</i> environment, for
     * example {@code "pandoc -fhighlighting".}
     *
     * <ul>
     * <li>profile property {@code hsenv_gitit_packages}</li>
     * </ul>
     *
     * @return the {@link List} of packages.
     *
     * @see #getHsenvProperties()
     */
    List getHsenvGititPackages() {
        profileListProperty "hsenv_gitit_packages", hsenvProperties
    }

    /**
     * Returns the <i>hsenv</i> command, for
     * example {@code "/root/.cabal/bin/hsenv".}
     *
     * <ul>
     * <li>profile property {@code "hsenv_command"}</li>
     * </ul>
     *
     * @see #getHsenvProperties()
     */
    String getHsenvCommand() {
        profileProperty "hsenv_command", hsenvProperties
    }

    /**
     * Returns the <i>deactivate</i> command, for
     * example {@code "deactivate_hsenv".}
     *
     * <ul>
     * <li>profile property {@code "hsenv_deactivate_command"}</li>
     * </ul>
     *
     * @see #getHsenvProperties()
     */
    String getHsenvDeactivateCommand() {
        profileProperty "hsenv_deactivate_command", hsenvProperties
    }

    /**
     * Returns the <i>cabal</i> command inside the <i>hsenv</i>
     * environment, for example {@code "<gititDir>/.hsenv/bin/cabal".}
     * The placeholder {@code "<gititDir>"} is replaced by the <i>Gitit</i>
     * service domain directory.
     *
     * <ul>
     * <li>profile property {@code "hsenv_cabal_command"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @see #getHsenvProperties()
     */
    String hsenvCabalCommand(Domain domain, GititService service) {
        def dir = gititDir domain, service
        def p = profileProperty "hsenv_cabal_command", hsenvProperties
        new ST(p).add("gititDir", dir).render()
    }

    /**
     * Returns the <i>activate</i> command inside the <i>hsenv</i>
     * environment, for example {@code "<gititDir>/.hsenv/bin/activate".}
     * The placeholder {@code "<gititDir>"} is replaced by the <i>Gitit</i>
     * service domain directory.
     *
     * <ul>
     * <li>profile property {@code "hsenv_activate_command"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @see #getHsenvProperties()
     */
    String hsenvActivateCommand(Domain domain, GititService service) {
        def dir = gititDir domain, service
        def p = profileProperty "hsenv_activate_command", hsenvProperties
        new ST(p).add("gititDir", dir).render()
    }

    /**
     * Returns the <i>gitit</i> command inside the <i>hsenv</i>
     * environment, for example {@code "<gititDir>/.hsenv/cabal/bin/gitit".}
     * The placeholder {@code "<gititDir>"} is replaced by the <i>Gitit</i>
     * service domain directory.
     *
     * <ul>
     * <li>profile property {@code "hsenv_gitit_command"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @see #getHsenvProperties()
     */
    String hsenvGititCommand(Domain domain, GititService service) {
        def dir = gititDir domain, service
        def p = profileProperty "hsenv_gitit_command", hsenvProperties
        new ST(p).add("gititDir", dir).render()
    }

    /**
     * Returns if the <i>gitit</i> command exist, i.e. the <i>Gitit</i>
     * web service is already installed.
     *
     * @return {@code true} if the command exist.
     *
     * @see #hsenvGititCommand(Domain, GititService)
     */
    boolean gititCommandExist(Domain domain, GititService service) {
        new File(hsenvGititCommand(domain, service)).isFile()
    }

    /**
     * Returns the <i>Gitit</i> source directory inside the <i>hsenv</i>
     * environment, for example {@code "<gititDir>/gitit-0.10.3.1".}
     * The placeholder {@code "<gititDir>"} is replaced by the <i>Gitit</i>
     * service domain directory.
     *
     * <ul>
     * <li>profile property {@code "hsenv_gitit_source_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link GititService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #gititDir(Domain, GititService)
     * @see #getGititArchive()
     */
    File gititSourceDir(Domain domain, GititService service) {
        def dir = gititDir domain, service
        def p = profileProperty "hsenv_gitit_source_directory", hsenvProperties
        new File(new ST(p).add("gititDir", dir).render())
    }

    /**
     * Returns the default <i>hsenv</i> <i>Gitit</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getHsenvProperties()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
        hsenvTemplates = templatesFactory.create "HsenvFromSource"
        hsenvCommandTemplate = hsenvTemplates.getResource "hsenvcommands"
    }

    /**
     * @see ServiceConfig#getName()
     */
    String getName() {
        script.getName()
    }

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
    String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
