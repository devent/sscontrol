/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls;

import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.ACCESS_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.API_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.BACKUP_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.CONVERT_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.DEBUG_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.DRIVER_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.GMT_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.LANGUAGE_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.OFFSET_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.PREFIX_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.RESERVED_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.SITE_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.STATS_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.TARGET_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.UNIQUE_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.URLS_KEY;
import static com.anrisoftware.sscontrol.httpd.yourls.YourlsServiceStatement.USER_KEY;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTableFactory;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Yourls</i> service.
 *
 * @see <a href="http://yourls.org/>http://yourls.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class YourlsServiceImpl implements YourlsService {

    /**
     * The <i>Yourls</i> service name.
     */
    public static final String SERVICE_NAME = "yourls";

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    private StatementsTable statementsTable;

    /**
     * @see YourlsServiceFactory#create(Map, Domain)
     */
    @Inject
    YourlsServiceImpl(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(DATABASE_KEY, OVERRIDE_KEY, BACKUP_KEY, ACCESS_KEY,
                USER_KEY, GMT_KEY, UNIQUE_KEY, CONVERT_KEY, RESERVED_KEY,
                SITE_KEY, LANGUAGE_KEY);
        map.setAllowValue(true, DATABASE_KEY, ACCESS_KEY, RESERVED_KEY,
                SITE_KEY, LANGUAGE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PORT_KEY, PREFIX_KEY, DRIVER_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
        map.addAllowedKeys(ACCESS_KEY, STATS_KEY, API_KEY);
        map.addAllowedKeys(GMT_KEY, OFFSET_KEY);
        map.addAllowedKeys(UNIQUE_KEY, URLS_KEY);
        map.addAllowedKeys(CONVERT_KEY, MODE_KEY);
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY, USER_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        table.addAllowedKeys(USER_KEY, PASSWORD_KEY);
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
        map.put(DRIVER_KEY.toString(), m.mapValue(DATABASE_KEY, DRIVER_KEY));
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

    @Override
    public Access getSiteAccess() {
        return statementsMap.value(ACCESS_KEY);
    }

    @Override
    public Access getStatsAccess() {
        return statementsMap.mapValue(ACCESS_KEY, STATS_KEY);
    }

    @Override
    public Access getApiAccess() {
        return statementsMap.mapValue(ACCESS_KEY, API_KEY);
    }

    @Override
    public Integer getGmtOffset() {
        return statementsMap.mapValue(GMT_KEY, OFFSET_KEY);
    }

    @Override
    public Boolean getUniqueUrls() {
        Object value = statementsMap.mapValue(UNIQUE_KEY, URLS_KEY);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        } else {
            return (Boolean) value;
        }
    }

    @Override
    public Convert getUrlConvertMode() {
        return statementsMap.mapValue(CONVERT_KEY, MODE_KEY);
    }

    @Override
    public List<String> getReserved() {
        return statementsMap.valueAsStringList(RESERVED_KEY);
    }

    @Override
    public String getLanguage() {
        return statementsMap.value(LANGUAGE_KEY);
    }

    @Override
    public Map<String, String> getUsers() {
        return statementsTable.tableKeys(USER_KEY, PASSWORD_KEY);
    }

    @Override
    public String getSite() {
        return statementsMap.value(SITE_KEY);
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
