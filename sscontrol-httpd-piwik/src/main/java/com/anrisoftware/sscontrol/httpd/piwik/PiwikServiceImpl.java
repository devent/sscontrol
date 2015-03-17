/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik;

import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.ADAPTER_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.BACKUP_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.DEBUG_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.PREFIX_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.SCHEMA_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.TARGET_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.TYPE_KEY;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceStatement.USER_KEY;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Piwik</i> service.
 *
 * @see <a href="http://piwik.org/">http://piwik.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PiwikServiceImpl implements PiwikService {

    /**
     * The <i>Piwik</i> service name.
     */
    public static final String SERVICE_NAME = "piwik";

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    private StatementsTable statementsTable;

    /**
     * @see PiwikServiceFactory#create(Map, Domain)
     */
    @Inject
    PiwikServiceImpl(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(DATABASE_KEY, OVERRIDE_KEY, BACKUP_KEY);
        map.setAllowValue(true, DATABASE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PORT_KEY, PREFIX_KEY, ADAPTER_KEY, TYPE_KEY, SCHEMA_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        this.statementsTable = table;
    }

    @Override
    public Domain getDomain() {
        return service.getDomain();
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
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
        map.put(PORT_KEY.toString(), m.mapValue(DATABASE_KEY, PORT_KEY));
        map.put(PREFIX_KEY.toString(), m.mapValue(DATABASE_KEY, PREFIX_KEY));
        map.put(ADAPTER_KEY.toString(), m.mapValue(DATABASE_KEY, ADAPTER_KEY));
        map.put(TYPE_KEY.toString(), m.mapValue(DATABASE_KEY, TYPE_KEY));
        map.put(SCHEMA_KEY.toString(), m.mapValue(DATABASE_KEY, SCHEMA_KEY));
        return map.size() == 0 ? null : map;
    }

    @Override
    public OverrideMode getOverrideMode() {
        return statementsMap.mapValue(OVERRIDE_KEY, MODE_KEY);
    }

    @Override
    public URI getBackupTarget() {
        return statementsMap.mapValueAsURI(BACKUP_KEY, TARGET_KEY);
    }

    public Object methodMissing(String name, Object args) {
        try {
            return service.methodMissing(name, args);
        } catch (StatementsException e) {
            return statementsTable.methodMissing(name, args);
        }
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
