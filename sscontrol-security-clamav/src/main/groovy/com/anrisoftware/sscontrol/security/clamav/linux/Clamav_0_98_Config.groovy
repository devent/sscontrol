/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav.linux

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.format.byteformat.ByteFormat
import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.security.clamav.ClamavService

/**
 * <i>ClamAV 0.98.x</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Clamav_0_98_Config {

    /**
     * @see ServiceConfig#getScript()
     */
    private Object script

    @Inject
    Clamav_0_98_ConfigLogger logg

    @Inject
    ByteFormatFactory byteFormatFactory

    @Inject
    DurationAttributeRenderer durationAttributeRenderer

    TemplateResource clamavConfigTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Clamav_0_98_Config", ["renderers": [
                durationAttributeRenderer
            ]]
        this.clamavConfigTemplate = templates.getResource "clamav_config"
    }

    /**
     * Setups the default debug logging.
     *
     * @param service
     *            the {@link ClamavService} service.
     *
     * @see #getDefaultDebugLogLevel()
     * @see #getDefaultDebugLogTarget()
     */
    void setupDefaultDebug(ClamavService service) {
        def logLevel = defaultDebugLogLevel
        if (!service.debugLogging("level") || !service.debugLogging("level")["log"]) {
            service.debug "log", level: logLevel
        }
    }

    /**
     * Setups the default properties of the service.
     *
     * @param service
     *            the {@link ClamavService} service.
     *
     * @see #getDefaultBindPort()
     */
    void setupDefaults(ClamavService service) {
        if (service.bindingAddresses && service.bindingAddresses.size() > 0) {
            def address = service.bindingAddresses.keySet().iterator().next()
            if (!service.bindingAddresses[address] || service.bindingAddresses[address].size() == 0) {
                service.bind address, defaultBindPort
            }
        }
    }

    /**
     * Deploys the <i>ClamAV</i> configuration.
     *
     * @param service
     *            the {@link ClamavService}.
     *
     * @see #getClamavConfFile()
     */
    void deployClamavConfig(ClamavService service) {
        def configs = [
            configToken(clamavConfigTemplate, "tcpSocketConfig", "binding", service.bindingAddresses),
            configToken(clamavConfigTemplate, "tcpAddrConfig", "binding", service.bindingAddresses),
            configToken(clamavConfigTemplate, "localSocketConfig", "binding", service.bindingAddresses, "socket", profileProperty("clamav_local_socket", clamavProperties)),
            configToken(clamavConfigTemplate, "fixStaleSocketConfig", "binding", service.bindingAddresses, "enabled", profileBooleanProperty("clamav_fix_stale_socket", clamavProperties)),
            configToken(clamavConfigTemplate, "localSocketGroupConfig", "binding", service.bindingAddresses, "socketGroup", profileProperty("clamav_local_socket_group", clamavProperties)),
            configToken(clamavConfigTemplate, "localSocketModeConfig", "binding", service.bindingAddresses, "mode", profileProperty("clamav_local_socket_mode", clamavProperties)),
            configToken(clamavConfigTemplate, "scanMailConfig", "enabled", profileBooleanProperty("clamav_scan_mail", clamavProperties)),
            configToken(clamavConfigTemplate, "scanArchiveConfig", "enabled", profileBooleanProperty("clamav_scan_archive", clamavProperties)),
            configToken(clamavConfigTemplate, "archiveBlockEncryptedConfig", "enabled", profileBooleanProperty("clamav_encrypted_archive_virus", clamavProperties)),
            configToken(clamavConfigTemplate, "maxDirectoryRecursionConfig", "max", (int)profileNumberProperty("clamav_max_directory_recursion", clamavProperties)),
            configToken(clamavConfigTemplate, "followDirectorySymlinksConfig", "enabled", profileBooleanProperty("clamav_follow_directory_symlinks", clamavProperties)),
            configToken(clamavConfigTemplate, "followFileSymlinksConfig", "enabled", profileBooleanProperty("clamav_follow_file_symlinks", clamavProperties)),
            configToken(clamavConfigTemplate, "readTimeoutConfig", "timeout", profileDurationProperty("clamav_read_timeout", clamavProperties)),
            configToken(clamavConfigTemplate, "maxThreadsConfig", "max", (int)profileNumberProperty("clamav_max_threads", clamavProperties)),
            configToken(clamavConfigTemplate, "logSyslogConfig", "enabled", profileBooleanProperty("clamav_log_syslog", clamavProperties)),
            configToken(clamavConfigTemplate, "logRotateConfig", "enabled", profileBooleanProperty("clamav_log_rotate", clamavProperties)),
            configToken(clamavConfigTemplate, "logFacilityConfig", "facility", profileProperty("clamav_log_facility", clamavProperties)),
            configToken(clamavConfigTemplate, "logCleanConfig", "enabled", profileBooleanProperty("clamav_log_clean_files", clamavProperties)),
            configToken(clamavConfigTemplate, "logVerboseConfig", "enabled", isVerboseLog(service.debugLogging("log")["level"])),
            configToken(clamavConfigTemplate, "selfCheckConfig", "duration", profileDurationProperty("clamav_database_check_period", clamavProperties)),
            configToken(clamavConfigTemplate, "scanPEConfig", "enabled", profileBooleanProperty("clamav_scan_portable_executable", clamavProperties)),
            configToken(clamavConfigTemplate, "maxEmbeddedPEConfig", "size", ByteFormat.roundSizeSI(profileTypedProperty("clamav_max_embedded_portable_executable_size", byteFormatFactory.create(), clamavProperties))),
            configToken(clamavConfigTemplate, "scanOLE2Config", "enabled", profileBooleanProperty("clamav_scan_ole2", clamavProperties)),
            configToken(clamavConfigTemplate, "scanPDFConfig", "enabled", profileBooleanProperty("clamav_scan_pdf", clamavProperties)),
            configToken(clamavConfigTemplate, "scanHTMLConfig", "enabled", profileBooleanProperty("clamav_scan_html", clamavProperties)),
            configToken(clamavConfigTemplate, "scanSWFConfig", "enabled", profileBooleanProperty("clamav_scan_swf", clamavProperties)),
            configToken(clamavConfigTemplate, "detectBrokenExecutablesConfig", "enabled", profileBooleanProperty("clamav_detect_broken_executables", clamavProperties)),
            configToken(clamavConfigTemplate, "exitOnOOMConfig", "enabled", profileBooleanProperty("clamav_exit_on_oom", clamavProperties)),
            configToken(clamavConfigTemplate, "algorithmicDetectionConfig", "enabled", profileBooleanProperty("clamav_algorithmic_detection", clamavProperties)),
            configToken(clamavConfigTemplate, "scanELFConfig", "enabled", profileBooleanProperty("clamav_scan_elf", clamavProperties)),
            configToken(clamavConfigTemplate, "phishingSignaturesConfig", "enabled", profileBooleanProperty("clamav_phishing_signatures", clamavProperties)),
            configToken(clamavConfigTemplate, "phishingScanURLsConfig", "enabled", profileBooleanProperty("clamav_phishing_scan_urls", clamavProperties)),
            configToken(clamavConfigTemplate, "maxQueueConfig", "max", (int)profileNumberProperty("clamav_max_queue", clamavProperties)),
            configToken(clamavConfigTemplate, "maxScanSizeConfig", "size", ByteFormat.roundSizeSI(profileTypedProperty("clamav_max_scan_size", byteFormatFactory.create(), clamavProperties))),
            configToken(clamavConfigTemplate, "maxFileSizeConfig", "size", ByteFormat.roundSizeSI(profileTypedProperty("clamav_max_file_size", byteFormatFactory.create(), clamavProperties))),
            configToken(clamavConfigTemplate, "maxFilesConfig", "max", (int)profileNumberProperty("clamav_max_files_archive", clamavProperties)),
            configToken(clamavConfigTemplate, "bytecodeConfig", "enabled", profileBooleanProperty("clamav_bytecode", clamavProperties)),
            configToken(clamavConfigTemplate, "bytecodeSecurityConfig", "level", profileProperty("clamav_bytecode_security", clamavProperties)),
            configToken(clamavConfigTemplate, "crossFilesystemsConfig", "enabled", profileBooleanProperty("clamav_cross_filesystems_scan", clamavProperties)),
        ]
        def file = clamavConfFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    /**
     * Returns the <i>ClamAV</i> configuration file, for
     * example {@code "/etc/clamav/clamd.conf"}
     *
     * <ul>
     * <li>profile property {@code "clamav_conf_file"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    File getClamavConfFile() {
        profileDirProperty "clamav_conf_file", clamavProperties
    }

    /**
     * Returns the <i>Frashclam</i> configuration file, for
     * example {@code "/etc/clamav/freshclam.conf"}
     *
     * <ul>
     * <li>profile property {@code "freshclam_conf_file"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    File getFrashclamConfFile() {
        profileDirProperty "freshclam_conf_file", clamavProperties
    }

    /**
     * Returns the default debug log level, for
     * example {@code 0}
     *
     * <ul>
     * <li>profile property {@code "clamav_default_debug_log_level"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    int getDefaultDebugLogLevel() {
        profileNumberProperty "clamav_default_debug_log_level", clamavProperties
    }

    /**
     * Returns the default binding port, for
     * example {@code 3310}
     *
     * <ul>
     * <li>profile property {@code "clamav_default_bind_port"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    int getDefaultBindPort() {
        profileNumberProperty "clamav_default_bind_port", clamavProperties
    }

    final TokenTemplate configToken(TemplateResource template, Object... args) {
        def search = template.getText(true, "${args[0]}Search")
        def replace = template.getText(true, args)
        new TokenTemplate(search, replace)
    }

    final boolean isVerboseLog(def level) {
        level > 1
    }

    /**
     * Returns the default <i>ClamAV</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getClamavProperties()

    /**
     * Returns the <i>ClamAV</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
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
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
