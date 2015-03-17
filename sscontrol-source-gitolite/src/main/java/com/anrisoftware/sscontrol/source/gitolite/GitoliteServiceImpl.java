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

import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.ADMIN_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.DATA_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.GID_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.GROUP_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.KEY_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.PATH_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.PREFIX_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.UID_KEY;
import static com.anrisoftware.sscontrol.source.gitolite.GitoliteServiceStatement.USER_KEY;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;

/**
 * <i>Gitolite</i> service.
 *
 * @see <a href="http://gitolite.com/">http://gitolite.com/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class GitoliteServiceImpl implements GitoliteService {

    private static final String NAME = "service name";

    /**
     * The <i>Gitolite</i> service name.
     */
    public static final String SERVICE_NAME = "gitolite";

    private StatementsMap statementsMap;

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, SERVICE_NAME);
        map.addAllowed(ADMIN_KEY, USER_KEY, OVERRIDE_KEY, PREFIX_KEY, DATA_KEY);
        map.addAllowedKeys(ADMIN_KEY, KEY_KEY);
        map.setAllowValue(true, USER_KEY);
        map.addAllowedKeys(USER_KEY, GROUP_KEY, UID_KEY, GID_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(PREFIX_KEY, PATH_KEY);
        map.addAllowedKeys(DATA_KEY, PATH_KEY);
        this.statementsMap = map;
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public OverrideMode getOverrideMode() {
        return statementsMap.mapValue(OVERRIDE_KEY, MODE_KEY);
    }

    @Override
    public URI getAdminKey() {
        return statementsMap.mapValueAsURI(ADMIN_KEY, KEY_KEY);
    }

    @Override
    public String getPrefix() {
        return statementsMap.mapValue(PREFIX_KEY, PATH_KEY);
    }

    @Override
    public String getDataPath() {
        return statementsMap.mapValue(DATA_KEY, PATH_KEY);
    }

    @Override
    public Map<String, Object> getUser() {
        @SuppressWarnings("serial")
        Map<String, Object> map = new HashMap<String, Object>() {
            @Override
            public Object put(String key, Object value) {
                if (value != null) {
                    return super.put(key, value);
                } else {
                    return null;
                }
            }
        };
        StatementsMap m = statementsMap;
        map.put(USER_KEY.toString(), m.value(USER_KEY));
        map.put(GROUP_KEY.toString(), m.mapValue(USER_KEY, GROUP_KEY));
        map.put(UID_KEY.toString(), m.mapValue(USER_KEY, UID_KEY));
        map.put(GID_KEY.toString(), m.mapValue(USER_KEY, GID_KEY));
        return map.size() == 0 ? null : map;
    }

    public Object methodMissing(String name, Object args) {
        return statementsMap.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME).toString();
    }
}
