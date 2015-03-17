/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * <i>Gitolite</i> service statement.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum GitoliteServiceStatement {

    OVERRIDE_KEY,

    MODE_KEY,

    USER_KEY,

    GROUP_KEY,

    UID_KEY,

    GID_KEY,

    PREFIX_KEY,

    DATA_KEY,

    PATH_KEY,

    ADMIN_KEY,

    KEY_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }
}
