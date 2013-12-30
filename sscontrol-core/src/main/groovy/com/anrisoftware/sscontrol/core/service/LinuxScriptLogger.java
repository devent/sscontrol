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
package com.anrisoftware.sscontrol.core.service;

import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.addgroup_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.adduser_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.archive_type_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.args1;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.change_password_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.change_password_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.change_password_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.change_password_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chmod_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chmod_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chmod_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chmod_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chown_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chown_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chown_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chown_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.conf_file_found_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.conf_file_found_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.deployed_conf_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.deployed_conf_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.enabled_repository_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.enabled_repository_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.group_add_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.group_add_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.group_add_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.install_packages_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.link_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.link_files_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.link_files_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.link_files_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.properties_null;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.property_file_null;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.property_not_set;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.restarted_service_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.restarted_service_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.restarted_service_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.script1;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unknown_archive_type;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unknown_archive_type_message;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unpack_args_missing;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unpack_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unpack_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unpack_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.user_add_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.user_add_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.user_add_trace;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link LinuxScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LinuxScriptLogger extends AbstractLogger {

    private static final String GROUPS_FILE = "groupsFile";
    private static final String GROUP_NAME = "groupName";
    private static final String USERS_FILE = "usersFile";
    private static final String USER_NAME = "userName";
    private static final String TARGETS = "targets";
    private static final String OUTPUT = "output";
    private static final String TYPE = "type";
    private static final String FILE = "file";
    private static final String MOD = "mod";
    private static final String SYSTEM = "system";
    private static final String OWNER_GROUP = "ownerGroup";
    private static final String OWNER = "owner";
    private static final String COMMAND = "command";
    private static final String FILES = "files";
    private static final String PASSWORD = "password";
    private static final String DISTRBUTION_NAME = "name";

    enum _ {

        enabled_repository_debug("Enabled repository '{}' for {}."),

        enabled_repository_info("Enabled repository '{}' for service {}."),

        install_packages_done_trace("Installed packages {} in {}, {}."),

        install_packages_done_debug("Installed packages {} in {}."),

        install_packages_done_info("Installed service packages: {}."),

        conf_file_found_debug("No configuration file found '{}' in {}."),

        conf_file_found_info("No configuration file found '{}'."),

        deployed_conf_debug("Deploy configuration to '{}' in {}: '{}'."),

        deployed_conf_info("Deploy configuration to '{}'."),

        restarted_service_trace("Restarted service {}, {}."),

        restarted_service_debug("Restarted service {}."),

        restarted_service_info("Restarted service {}."),

        properties_null("Properties cannot be null for key '%s' in %s."),

        property_not_set("Property '%s' is not set for %s."),

        chmod_done_trace("Change permissions done {} in {}, {}."),

        chmod_done_debug("Change permissions done {} in {}."),

        chmod_done_info("Change permissions done for {}."),

        chown_done_trace("Change owner done {} in {}, {}."),

        chown_done_debug("Change owner done {} in {}."),

        chown_done_info("Change owner done for {}."),

        group_add_trace("Add group {} in {}, {}."),

        group_add_debug("Add group {} in {}."),

        group_add_info("Add group {} to service '{}'."),

        user_add_trace("Add user {} in {}, {}."),

        user_add_debug("Add user {} in {}."),

        user_add_info("Add user {} to service '{}'."),

        link_files_done_trace("Link {} done in {}, {}."),

        link_files_done_debug("Link {} done in {}."),

        link_files_done_info("Link files {} done in service '{}'."),

        unpack_done_trace("Unpack {} to {} done in {}, {}."),

        unpack_done_debug("Unpack {} to {} done in {}."),

        unpack_done_info("Unpack file {} to {} done in service '{}'."),

        change_password_done_trace("Change password {} to {} done in {}, {}."),

        change_password_done_debug("Change password {} to {} done in {}."),

        change_password_done_info("Chagen password {} done in service '{}'."),

        property_file_null("Property file cannot be null for key '%s' in %s."),

        chown_args_missing("Change owner argument '%s' missing."),

        chmod_args_missing("Change owner argument '%s' missing."),

        unpack_args_missing("Unpack argument '%s' missing."),

        link_args_missing("Link argument '%s' missing."),

        adduser_args_missing("Add user argument '%s' missing."),

        addgroup_args_missing("Add group argument '%s' missing."),

        archive_type_args_missing("Archive type argument '%s' missing."),

        unknown_archive_type("Unknown archive type"),

        unknown_archive_type_message(
                "Unknown archive type '{}' for script '{}'."),

        change_password_args_missing("Change password argument '%s' missing."),

        script1("script"),

        args1("arguments");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Create logger for {@link LinuxScript}.
     */
    LinuxScriptLogger() {
        super(LinuxScript.class);
    }

    void installPackagesDone(LinuxScript script, Object worker, Object packages) {
        if (isTraceEnabled()) {
            trace(install_packages_done_trace, packages, script, worker);
        } else if (isDebugEnabled()) {
            debug(install_packages_done_debug, packages, script);
        } else {
            info(install_packages_done_info, packages);
        }
    }

    void changeModDone(LinuxScript script, Object worker, Object args) {
        if (isTraceEnabled()) {
            trace(chmod_done_trace, args, script, worker);
        } else if (isDebugEnabled()) {
            debug(chmod_done_debug, args, script);
        } else {
            info(chmod_done_info, args);
        }
    }

    void changeOwnerDone(LinuxScript script, Object worker, Object args) {
        if (isTraceEnabled()) {
            trace(chown_done_trace, args, script, worker);
        } else if (isDebugEnabled()) {
            debug(chown_done_debug, args, script);
        } else {
            info(chown_done_info, args);
        }
    }

    void noConfigurationFound(LinuxScript script, Object file) {
        if (isDebugEnabled()) {
            debug(conf_file_found_debug, file, script);
        } else {
            info(conf_file_found_info, file);
        }
    }

    void deployConfigurationDone(LinuxScript script, File file,
            String configuration) {
        if (isDebugEnabled()) {
            debug(deployed_conf_debug, file, script, configuration);
        } else {
            info(deployed_conf_info, file);
        }
    }

    void enableRepositoryDone(LinuxScript script, String repository) {
        if (isDebugEnabled()) {
            debug(enabled_repository_debug, repository, script);
        } else {
            info(enabled_repository_info, repository, script.getName());
        }
    }

    void restartServiceDone(LinuxScript script, Object worker) {
        if (isTraceEnabled()) {
            trace(restarted_service_trace, script, worker);
        } else if (isDebugEnabled()) {
            debug(restarted_service_debug, script);
        } else {
            info(restarted_service_info, script.getName());
        }
    }

    void addGroupDone(LinuxScript script, Object worker, Object group) {
        if (isTraceEnabled()) {
            trace(group_add_trace, group, script, worker);
        } else if (isDebugEnabled()) {
            debug(group_add_debug, group, script);
        } else {
            info(group_add_info, group, script.getName());
        }
    }

    void addUserDone(LinuxScript script, Object worker, Object user) {
        if (isTraceEnabled()) {
            trace(user_add_trace, user, script, worker);
        } else if (isDebugEnabled()) {
            debug(user_add_debug, user, script);
        } else {
            info(user_add_info, user, script.getName());
        }
    }

    void linkFilesDone(LinuxScript script, Object worker, Object args) {
        if (isTraceEnabled()) {
            trace(link_files_done_trace, args, script, worker);
        } else if (isDebugEnabled()) {
            debug(link_files_done_debug, args, script);
        } else {
            info(link_files_done_info, args, script.getName());
        }
    }

    void unpackDone(LinuxScript script, Object worker, Object file,
            Object target) {
        if (isTraceEnabled()) {
            trace(unpack_done_trace, file, target, script, worker);
        } else if (isDebugEnabled()) {
            debug(unpack_done_debug, file, target, script);
        } else {
            info(unpack_done_info, file, target, script.getName());
        }
    }

    void checkProperties(LinuxScript script, Object properties, String key) {
        notNull(properties, properties_null.toString(), key, script);
    }

    void checkPropertyKey(LinuxScript script, Object property, Object key) {
        notNull(property, property_not_set.toString(), key, script);
    }

    void checkPropertyFile(LinuxScript script, Object path, Object key) {
        notNull(path, property_file_null.toString(), key, script);
    }

    void checkChangeOwnerArgs(Map<String, Object> args) {
        isTrue(args.containsKey(FILES), chown_args_missing.toString(), FILES);
        isTrue(args.containsKey(COMMAND), chown_args_missing.toString(),
                COMMAND);
        isTrue(args.containsKey(OWNER), chown_args_missing.toString(), OWNER);
        isTrue(args.containsKey(OWNER_GROUP), chown_args_missing.toString(),
                OWNER_GROUP);
        isTrue(args.containsKey(SYSTEM), chown_args_missing.toString(), SYSTEM);
    }

    void checkChangeModArgs(Map<String, Object> args) {
        isTrue(args.containsKey(FILES), chmod_args_missing.toString(), FILES);
        isTrue(args.containsKey(COMMAND), chmod_args_missing.toString(),
                COMMAND);
        isTrue(args.containsKey(MOD), chmod_args_missing.toString(), MOD);
        isTrue(args.containsKey(SYSTEM), chmod_args_missing.toString(), SYSTEM);
    }

    void checkUnpackArgs(Map<String, Object> args) {
        isTrue(args.containsKey(FILE), unpack_args_missing.toString(), FILE);
        isTrue(args.containsKey(TYPE), unpack_args_missing.toString(), TYPE);
        isTrue(args.containsKey(OUTPUT), unpack_args_missing.toString(), OUTPUT);
        isTrue(args.containsKey(SYSTEM), unpack_args_missing.toString(), SYSTEM);
    }

    void checkArchiveTypeArgs(Map<String, Object> args) {
        isTrue(args.containsKey(FILE), archive_type_args_missing.toString(),
                FILE);
    }

    ServiceException unknownArchiveType(LinuxScript script,
            @SuppressWarnings("rawtypes") Map args) {
        return logException(
                new ServiceException(unknown_archive_type).add(script1, script)
                        .add(args1, args), unknown_archive_type_message,
                args.get(FILE), script.getName());
    }

    void checkLinkArgs(Map<String, Object> args) {
        isTrue(args.containsKey(FILES), link_args_missing.toString(), FILES);
        isTrue(args.containsKey(TARGETS), link_args_missing.toString(), TARGETS);
        isTrue(args.containsKey(COMMAND), link_args_missing.toString(), COMMAND);
        isTrue(args.containsKey(SYSTEM), link_args_missing.toString(), SYSTEM);
    }

    void checkAddUserArgs(Map<String, Object> args) {
        isTrue(args.containsKey(SYSTEM), adduser_args_missing.toString(),
                SYSTEM);
        isTrue(args.containsKey(USER_NAME), adduser_args_missing.toString(),
                FILES);
        isTrue(args.containsKey(GROUP_NAME), adduser_args_missing.toString(),
                GROUP_NAME);
        isTrue(args.containsKey(USERS_FILE), adduser_args_missing.toString(),
                USERS_FILE);
        isTrue(args.containsKey(COMMAND), adduser_args_missing.toString(),
                COMMAND);
    }

    void checkAddGroupArgs(Map<String, Object> args) {
        isTrue(args.containsKey(SYSTEM), addgroup_args_missing.toString(),
                SYSTEM);
        isTrue(args.containsKey(GROUPS_FILE), addgroup_args_missing.toString(),
                GROUPS_FILE);
        isTrue(args.containsKey(COMMAND), addgroup_args_missing.toString(),
                COMMAND);
        isTrue(args.containsKey(GROUP_NAME), addgroup_args_missing.toString(),
                GROUP_NAME);
    }

    void checkChangePasswordArgs(Map<String, Object> args) {
        isTrue(args.containsKey(USER_NAME),
                change_password_args_missing.toString(), USER_NAME);
        isTrue(args.containsKey(DISTRBUTION_NAME),
                change_password_args_missing.toString(), DISTRBUTION_NAME);
        isTrue(args.containsKey(PASSWORD),
                change_password_args_missing.toString(), PASSWORD);
        isTrue(args.containsKey(SYSTEM),
                change_password_args_missing.toString(), SYSTEM);
        isTrue(args.containsKey(COMMAND),
                change_password_args_missing.toString(), COMMAND);
    }

    void changePasswordDone(LinuxScript script, Object worker, Object args) {
        if (isTraceEnabled()) {
            trace(change_password_done_trace, args, script, worker);
        } else if (isDebugEnabled()) {
            debug(change_password_done_debug, args, script);
        } else {
            info(change_password_done_info, args, script.getName());
        }
    }

}
