/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service

import static org.apache.commons.lang3.StringUtils.*

import java.nio.charset.Charset

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.codehaus.groovy.runtime.InvokerHelper

import com.anrisoftware.globalpom.textmatch.tokentemplate.DefaultTokensTemplate
import com.anrisoftware.globalpom.textmatch.tokentemplate.DefaultTokensTemplateFactory
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenMarker
import com.anrisoftware.globalpom.threads.api.Threads
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.core.api.Service

/**
 * Provides utilities methods for a Linux service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class LinuxScript extends Script {

    private static final String SERVICE_NAME = "name"

    /**
     * The name of the script.
     */
    String name

    /**
     * The service {@link ProfileProperties} profile properties.
     */
    ProfileProperties profile

    /**
     * The {@link Service} of the script.
     */
    Service service

    /**
     * The {@link Threads} threads pool for the script.
     */
    Threads threads

    @Inject
    private LinuxScriptLogger log

    @Inject
    DefaultTokensTemplateFactory tokensTemplateFactory

    Templates commandTemplates

    /**
     * Returns the service of the script.
     *
     * @return the {@link Service}.
     */
    Service getService() {
        service
    }

    /**
     * Returns the system packages to install.
     *
     * <ul>
     * <li>profile property {@code system_packages}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getSystemPackages() {
        profileListProperty "system_packages", defaultProperties
    }

    /**
     * Returns the packages sources file, for
     * example {@code /etc/apt/sources.list}.
     *
     * <ul>
     * <li>profile property {@code packages_sources_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getPackagesSourcesFile() {
        profileProperty("packages_sources_file", defaultProperties) as File
    }

    /**
     * Returns additional repository to enable.
     *
     * <ul>
     * <li>profile property {@code additional_repository}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAdditionalRepository() {
        profileProperty "additional_repository", defaultProperties
    }

    /**
     * Returns the distribution name, for example {@code "lucid"}.
     *
     * <ul>
     * <li>profile property {@code distribution_name}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDistributionName() {
        profileProperty "distribution_name", defaultProperties
    }

    /**
     * Returns the repository string, for
     * example {@code "deb http://archive.ubuntu.com/ubuntu <distributionName> <repository>"}
     *
     * <ul>
     * <li>profile property {@code repository_string}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRepositoryString() {
        profileProperty "repository_string", defaultProperties
    }

    /**
     * Returns the install command.
     *
     * <ul>
     * <li>property key {@code install_command}</li>
     * </ul>
     */
    String getInstallCommand() {
        profileProperty "install_command", defaultProperties
    }

    /**
     * Returns the {@code apt-key} command.
     *
     * <ul>
     * <li>property key {@code apt_key_command}</li>
     * </ul>
     */
    String getAptKeyCommand() {
        profileProperty "apt_key_command", defaultProperties
    }

    /**
     * Returns the change owner command.
     *
     * <ul>
     * <li>property key {@code chown_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getChownCommand() {
        profileProperty "chown_command", defaultProperties
    }

    /**
     * Returns the name of the root user, for example {@code "root".}
     *
     * <ul>
     * <li>profile property {@code "root_user"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRootUser() {
        profileProperty "root_user", defaultProperties
    }

    /**
     * Returns the name of the root group, for example {@code "root".}
     *
     * <ul>
     * <li>profile property {@code "root_group"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRootGroup() {
        profileProperty "root_group", defaultProperties
    }

    /**
     * Returns the configuration on the server.
     */
    String currentConfiguration(File file) {
        if (file.isFile()) {
            FileUtils.readFileToString file, charset
        } else {
            log.noConfigurationFound this, file
            ""
        }
    }

    /**
     * Deploys the configuration to the configuration file.
     */
    def deployConfiguration(TokenMarker tokenMarker, String currentConfiguration, List configurations, File file) {
        configurations = configurations.flatten()
        DefaultTokensTemplate tokens
        String configuration = currentConfiguration
        configurations.each {
            tokens = tokensTemplateFactory.create(tokenMarker, it, configuration).replace()
            configuration = tokens.text
        }
        FileUtils.write file, configuration, charset
        log.deployConfigurationDone this, file, configuration
    }

    /**
     * Returns the configuration template tokens.
     *
     * @param comment
     * 			  the string that marks the beginning of a comment in the
     * 			  configuration. Defaults to '#'.
     */
    TokenMarker configurationTokens(def comment = "#") {
        new TokenMarker("$comment SSCONTROL-$name", "$comment SSCONTROL-$name-END\n")
    }

    /**
     * Returns the unpack commands for the archive.
     */
    String unpackCommand(String archive) {
        def name = FilenameUtils.getName(archive)
        return unpackCommands[archiveType(archive)]
    }

    /**
     * Returns the type of the archive.
     */
    String archiveType(String archive) {
        def name = FilenameUtils.getName(archive)
        switch (name) {
            case ~/.*\.tar\.gz$/:
                return "tgz"
            case ~/.*\.zip$/:
                return "zip"
            case ~/.*\.gz$/:
                return "gz"
            default:
                return "unknown"
        }
    }

    /**
     * Returns the unpack commands for the archive types.
     *
     * @see #getTarCommand()
     * @see #getUnzipCommand()
     * @see #getGunzipCommand()
     */
    Map getUnpackCommands() {
        [tgz: tarCommand, zip: unzipCommand, gz: gunzipCommand]
    }

    /**
     * Returns the {@code tar} command.
     *
     * <ul>
     * <li>property key {@code tar_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getTarCommand() {
        profileProperty "tar_command", defaultProperties
    }

    /**
     * Returns the {@code unzip} command.
     *
     * <ul>
     * <li>property key {@code unzip_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUnzipCommand() {
        profileProperty "unzip_command", defaultProperties
    }

    /**
     * Returns the {@code gunzip} command.
     *
     * <ul>
     * <li>property key {@code gunzip_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getGunzipCommand() {
        profileProperty "gunzip_command", defaultProperties
    }

    /**
     * Returns the file link command.
     *
     * <ul>
     * <li>property key {@code link_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getLinkCommand() {
        profileProperty "link_command", defaultProperties
    }

    /**
     * Returns the file link command.
     *
     * <ul>
     * <li>property key {@code "change_password_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getChangePasswordCommand() {
        profileProperty "change_password_command", defaultProperties
    }

    /**
     * Returns the default character set.
     *
     * <ul>
     * <li>property key {@code charset}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Charset getCharset() {
        Charset.forName profileProperty("charset", defaultProperties)
    }

    /**
     * Returns the restart command for the service.
     *
     * <ul>
     * <li>property key {@code restart_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRestartCommand() {
        profileProperty "restart_command", defaultProperties
    }

    /**
     * Returns the services to restart.
     *
     * <ul>
     * <li>profile property {@code restart_services}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getRestartServices() {
        profileListProperty "restart_services", defaultProperties
    }

    /**
     * Returns the stop command for the service.
     *
     * <ul>
     * <li>property key {@code stop_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getStopCommand() {
        profileProperty "stop_command", defaultProperties
    }

    /**
     * Returns the services to stop.
     *
     * <ul>
     * <li>profile property {@code stop_services}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getStopServices() {
        profileListProperty "stop_services", defaultProperties
    }

    /**
     * Returns the service packages.
     *
     * <ul>
     * <li>profile property {@code "packages"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getPackages() {
        profileListProperty "packages", defaultProperties
    }

    /**
     * Returns the path of the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "configuration_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getConfigurationDir() {
        profileDirProperty "configuration_directory", defaultProperties
    }

    /**
     * Returns the path of the local software directory, for example
     * {@code "/usr/local".}
     *
     * <ul>
     * <li>profile property {@code "local_software_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getLocalSoftwareDir() {
        profileDirProperty "local_software_directory", defaultProperties
    }

    /**
     * Returns the path of the temporary directory, defaults to
     * {@link System#getProperties()}.
     *
     * <ul>
     * <li>profile property {@code "temp_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getTmpDirectory() {
        if (containsKey("temp_directory")) {
            def dir = profileProperty("temp_directory", defaultProperties)
            if (dir instanceof File) {
                return dir
            }
            if (StringUtils.isBlank(dir)) {
                return javaTmpDirectory
            } else {
                return new File(dir)
            }
        } else {
            return javaTmpDirectory
        }
    }

    def getJavaTmpDirectory() {
        new File(System.getProperty("java.io.tmpdir"))
    }

    /**
     * Returns the change permissions command.
     *
     * <ul>
     * <li>property key {@code chmod_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getChmodCommand() {
        profileProperty "chmod_command", defaultProperties
    }

    /**
     * Returns the command to create a new local group.
     *
     * <ul>
     * <li>property key {@code group_add_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getGroupAddCommand() {
        profileProperty "group_add_command", defaultProperties
    }

    /**
     * Returns the local groups file.
     *
     * <ul>
     * <li>property key {@code groups_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getGroupsFile() {
        profileProperty("groups_file", defaultProperties) as File
    }

    /**
     * Returns the command to create a new local user.
     *
     * <ul>
     * <li>property key {@code user_add_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUserAddCommand() {
        profileProperty "user_add_command", defaultProperties
    }

    /**
     * Returns the local users file.
     *
     * <ul>
     * <li>property key {@code users_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getUsersFile() {
        profileProperty("users_file", defaultProperties) as File
    }

    /**
     * Returns the command to modify a local user, for
     * example {@code "/usr/sbin/usermod"}.
     *
     * <ul>
     * <li>property key {@code user_mod_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUserModCommand() {
        profileProperty "user_mod_command", defaultProperties
    }

    /**
     * Returns the command to modify a local group, for
     * example {@code "/usr/sbin/groupmod"}.
     *
     * <ul>
     * <li>property key {@code group_mod_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getGroupModCommand() {
        profileProperty "group_mod_command", defaultProperties
    }

    /**
     * Returns the command to show the running processes, for
     * example {@code "/bin/ps"}.
     *
     * <ul>
     * <li>property key {@code ps_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getPsCommand() {
        profileProperty "ps_command", defaultProperties
    }

    /**
     * Returns the command to terminate a process, for
     * example {@code "/usr/bin/kill"}.
     *
     * <ul>
     * <li>property key {@code kill_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getKillCommand() {
        profileProperty "kill_command", defaultProperties
    }

    /**
     * Returns the reconfigure command, for
     * example {@code "/usr/sbin/dpkg-reconfigure"}.
     *
     * <ul>
     * <li>property key {@code reconfigure_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getReconfigureCommand() {
        profileProperty "reconfigure_command", defaultProperties
    }

    /**
     * Returns the <i>zcat</i> command, for example {@code "/bin/zcat"}.
     *
     * <ul>
     * <li>property key {@code zcat_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getZcatCommand() {
        profileProperty "zcat_command", defaultProperties
    }

    /**
     * Returns the <i>netstat</i> command, for example {@code "/bin/netstat"}.
     *
     * <ul>
     * <li>property key {@code netstat_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getNetstatCommand() {
        profileProperty "netstat_command", defaultProperties
    }

    /**
     * Returns the default properties for the service, as in example:
     *
     * <pre>
     * 	&#64;Inject
     *	&#64;Named("my-properties")
     *	ContextProperties myProperties
     *
     *	&#64;Override
     *	def getDefaultProperties() {
     *		myProperties
     *	}
     * </pre>
     */
    abstract ContextProperties getDefaultProperties()

    /**
     * Set properties of the script.
     */
    @Override
    void setProperty(String property, Object newValue) {
        metaClass.setProperty(this, property, newValue)
    }

    /**
     * Delegates profile property methods.
     */
    def methodMissing(String name, def args) {
        switch (name) {
            case ~/profile.*Property/:
            case 'containsKey':
                return InvokerHelper.invokeMethod(profile, name, args)
            default:
                throw new MissingMethodException(name, getClass(), args)
        }
    }

    /**
     * Runs the specified script in the context of the current script.
     */
    void runScript(LinuxScript script) {
        def that = this
        script.name = name
        script.profile = profile
        script.service = service
        script.metaClass.propertyMissing = { name -> that."$name" }
        script.metaClass.propertyMissing = { name, value -> that."$name($value)" }
        script.run()
    }

    @Override
    String toString() {
        new ToStringBuilder(this).append(SERVICE_NAME, getName())
    }
}
