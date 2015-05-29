/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.fromarchive

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService

/**
 * Installs <i>FUDForum</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class FudforumArchiveInstallConfig {

    @Inject
    private FudforumArchiveInstallConfigLogger log

    private Object script

    @Inject
    ScriptExecFactory scriptExecFactory

    /**
     * Installs the <i>FUDForum</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void installService(Domain domain, FudforumService service) {
        def task = scriptExecFactory.create(
                log: log.log,
                runCommands: runCommands,
                phpCommand: phpCommand,
                workDirectory: installScript(domain, service).parentFile,
                installScript: installScriptFile.name,
                this, threads, installTemplate, "installScript")()
        log.serviceInstalled this, domain, task
    }

    /**
     * Upgrades the <i>FUDForum</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void upgradeService(Domain domain, FudforumService service) {
        def task = scriptExecFactory.create(
                log: log.log,
                runCommands: runCommands,
                phpCommand: phpCommand,
                workDirectory: upgradeScript(domain, service).parentFile,
                upgradeScript: upgradeScriptFile.name,
                this, threads, installTemplate, "upgradeScript")()
        log.serviceUpgraded this, domain, task
    }

    /**
     * Returns the <i>install.php</i> script path.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @see #getInstallScriptFile()
     */
    File installScript(Domain domain, FudforumService service) {
        def file = installScriptFile
        if (file.absolute) {
            return file
        } else {
            new File(fudforumDir(domain, service), file.name)
        }
    }

    /**
     * Returns the <i>upgrade.php</i> script path.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @see #getUpgradeScriptFile()
     */
    File upgradeScript(Domain domain, FudforumService service) {
        def file = upgradeScriptFile
        if (file.absolute) {
            return file
        } else {
            new File(fudforumDir(domain, service), file.name)
        }
    }

    /**
     * Returns the <i>php</i> command.
     *
     * <ul>
     * <li>profile property {@code "fudforum_php_command"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getPhpCommand() {
        profileProperty "fudforum_php_command", fudforumProperties
    }

    /**
     * Returns the <i>install.php</i> script. If the file is not absolute,
     * then the file is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "fudforum_install_script"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    File getInstallScriptFile() {
        profileDirProperty "fudforum_install_script", fudforumProperties
    }

    /**
     * Returns the <i>upgrade.php</i> script. If the file is not absolute,
     * then the file is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "fudforum_upgrade_script"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    File getUpgradeScriptFile() {
        profileDirProperty "fudforum_upgrade_script", fudforumProperties
    }

    /**
     * Returns the <i>FUDForum</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFudforumProperties()

    /**
     * Returns the install forum script template.
     *
     * @return the {@link TemplateResource} resource.
     */
    abstract TemplateResource getInstallTemplate()

    /**
     * Returns the <i>FUDForum</i> service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        script.getProfile()
    }

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        script
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
