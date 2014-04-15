/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
     * Installs the system packages.
     */
    void installSystemPackages() {
        def packages = systemPackages
        !packages.empty ? installPackages(packages) : null
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
     * Returns additional repositories to enable.
     *
     * <ul>
     * <li>profile property {@code additional_repositories}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getAdditionalRepositories() {
        profileListProperty "additional_repositories", defaultProperties
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
     * Enables the specified Debian repositories.
     *
     * @param repositories
     *               the list with the repositories.
     *
     * @param distributionName
     *               optionally, the name of the distribution.
     *
     * @return {@code true} if at least one of the repositories was enabled.
     *
     * @see #getDistributionName()
     */
    boolean enableDebRepositories(List repositories = additionalRepositories, String distributionName = distributionName) {
        boolean enabled = false
        repositories.each {
            if (!containsDebRepository(it, distributionName)) {
                enableDebRepository it, distributionName
                enabled = true
            }
        }
        return enabled
    }

    /**
     * Enables the Debian repository.
     *
     * @param repository
     *               the repository name.
     *
     * @param distribution
     *               the name of the distribution.
     *
     * @see #getPackagesSourcesFile()
     */
    void enableDebRepository(String repository, String distributionName) {
        def str = repositoryString repository, distributionName
        FileUtils.write packagesSourcesFile, "$str\n", charset, true
        log.enableRepositoryDone this, repository
    }

    /**
     * Returns the repository string.
     *
     * @param repository
     *            the repository name.
     *
     * @param distributionName
     *            optionally, the distribution name.
     *
     * <ul>
     * <li>profile property {@code repository_string}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getDistributionName()
     */
    String repositoryString(String repository, String distributionName = distributionName) {
        def str = profileProperty "repository_string", defaultProperties
        String.format str, distributionName, repository
    }

    /**
     * Tests if the system already have the repository enabled.
     *
     * @param repository
     *            the repository name.
     *
     * @param distributionName
     *            optionally, the distribution name.
     *
     * @return {@code true} if the repository is already enabled.
     *
     * @see #getPackagesSourcesFile()
     * @see #getPackagingType()
     */
    boolean containsDebRepository(String repository, String distributionName = distributionName) {
        def dir = packagesSourcesFile.parentFile
        def template = commandTemplates.getResource("list_repositories")
        def line = scriptCommandLineFactory.create(packagingType, template).addSub("configurationDir", dir)
        def task = scriptCommandExecFactory.create(commandExecFactory).exec(line).get()
        split(task.out, '\n').find { it.trim().endsWith "$distributionName $repository" } != null
    }

    /**
     * Returns the packaging type. The packaging type is the packaging system
     * used on the system, like {@code "apt", "yum".}
     *
     * <ul>
     * <li>property key {@code packaging_type}
     * </ul>
     */
    String getPackagingType() {
        profileProperty "packaging_type", defaultProperties
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
     * Change the permissions to the specified files.
     *
     * @param args
     * 			  the arguments:
     * <ul>
     * <li>{@code mod:} the modification;
     * <li>{@code files:} the file or files;
     * <li>{@code recursive:} optionally, set to {@code true} to
     * recursively change the permissions of all files and sub-directories.
     * <li>{@code system:} optionally, the system of the server.
     * Defaults to unix;
     * </ul>
     *
     * @return the {@link ProcessTask} process task.
     */
    def changeMod(Map args) {
        args.command = args.containsKey("command") ? args.command : chmodCommand
        args.system = args.containsKey("system") ? args.system : "unix"
        log.checkChangeModArgs args
        def template = commandTemplates.getResource("chmod")
        def line = scriptCommandLineFactory.create(args.system, template).addSub("args", args)
        def script = scriptCommandExecFactory.create(commandExecFactory)
        script.commandError = errorLogCommandOutputFactory.create(log, line)
        script.commandOutput = debugLogCommandOutputFactory.create(log, line)
        def task = script.exec(line).get()
        log.changeModDone this, task, args
        return task
    }

    /**
     * Change the owner to the specified files.
     *
     * @param args
     * 			  the arguments:
     * <ul>
     * <li>{@code owner:} the owner user;
     * <li>{@code ownerGroup:} the owner group;
     * <li>{@code files:} the file or files;
     * <li>{@code recursive:} optionally, set to {@code true} to
     * recursively change the owner of all files and sub-directories.
     * <li>{@code system:} optionally, the system of the server.
     * Defaults to unix;
     * </ul>
     *
     * @return the {@link ProcessTask} process task.
     */
    def changeOwner(Map args) {
        args.command = args.containsKey("command") ? args.command : chownCommand
        args.system = args.containsKey("system") ? args.system : "unix"
        log.checkChangeOwnerArgs args
        def template = commandTemplates.getResource("chown")
        def line = scriptCommandLineFactory.create(args.system, template).addSub("args", args)
        def script = scriptCommandExecFactory.create(commandExecFactory)
        script.commandError = errorLogCommandOutputFactory.create(log, line)
        script.commandOutput = debugLogCommandOutputFactory.create(log, line)
        def task = script.exec(line).get()
        log.changeOwnerDone this, task, args
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
     * Unpacks the specified archives.
     *
     * @param args
     * 			  the arguments:
     * <ul>
     * <li>{@code file:} the archive {@link File};
     * <li>{@code type:} the type of the archive, for example {@code tar, zip}.
     * If not set then the type is determined from the archive file name;
     * <li>{@code output:} the output {@link File} directory;
     * <li>{@code override:} set to {@code true} to override existing files;
     * <li>{@code system:} the system of the server, defaults to {@code unix};
     * <li>{@code command:} the command to unpack the archive,
     * defaults to what the type of the archive is;
     * </ul>
     *
     * @return the {@link ProcessTask} process task.
     */
    def unpack(Map args) {
        args.system = args.containsKey("system") ? args.system : "unix"
        args.type = args.containsKey("type") ? args.type : archiveType(args)
        args.command = args.containsKey("command") ? args.command : unpackCommand(args)
        log.checkUnpackArgs args
        def template = commandTemplates.getResource("unpack")
        def line = scriptCommandLineFactory.create(args.system, template).addSub("args", args)
        def script = scriptCommandExecFactory.create(commandExecFactory)
        script.commandError = errorLogCommandOutputFactory.create(log, line)
        script.commandOutput = debugLogCommandOutputFactory.create(log, line)
        def task = script.exec(line).get()
        log.unpackDone this, task, args.file, args.output
        return task
    }

    /**
     * Returns the type for the specified archive.
     *
     * @param args
     *            the arguments:
     * <ul>
     * <li>{@code file:} the archive {@link File};
     * </ul>
     *
     * @return the archive type.
     *
     * throws ServiceException
     *          if the archive type is unknown.
     */
    String archiveType(Map args) {
        def name = args.file
        if (args.file instanceof File) {
            name = args.file.getName()
        }
        switch (name) {
            case ~/.*tar\.gz$/:
                return "tgz"
            case ~/.*\.zip$/:
                return "zip"
            default:
                throw log.unknownArchiveType(this, args)
        }
    }

    /**
     * Returns the unpack command for the specified archive.
     *
     * @param args
     *            the arguments:
     * <ul>
     * <li>{@code type:} the archive type;
     * </ul>
     *
     * @return the archive type.
     *
     * throws ServiceException
     *          if the archive type is unknown.
     */
    String unpackCommand(Map args) {
        switch (args.type) {
            case 'tgz':
                return tarCommand
            case 'zip':
                return unzipCommand
            default:
                throw log.unknownArchiveType(this, args)
        }
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
     * Link files and directories.
     *
     * @param args
     * 			  the arguments:
     * <ul>
     * <li>{@code files:} the source {@link File} or files.
     * <li>{@code targets:} the target {@link File} or files;
     * <li>{@code system:} the system of the server, defaults to {@code unix};
     * <li>{@code command:} the link command, defaults to {@link #getLinkCommand()}.
     * <li>{@code override:} set to {@code true} to override existing links.
     * </ul>
     *
     * @return the {@link ProcessTask} process task.
     */
    def link(Map args) {
        args.system = args.containsKey("system") ? args.system : "unix"
        args.command = args.containsKey("command") ? args.command : linkCommand
        log.checkLinkArgs args
        def template = commandTemplates.getResource("mkln")
        def line = scriptCommandLineFactory.create(args.system, template).addSub("args", args)
        def script = scriptCommandExecFactory.create(commandExecFactory)
        script.commandError = errorLogCommandOutputFactory.create(log, line)
        script.commandOutput = debugLogCommandOutputFactory.create(log, line)
        def task = script.exec(line).get()
        log.linkFilesDone this, task, args
        return task
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
     * Changes the password of the specified user.
     *
     * @param args
     *            the arguments:
     * <ul>
     * <li>{@code userName:} the user name;
     * <li>{@code password:} the new password;
     * <li>{@code name:} the distribution of the server, for example {@code "redhat", "debian"};
     * <li>{@code system:} optionally, the system of the server;
     * <li>{@code command:} optionally, the change password command;
     * </ul>
     *
     * @return the {@link ProcessTask} process task.
     *
     * @see #getChangePasswordCommand()
     */
    def changePassword(Map args) {
        args.command = args.containsKey("command") ? args.command : changePasswordCommand
        args.system = args.containsKey("system") ? args.system : "unix"
        log.checkChangePasswordArgs args
        def template = commandTemplates.getResource("changepass")
        def line = scriptCommandLineFactory.create(args.system, template).addSub("args", args)
        def script = scriptCommandExecFactory.create(commandExecFactory)
        script.commandError = errorLogCommandOutputFactory.create(log, line)
        script.commandOutput = debugLogCommandOutputFactory.create(log, line)
        def task = script.exec(line).get()
        log.changePasswordDone this, task, args
        return task
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
     * Adds the specified local group.
     *
     * @param args
     * 			  the arguments:
     * <ul>
     * <li>{@code groupName:} the name of the group;
     * <li>{@code groupId:} optionally, the ID of the group;
     * <li>{@code systemGroup:} optionally, {@code true} if the group should be a system group;
     * <li>{@code system:} the system of the server, defaults to {@code unix};
     * <li>{@code command:} the link command, defaults to {@link #getGroupAddCommand()};
     * </ul>
     */
    void addGroup(Map args) {
        args.system = args.containsKey("system") ? args.system : "unix"
        args.command = args.containsKey("command") ? args.command : groupAddCommand
        args.groupsFile = args.containsKey("groupsFile") ? args.groupsFile : groupsFile
        log.checkAddGroupArgs args
        def template = commandTemplates.getResource("groupadd")
        def worker = scriptCommandFactory.create(template, args.system, "args", args)()
        log.addGroupDone this, worker, args
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
     * Adds the specified local user.
     *
     * @param args
     * 			  the arguments:
     * <ul>
     * <li>{@code userName:} the name of the user;
     * <li>{@code groupName:} the name of the user's group;
     * <li>{@code userId:} optionally, the ID of the user;
     * <li>{@code systemUser:} optionally, {@code true} if the group should be a system group;
     * <li>{@code homeDir:} optionally, the home directory for the user;
     * <li>{@code shell:} optionally, the shell for the user;
     * <li>{@code system:} the system of the server, defaults to {@code unix};
     * <li>{@code command:} the link command, defaults to {@link #getUserAddCommand()};
     * </ul>
     */
    void addUser(Map args) {
        args.system = args.containsKey("system") ? args.system : "unix"
        args.command = args.containsKey("command") ? args.command : userAddCommand
        args.usersFile = args.containsKey("usersFile") ? args.usersFile : usersFile
        if (args.containsKey("homeDir")) {
            args.homeDir.parentFile.mkdirs()
        }
        log.checkAddUserArgs args
        def template = commandTemplates.getResource("useradd")
        def worker = scriptCommandFactory.create(template, args.system, "args", args)()
        log.addUserDone this, worker, args
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
     * Modifies the specified local user.
     *
     * @param args
     *            the arguments:
     * <ul>
     * <li>{@code userName:} the name of the user;
     * <li>{@code groups:} optionally, the list of the user groups;
     * <li>{@code append:} optionally, set to {@code true} to append the specified groups;
     * <li>{@code shell:} optionally, the shell for the user;
     * <li>{@code system:} the system of the server, defaults to {@code unix};
     * <li>{@code command:} the link command, defaults to {@link #getUserModCommand()};
     * </ul>
     */
    void changeUser(Map args) {
        args.system = args.containsKey("system") ? args.system : "unix"
        args.command = args.containsKey("command") ? args.command : userModCommand
        log.checkModUserArgs args
        def template = commandTemplates.getResource("usermod")
        def worker = scriptCommandFactory.create(template, args.system, "args", args)()
        log.modifyUserDone this, worker, args
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
     * Returns the zcat command, for example {@code "/bin/zcat"}.
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
     * Checks if the listed ports are already in use by a service.
     *
     * @param ports
     *            the ports.
     *
     * @return the {@link Map} of service names that are using the found ports.
     */
    Map checkPortsInUse(def ports) {
        def command = listUsedPorts()
        def lines = StringUtils.split command.out, "\n"
        def found = [:]
        lines.each { val ->
            def port = ports.find { port -> StringUtils.contains(val, ":$port ") }
            port ? parsePortService(found, port, val) : false
        }
        return found
    }

    Map parsePortService(Map map, int port, String line) {
        def split = StringUtils.split line, " "
        def service = split.find { it =~ /\d+\/.*/ }
        service = StringUtils.split(service, "/")[-1]
        map[port] = service
        return map
    }

    /**
     * Execute the command to list ports in use.
     *
     * @return the {@link ProcessTask} process task.
     */
    def listUsedPorts() {
        execCommandFactory.create("$netstatCommand -pnl")()
    }

    /**
     * Returns the {@code "netstat"} command, for example {@code "/bin/netstat"}.
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
