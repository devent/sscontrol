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
package com.anrisoftware.sscontrol.scripts.enableaptrepository;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.write;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.stringtemplate.v4.ST;

import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.sscontrol.scripts.repositoryaptenabled.RepositoryAptEnabledFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Enabled the repository on the distribution.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class EnableAptRepository implements Callable<EnableAptRepository> {

    private static final String REP_FORMAT = "%s\n";

    private static final String REPOSITORY_VAR = "repository";

    private static final String DISTRIBUTION_NAME_VAR = "distributionName";

    private final EnableAptRepositoryLogger log;

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    private final String distributionName;

    private final String repository;

    private final String repositoryString;

    private final File packagesSourcesFile;

    private final Charset charset;

    @Inject
    private RepositoryAptEnabledFactory repositoryEnabledFactory;

    /**
     * @see EnableAptRepositoryFactory#create(Map, Object, Threads)
     */
    @Inject
    EnableAptRepository(EnableAptRepositoryLogger log,
            @Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        this.log = log;
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        this.packagesSourcesFile = log.packagesSourcesFile(args, parent);
        this.distributionName = log.distributionName(args, parent);
        this.repository = log.repository(args, parent);
        this.repositoryString = log.repositoryString(args, parent);
        this.charset = log.charset(args, parent);
    }

    @Override
    public EnableAptRepository call() throws Exception {
        boolean contains = repositoryEnabledFactory
                .create(args, parent, threads).call().getContains();
        if (!contains) {
            String str = repositoryString();
            write(packagesSourcesFile, format(REP_FORMAT, str), charset, true);
            log.enableRepositoryDone(parent, repository);
        }
        return this;
    }

    private String repositoryString() {
        return new ST(repositoryString)
                .add(DISTRIBUTION_NAME_VAR, distributionName)
                .add(REPOSITORY_VAR, repository).render();
    }

}
