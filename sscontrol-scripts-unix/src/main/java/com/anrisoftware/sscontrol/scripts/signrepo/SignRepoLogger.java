/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.scripts.signrepo;

import static com.anrisoftware.sscontrol.scripts.signrepo.SignRepoLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.signrepo.SignRepoLogger._.mod_changed;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link SignRepo}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SignRepoLogger extends AbstractLogger {

    static final String NAME_KEY = "name";
    static final String TMP_KEY = "tmp";
    static final String SYSTEM_KEY = "system";
    static final String COMMAND_KEY = "command";
    static final String KEY_KEY = "key";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        mod_changed("Repository signed with '{}', {}.");

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
     * Sets the context of the logger to {@link SignRepo}.
     */
    public SignRepoLogger() {
        super(SignRepo.class);
    }

    void key(Map<String, Object> args, Object parent) {
        Object object = args.get(KEY_KEY);
        notNull(object, argument_null.toString(), KEY_KEY);
    }

    void command(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMAND_KEY);
        notNull(object, argument_null.toString(), COMMAND_KEY);
    }

    void system(Map<String, Object> args, Object parent) {
        Object object = args.get(SYSTEM_KEY);
        notNull(object, argument_null.toString(), SYSTEM_KEY);
    }

    void tmp(Map<String, Object> args, Object parent) {
        Object object = args.get(TMP_KEY);
        notNull(object, argument_null.toString(), TMP_KEY);
    }

    void name(Map<String, Object> args, Object parent) {
        Object object = args.get(NAME_KEY);
        notNull(object, argument_null.toString(), NAME_KEY);
    }

    void repoSigned(Object parent, Map<String, Object> args) {
        debug(mod_changed, args.get(KEY_KEY), parent);
    }

}
