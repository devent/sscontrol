/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.core

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.source.gitolite.GitoliteService

/**
 * <i>Gitolite 3</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Gitolite_3_Config {

    private LinuxScript script

    private TemplateResource gitoliteCommandsTemplate

    @Inject
    private Gitolite_3_ConfigLogger log

    @Inject
    private ScriptExecFactory scriptExecFactory

    @Inject
    final void setTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Gitolite_3_Config"
        this.gitoliteCommandsTemplate = templates.getResource "gitolite_commands"
    }

    /**
     * Setups the default properties of the service.
     *
     * @param service
     *            the {@link GitoliteService} service.
     */
    void setupDefaults(GitoliteService service) {
        setupDefaultPrefix service
        setupDefaultData service
        setupDefaultOverrideMode service
        setupDefaultUser service
    }

    /**
     * Setups the default service prefix.
     *
     * @param service
     *            the {@link GitoliteService} service.
     */
    void setupDefaultPrefix(GitoliteService service) {
        if (service.prefix == null) {
            service.prefix path: defaultPrefix
        }
    }

    /**
     * Setups the default service data path.
     *
     * @param service
     *            the {@link GitoliteService} service.
     */
    void setupDefaultData(GitoliteService service) {
        if (service.dataPath == null) {
            service.data path: defaultData
        }
    }

    /**
     * Setups the default override mode.
     *
     * @param service
     *            the {@link GitoliteService} service.
     */
    void setupDefaultOverrideMode(GitoliteService service) {
        if (service.overrideMode == null) {
            service.override mode: defaultOverrideMode
        }
    }

    /**
     * Setups the default service local user.
     *
     * @param service
     *            the {@link GitoliteService} service.
     */
    void setupDefaultUser(GitoliteService service) {
        if (service.user == null) {
            service.user defaultUser, group: defaultGroup
        }
        if (service.user.user == null) {
            service.user defaultUser
        }
        if (service.user.group == null) {
            service.user group: defaultGroup
        }
    }

    /**
     * Installs the service.
     *
     * @param service
     *            the {@link GitoliteService}.
     */
    void installGitolite(GitoliteService service) {
        def path = gitoliteCommand(service).parentFile.canonicalFile
        path.mkdirs()
        def task = scriptExecFactory.create(
                log: log.log,
                installCommand: gitoliteInstallCommand(service),
                prefix: path,
                this, threads, gitoliteCommandsTemplate, "installGitolite")()
        log.installedGitolite this, task, service.prefix
    }

    /**
     * Deploys the administrator public key.
     *
     * @param service
     *            the {@link GitoliteService}.
     */
    void deployAdminKey(GitoliteService service) {
        def key = copyAdminKey service
        def task = scriptExecFactory.create(
                log: log.log,
                suCommand: suCommand,
                gitoliteCommand: gitoliteCommand(service),
                gitoliteUser: service.user.user,
                key: key,
                this, threads, gitoliteCommandsTemplate, "installAdminKey")()
        key.delete()
        log.installedAdminKey this, task, service.adminKey
    }

    File copyAdminKey(GitoliteService service) {
        def name = FilenameUtils.getBaseName(service.adminKey.path)
        def target = new File(tmpDirectory, "${name}.pub")
        FileUtils.copyURLToFile service.adminKey.toURL(), target
        target
    }

    /**
     * Upgrades the service after update.
     *
     * @param service
     *            the {@link GitoliteService}.
     */
    void upgradeGitolite(GitoliteService service) {
        def key = copyAdminKey service
        def task = scriptExecFactory.create(
                log: log.log,
                suCommand: suCommand,
                gitoliteCommand: gitoliteCommand(service),
                gitoliteUser: service.user.user,
                this, threads, gitoliteCommandsTemplate, "upgradeGitolite")()
        log.upgradedGitolite this, task, service.prefix
    }

    /**
     * Returns the path of the <i>Gitolite</i> install command, for
     * example {@code "install"}. If the path is not absolute it
     * is assumed to be located under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "gitolite_install_command"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    File gitoliteInstallCommand(GitoliteService service) {
        profileFileProperty "gitolite_install_command", new File(service.prefix), gitoliteProperties
    }

    /**
     * Returns the path of the <i>gitolite</i> command, for
     * example {@code "../bin/gitolite"}. If the path is not absolute it
     * is assumed to be located under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "gitolite_command"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    File gitoliteCommand(GitoliteService service) {
        profileFileProperty "gitolite_command", new File(service.prefix), gitoliteProperties
    }

    /**
     * Returns the default service prefix path, for
     * example {@code "/usr/local/gitolite"}. That is the path where the
     * service is installed to.
     *
     * <ul>
     * <li>profile property {@code "gitolite_default_prefix"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    String getDefaultPrefix() {
        profileProperty "gitolite_default_prefix", gitoliteProperties
    }

    /**
     * Returns the default service data path, for
     * example {@code "/var/git"}. That is the path where the
     * source code repositories are located.
     *
     * <ul>
     * <li>profile property {@code "gitolite_default_data"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    String getDefaultData() {
        profileProperty "gitolite_default_data", gitoliteProperties
    }

    /**
     * Returns the default service override mode, for
     * example {@code "upgrade"}.
     *
     * <ul>
     * <li>profile property {@code "gitolite_default_override_mode"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    OverrideMode getDefaultOverrideMode() {
        def value = profileProperty "gitolite_default_override_mode", gitoliteProperties
        OverrideMode.valueOf(value)
    }

    /**
     * Returns the default service local user name, for
     * example {@code "git"}
     *
     * <ul>
     * <li>profile property {@code "gitolite_default_user"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    String getDefaultUser() {
        profileProperty "gitolite_default_user", gitoliteProperties
    }

    /**
     * Returns the default service local group name, for
     * example {@code "git"}
     *
     * <ul>
     * <li>profile property {@code "gitolite_default_group"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    String getDefaultGroup() {
        profileProperty "gitolite_default_group", gitoliteProperties
    }

    /**
     * Returns the default <i>Gitolite</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getGitoliteProperties()

    /**
     * Returns the <i>Gitolite</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript getScript() {
        script
    }

    /**
     * Delegates missing property to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing method to the parent script.
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
