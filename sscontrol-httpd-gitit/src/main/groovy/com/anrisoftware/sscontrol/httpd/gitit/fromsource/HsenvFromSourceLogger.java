/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.gitit.fromsource;

import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.compare_gitit_versions_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.compare_gitit_versions_info;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_gitit_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_gitit_info;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_gitit_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_hsenv_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_hsenv_info;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_packages_done_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.unpack_archive_info;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.globalpom.version.VersionFormat;
import com.anrisoftware.globalpom.version.VersionFormatFactory;

/**
 * Logging for {@link HsenvFromSource}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HsenvFromSourceLogger extends AbstractLogger {

    enum _ {

        install_packages_done_trace("Install packages {} done for {}, {}."),

        install_packages_done_debug("Install packages {} done for {}."),

        install_packages_done_info("Install packages {} done for service '{}'."),

        unpack_archive_debug("Unpack archive '{}' done for {}."),

        unpack_archive_info("Unpack Gitit archive '{}' done for service '{}'."),

        install_hsenv_debug("Install hsenv packages {} done for {}."),

        install_hsenv_info("Install hsenv packages {} done for service '{}'."),

        install_gitit_trace("Install Gitit done for {}, {}."),

        install_gitit_debug("Install Gitit done for {}."),

        install_gitit_info("Install Gitit done for service '{}'."),

        compare_gitit_versions_info(
                "Compare installed Gitit version {} <= archive version {} <= version limit {} for script '{}'."),

        compare_gitit_versions_debug(
                "Compare Gitit versions current {}, archive {}, limit {} for {}.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Inject
    private VersionFormatFactory versionFormatFactory;

    /**
     * Sets the context of the logger to {@link HsenvFromSource}.
     */
    public HsenvFromSourceLogger() {
        super(HsenvFromSource.class);
    }

    void installHsenvCabalPackagesDone(HsenvFromSource config,
            ProcessTask task, Object packages) {
        if (isTraceEnabled()) {
            trace(install_packages_done_trace, packages, config, task);
        } else if (isDebugEnabled()) {
            debug(install_packages_done_debug, packages, config);
        } else {
            info(install_packages_done_info, packages, config.getName());
        }
    }

    void unpackArchiveDone(HsenvFromSource config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getName());
        }
    }

    @SuppressWarnings("rawtypes")
    void installHsenvDone(HsenvFromSource config, List packages) {
        if (isDebugEnabled()) {
            debug(install_hsenv_debug, packages, config);
        } else {
            info(install_hsenv_info, packages, config.getName());
        }
    }

    void installGititDone(HsenvFromSource config, ProcessTask task) {
        if (isTraceEnabled()) {
            trace(install_gitit_trace, config, task);
        } else if (isDebugEnabled()) {
            debug(install_gitit_debug, config);
        } else {
            info(install_gitit_info, config.getName());
        }
    }

    void checkGititVersion(HsenvFromSource config, Version currentVersion,
            Version archiveVersion, Version versionLimit) {
        if (isDebugEnabled()) {
            debug(compare_gitit_versions_debug, currentVersion, archiveVersion,
                    versionLimit, config);
        } else {
            VersionFormat format = versionFormatFactory.create();
            info(compare_gitit_versions_info, format.format(currentVersion),
                    format.format(archiveVersion), format.format(versionLimit),
                    config.getName());
        }
    }
}
