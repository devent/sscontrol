/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.versionlimits;

import com.anrisoftware.globalpom.version.Version;
import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create the version checker.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CheckVersionLimitFactory {

    /**
     * Creates the version checker.
     *
     * @param currentVersion
     *            the current {@link Version} version.
     *
     * @param archiveVersion
     *            the archive {@link Version} version.
     *
     * @param versionLimit
     *            the {@link Version} version limit.
     *
     * @return the {@link CheckVersionLimit}.
     */
    CheckVersionLimit create(
            @Assisted("currentVersion") Version currentVersion,
            @Assisted("archiveVersion") Version archiveVersion,
            @Assisted("versionLimit") Version versionLimit);
}
