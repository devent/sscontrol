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

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.google.inject.assistedinject.Assisted;

/**
 * Tests if the APT repository is already enabled on the distribution.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RepositoryAptEnabled implements Callable<RepositoryAptEnabled> {

    private static final String SEARCH_PATTERN = ".*(%s)\\s.*(%s)";

    private static final String NEW_LINE_CHAR = System
            .getProperty("line.separator");

    private static final String PACKAGES_SOURCES_DIR_KEY = "packagesSourcesDir";

    private final Map<String, Object> args;

    private final Threads threads;

    private final String distributionName;

    private final String repository;

    @Inject
    private RepositoryEnabledProcessFactory containsRepositoryProcessFactory;

    private boolean contains;

    /**
     * @see RepositoryAptEnabledFactory#create(Map, Object, Threads)
     */
    @Inject
    RepositoryAptEnabled(RepositoryAptEnabledLogger log,
            @Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        this.args = args;
        this.threads = threads;
        this.distributionName = log.distributionName(args, parent);
        args.put(PACKAGES_SOURCES_DIR_KEY, log
                .packagesSourcesFile(args, parent).getParentFile());
        this.repository = log.repository(args, parent);
        this.contains = false;
    }

    @Override
    public RepositoryAptEnabled call() throws Exception {
        ProcessTask task;
        task = containsRepositoryProcessFactory.create(threads, args).call();
        String out = task.getOut();
        String[] split = split(out, NEW_LINE_CHAR);
        boolean found = false;
        for (String line : split) {
            line = line.trim();
            if (matches(line)) {
                found = true;
                break;
            }
        }
        this.contains = found;
        return this;
    }

    private boolean matches(String line) {
        return line
                .matches(format(SEARCH_PATTERN, distributionName, repository));
    }

    public boolean getContains() {
        return contains;
    }
}
