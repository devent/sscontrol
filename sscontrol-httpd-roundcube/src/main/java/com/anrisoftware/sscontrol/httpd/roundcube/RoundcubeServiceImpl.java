/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube;

import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.BACKUP_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.DEBUG_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.DOMAIN_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.DRIVER_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.MAIL_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.NAME_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.PLUGINS_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.PRODUCT_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.SERVER_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.TARGET_KEY;
import static com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceStatement.USER_KEY;
import static com.anrisoftware.sscontrol.httpd.webservice.OverrideMode.no;
import static com.anrisoftware.sscontrol.httpd.webservice.OverrideMode.override;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Roundcube</i> service.
 *
 * @see <a href="http://roundcube.net/">http://roundcube.net/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RoundcubeServiceImpl implements RoundcubeService {

    private static final String NAME = "name";

    private static final String ALIAS_KEY = "alias";

    public static final String SERVICE_NAME = "roundcube";

    private final Domain domain;

    private final StatementsMap statementsMap;

    private final DefaultWebService service;

    private StatementsTable statementsTable;

    /**
     * @see RoundcubeServiceFactory#create(Map, Domain)
     */
    @Inject
    RoundcubeServiceImpl(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        this.domain = domain;
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(OVERRIDE_KEY, BACKUP_KEY, DATABASE_KEY, MAIL_KEY,
                PLUGINS_KEY, PRODUCT_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PORT_KEY, DRIVER_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
        map.addAllowedKeys(MAIL_KEY, USER_KEY, PASSWORD_KEY);
        map.addAllowedKeys(PRODUCT_KEY, NAME_KEY);
        map.setAllowValue(true, DATABASE_KEY, MAIL_KEY, PLUGINS_KEY);
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(factory, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY, SERVER_KEY, HOST_KEY);
        table.addAllowedKeys(SERVER_KEY, HOST_KEY, PORT_KEY);
        table.addAllowedKeys(HOST_KEY, DOMAIN_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        this.statementsTable = table;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    public StatementsMap getStatementsMap() {
        return service.getStatementsMap();
    }

    public void setAlias(String alias) throws ServiceException {
        service.setAlias(alias);
    }

    @Override
    public String getAlias() {
        return service.getAlias();
    }

    public void setId(String id) throws ServiceException {
        service.setId(id);
    }

    @Override
    public String getId() {
        return service.getId();
    }

    public void setRef(String ref) throws ServiceException {
        service.setRef(ref);
    }

    @Override
    public String getRef() {
        return service.getRef();
    }

    public void setRefDomain(String ref) throws ServiceException {
        service.setRefDomain(ref);
    }

    @Override
    public String getRefDomain() {
        return service.getRefDomain();
    }

    public void setPrefix(String prefix) throws ServiceException {
        service.setPrefix(prefix);
    }

    @Override
    public String getPrefix() {
        return service.getPrefix();
    }

    @Override
    public Map<String, Object> debugLogging(String key) {
        return statementsTable.tableKeys(DEBUG_KEY, key);
    }

    @Override
    public OverrideMode getOverrideMode() {
        Object value = statementsMap.mapValue(OVERRIDE_KEY, MODE_KEY);
        if (value instanceof Boolean) {
            return ((Boolean) value) ? override : no;
        }
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value) == YesNoFlag.yes ? override : no;
        }
        if (value instanceof OverrideMode) {
            return (OverrideMode) value;
        }
        return null;
    }

    @Override
    public URI getBackupTarget() {
        return statementsMap.mapValueAsURI(BACKUP_KEY, TARGET_KEY);
    }

    @Override
    public String getProductName() {
        return statementsMap.mapValue(PRODUCT_KEY, NAME_KEY);
    }

    @Override
    public Map<String, Object> getDatabase() {
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
        map.put(DATABASE_KEY.toString(), m.value(DATABASE_KEY));
        map.put(USER_KEY.toString(), m.mapValue(DATABASE_KEY, USER_KEY));
        map.put(PASSWORD_KEY.toString(), m.mapValue(DATABASE_KEY, PASSWORD_KEY));
        map.put(HOST_KEY.toString(), m.mapValue(DATABASE_KEY, HOST_KEY));
        map.put(DRIVER_KEY.toString(), m.mapValue(DATABASE_KEY, DRIVER_KEY));
        return map.size() == 0 ? null : map;
    }

    @Override
    public Map<String, Object> getMailServer() {
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
        map.put(MAIL_KEY.toString(), m.value(MAIL_KEY));
        map.put(USER_KEY.toString(), m.mapValue(MAIL_KEY, USER_KEY));
        map.put(PASSWORD_KEY.toString(), m.mapValue(MAIL_KEY, PASSWORD_KEY));
        return map.size() == 0 ? null : map;
    }

    @Override
    public Map<String, String> getImapServers() {
        return statementsTable.tableKeys(SERVER_KEY, HOST_KEY);
    }

    @Override
    public String getImapServer() {
        StatementsTable tmap = statementsTable;
        Map<String, Object> map = tmap.tableKeys(SERVER_KEY, HOST_KEY);
        if (map != null) {
            return (String) map.get("default");
        } else {
            return null;
        }
    }

    @Override
    public Integer getImapPort() {
        StatementsTable tmap = statementsTable;
        Map<String, Object> map = tmap.tableKeys(SERVER_KEY, PORT_KEY);
        if (map != null) {
            return ((Number) map.get("default")).intValue();
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> getImapDomains() {
        return statementsTable.tableKeys(HOST_KEY, DOMAIN_KEY);
    }

    @Override
    public String getImapDomain() {
        Set<String> values = statementsTable.tableValues(HOST_KEY);
        if (values != null) {
            return new ArrayList<String>(values).get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<String> getPlugins() {
        return statementsMap.valueAsStringList(PLUGINS_KEY);
    }

    public Object methodMissing(String name, Object args)
            throws StatementsException {
        try {
            return service.methodMissing(name, args);
        } catch (StatementsException e) {
            return statementsTable.methodMissing(name, args);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME)
                .append(ALIAS_KEY, getAlias()).toString();
    }
}
