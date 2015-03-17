/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud;

import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.ADAPTER_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.BACKUP_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.DEBUG_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.PREFIX_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.TARGET_KEY;
import static com.anrisoftware.sscontrol.httpd.owncloud.OwncloudServiceStatement.USER_KEY;

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

/**
 * <i>ownCloud</i> service.
 *
 * @see <a href="http://owncloud.org/">http://owncloud.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class DefaultOwncloudService implements OwncloudService {

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    private final String serviceName;

    private StatementsTable statementsTable;

    protected DefaultOwncloudService(String serviceName,
            DefaultWebServiceFactory webServiceFactory,
            Map<String, Object> args, Domain domain) {
        this.service = webServiceFactory.create(serviceName, args, domain);
        this.statementsMap = service.getStatementsMap();
        this.serviceName = serviceName;
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(DATABASE_KEY, OVERRIDE_KEY, BACKUP_KEY);
        map.setAllowValue(true, DATABASE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PORT_KEY, PREFIX_KEY, ADAPTER_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, serviceName);
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
        return serviceName;
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
