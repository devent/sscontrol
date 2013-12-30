/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.openssh.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.sscontrol.core.bindings.BindingFactory
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.User

/**
 * Base OpenSSH remote script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseOpensshRemoteScript extends LinuxScript {

    @Inject
    Map<String, RemoteScript> remoteScript

    @Inject
    private BindingFactory bindingFactory

    @Inject
    private DebugLoggingProperty debugLoggingProperty

    @Override
    def run() {
        setupParentScript()
        super.run()
        setupDefaultDebug()
        setupDefaultBinding()
        beforeConfiguration()
        remoteScript.users.deployRemoteScript service
        remoteScript.userkey.deployRemoteScript service
        remoteScript.authorizedkeys.deployRemoteScript service
        remoteScript.screen.deployRemoteScript service
    }

    void setupParentScript() {
        remoteScript.each { key, RemoteScript value ->
            value.setScript this
        }
    }

    /**
     * Deploys the configuration before the script configuration.
     */
    void beforeConfiguration() {
    }

    /**
     * Returns the default bind addresses and ports.
     *
     * <ul>
     * <li>profile property {@code "default_binding"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultBinding() {
        profileListProperty "default_binding", defaultProperties
    }

    /**
     * Returns the default debug logging.
     *
     * <ul>
     * <li>profile property {@code "default_debug"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    DebugLogging getDefaultDebug() {
        def str = profileProperty "default_debug", defaultProperties
        def args = str.split(",").inject([:]) { acc, val ->
            def nameAndValue = val.split(":")
            acc[nameAndValue[0].trim()] = nameAndValue[1].trim()
            acc
        }
        debugLoggingFactory.create args
    }

    /**
     * Returns minimal user ID, for example {@code "1000".}
     *
     * <ul>
     * <li>profile property {@code "minimum_uid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumUid() {
        profileNumberProperty "minimum_uid", defaultProperties
    }

    /**
     * Returns minimal group ID, for example {@code "1000".}
     *
     * <ul>
     * <li>profile property {@code "minimum_gid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumGid() {
        profileNumberProperty "minimum_gid", defaultProperties
    }

    /**
     * Returns the user home directory pattern, for
     * example {@code "/home/<user.name>".}
     *
     * <ul>
     * <li>profile property {@code "home_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getHomePattern() {
        profileProperty "home_pattern", defaultProperties
    }

    /**
     * Returns the user home directory for the specified user.
     *
     * @param user
     *            the {@link User}.
     *
     * @see #getHomePattern()
     */
    File homeDir(User user) {
        def st = new ST(homePattern).add("user", user)
        new File(st.render())
    }

    /**
     * Returns the user SSH/key pattern, for
     * example {@code "/home/<user.name>/.ssh/id_rsa".}
     *
     * <ul>
     * <li>profile property {@code "ssh_key_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSshkeyPattern() {
        profileProperty "ssh_key_pattern", defaultProperties
    }

    /**
     * Returns the user SSH/key file for the specified user.
     *
     * @param user
     *            the {@link User}.
     *
     * @see #getSshkeyPattern()
     */
    File sshkeyFile(User user) {
        def st = new ST(sshkeyPattern).add("user", user)
        new File(st.render())
    }

    /**
     * Returns the user authorized keys file pattern, for
     * example {@code "<user.home>/.ssh/authorized_keys".}
     *
     * <ul>
     * <li>profile property {@code "authorized_keys_file_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAuthorizedKeysFilePattern() {
        profileProperty "authorized_keys_file_pattern", defaultProperties
    }

    /**
     * Returns the user authorized keys file for the specified user.
     *
     * @param user
     *            the {@link User}.
     *
     * @see #getAuthorizedKeysFilePattern()
     */
    File authorizedKeysFile(User user) {
        def st = new ST(authorizedKeysFilePattern).add("user", user)
        new File(st.render())
    }

    /**
     * Returns the default log-in shell, for
     * example {@code "/bin/bash".}
     *
     * <ul>
     * <li>profile property {@code "default_login_shell"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultLoginShell() {
        profileProperty "default_login_shell", defaultProperties
    }

    /**
     * Returns SSH/key generation command, for
     * example {@code "/usr/bin/ssh-keygen".}
     *
     * <ul>
     * <li>profile property {@code "key_gen_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getKeyGenCommand() {
        profileProperty "key_gen_command", defaultProperties
    }

    /**
     * Returns the SSHD configuration file, for
     * example {@code "sshd_config".} If the path is not absolute then the
     * file is assumed under the SSHD configuration directory.
     *
     * <ul>
     * <li>profile property {@code "sshd_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getSshdConfigDir()
     */
    File getSshdConfigFile() {
        profileFileProperty "sshd_configuration_file", sshdConfigDir, defaultProperties
    }

    /**
     * Returns the SSHD configuration directory, for
     * example {@code "/etc/ssh".}
     *
     * <ul>
     * <li>profile property {@code "sshd_configuration_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getSshdConfigDir() {
        profileProperty("sshd_configuration_directory", defaultProperties) as File
    }

    /**
     * Returns SSH protocols, for
     * example {@code "2".}
     *
     * <ul>
     * <li>profile property {@code "protocols"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getProtocols() {
        profileListProperty "protocols", defaultProperties
    }

    /**
     * Returns SSH duration grace time, for
     * example {@code "PT60S".}
     *
     * <ul>
     * <li>profile property {@code "login_grace_time"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Duration getLoginGraceTime() {
        profileDurationProperty "login_grace_time", defaultProperties
    }

    /**
     * Returns that the user root can log-in, for
     * example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "permit_root_login"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getPermitRootLogin() {
        profileBooleanProperty "permit_root_login", defaultProperties
    }

    /**
     * Returns that access modes for user's files and directories should
     * be checked, for example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "strict_modes"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getStrictModes() {
        profileBooleanProperty "strict_modes", defaultProperties
    }

    /**
     * Returns the authorized keys file string, for
     * example {@code "%h/.ssh/authorized_keys".}
     *
     * <ul>
     * <li>profile property {@code "authorized_keys_file_config"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAuthorizedKeysFile() {
        profileProperty "authorized_keys_file_config", defaultProperties
    }

    /**
     * Returns that authentication per password should be
     * allowed, for example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "password_authentication"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getPasswordAuthentication() {
        profileBooleanProperty "password_authentication", defaultProperties
    }

    /**
     * Returns that X11 forwarding should be allowed, for
     * example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "x_forwarding"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getXForwarding() {
        profileBooleanProperty "x_forwarding", defaultProperties
    }

    /**
     * Returns that screen configuration should be deployed for local
     * users, for example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "deploy_screen"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getDeployScreen() {
        profileBooleanProperty "deploy_screen", defaultProperties
    }

    /**
     * Returns the screen packages, for example {@code "screen".}
     *
     * <ul>
     * <li>profile property {@code "screen_packages"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getScreenPackages() {
        profileListProperty "screen_packages", defaultProperties
    }

    /**
     * Returns local scripts directory, for example {@code "/usr/local/bin".}
     *
     * <ul>
     * <li>profile property {@code "local_bin_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getLocalBinDirectory() {
        profileProperty("local_bin_directory", defaultProperties) as File
    }

    /**
     * Returns auto-screen script file, for example {@code "auto_script.sh".}
     * If the file path is not absolute it is assumed under the local scripts
     * directory.
     *
     * <ul>
     * <li>profile property {@code "auto_screen_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getLocalBinDirectory()
     */
    File getAutoScreenFile() {
        profileFileProperty "auto_screen_file", localBinDirectory, defaultProperties
    }

    /**
     * Returns screen-rc file for the specified user, for
     * example {@code ".screenrc".}
     *
     * <ul>
     * <li>profile property {@code "screen_configuration_file"}</li>
     * </ul>
     *
     * @see #homeDir(User)
     * @see #getDefaultProperties()
     */
    File screenConfigFile(User user) {
        def file = profileProperty "screen_configuration_file", defaultProperties
        new File(homeDir(user), file)
    }

    /**
     * Setups the default binding addresses and ports.
     */
    void setupDefaultBinding() {
        if (service.binding.size() == 0) {
            defaultBinding.each { service.binding.addAddress(it) }
        }
    }

    /**
     * Setups the default debug logging.
     */
    void setupDefaultDebug() {
        if (service.debug == null) {
            service.debug = debugLoggingProperty.defaultDebug this
        }
    }

    @Override
    RemoteService getService() {
        super.getService();
    }
}
