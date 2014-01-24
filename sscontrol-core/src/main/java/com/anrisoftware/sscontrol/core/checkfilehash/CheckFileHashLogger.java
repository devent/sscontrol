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
package com.anrisoftware.sscontrol.core.checkfilehash;

import static com.anrisoftware.sscontrol.core.checkfilehash.CheckFileHashLogger._.file_null;
import static com.anrisoftware.sscontrol.core.checkfilehash.CheckFileHashLogger._.hash_matching;
import static com.anrisoftware.sscontrol.core.checkfilehash.CheckFileHashLogger._.resource_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.resources.ToURIFactory;

/**
 * Logging for {@link CheckFileHash}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CheckFileHashLogger extends AbstractLogger {

    private static final String HASH = "hash";
    private static final String FILE = "file";

    enum _ {

        file_null("File cannot be null for %s."),

        resource_null("Hash resource cannot be null for %s."),

        hash_matching("Hash {} compared to hash {} matches {} for {}.");

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
    private ToURIFactory toURI;

    /**
     * Sets the context of the logger to {@link CheckFileHash}.
     */
    public CheckFileHashLogger() {
        super(CheckFileHash.class);
    }

    URI file(Object script, Map<String, Object> args) {
        Object file = args.get(FILE);
        notNull(file, file_null.toString(), script);
        return toURI.create().convert(file.toString());
    }

    URI hash(Object script, Map<String, Object> args) {
        Object resource = args.get(HASH);
        notNull(resource, resource_null.toString(), script);
        return toURI.create().convert(resource.toString());
    }

    void hashMatching(CheckFileHash check, String expected, String hashstr,
            boolean matching) {
        info(hash_matching, expected, hashstr, matching, check);
    }
}
