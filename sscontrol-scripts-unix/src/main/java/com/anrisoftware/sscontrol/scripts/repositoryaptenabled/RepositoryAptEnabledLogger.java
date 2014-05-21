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
package com.anrisoftware.sscontrol.scripts.repositoryaptenabled;

import static com.anrisoftware.sscontrol.scripts.repositoryaptenabled.RepositoryAptEnabledLogger._.argument_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link RepositoryAptEnabled}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RepositoryAptEnabledLogger extends AbstractLogger {

    private static final String REPOSITORY_KEY = "repository";

    private static final String PACKAGES_SOURCES_FILE_KEY = "packagesSourcesFile";

    private static final String DISTRIBUTION_NAME_KEY = "distributionName";

    enum _ {

        argument_null("Argument '%s' cannot be null.");

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
     * Sets the context of the logger to {@link RepositoryAptEnabled}.
     */
    public RepositoryAptEnabledLogger() {
        super(RepositoryAptEnabled.class);
    }

    String distributionName(Map<String, Object> args, Object parent) {
        Object value = args.get(DISTRIBUTION_NAME_KEY);
        notNull(value, argument_null.toString(), DISTRIBUTION_NAME_KEY);
        return value.toString();
    }

    File packagesSourcesFile(Map<String, Object> args, Object parent) {
        Object value = args.get(PACKAGES_SOURCES_FILE_KEY);
        notNull(value, argument_null.toString(), PACKAGES_SOURCES_FILE_KEY);
        return (File) value;
    }

    String repository(Map<String, Object> args, Object parent) {
        Object value = args.get(REPOSITORY_KEY);
        notNull(value, argument_null.toString(), REPOSITORY_KEY);
        return value.toString();
    }
}
