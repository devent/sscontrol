/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress;

import static com.anrisoftware.sscontrol.core.overridemode.OverrideMode.no;
import static com.anrisoftware.sscontrol.core.overridemode.OverrideMode.override;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.ADMIN_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.BACKUP_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.CACHE_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.CHARSET_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.COLLATE_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.DEBUG_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.ENABLED_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.FORCE_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.LOGIN_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.MULTISITE_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.PLUGINS_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.PLUGIN_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.PREFIX_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.SCHEMA_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.SETUP_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.TARGET_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.THEMES_KEY;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceStatement.USER_KEY;

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

/**
 * <i>Wordpress</i> service.
 *
 * @see <a href='http://wordpress.org/">http://wordpress.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class DefaultWordpressService implements WordpressService {

    private final Domain domain;

    private final StatementsMap statementsMap;

    private final DefaultWebService service;

    private final String serviceName;

    private StatementsTable statementsTable;

    protected DefaultWordpressService(String serviceName,
            DefaultWebServiceFactory webServiceFactory,
            Map<String, Object> args, Domain domain) {
        this.service = webServiceFactory.create(serviceName, args, domain);
        this.statementsMap = service.getStatementsMap();
        this.domain = domain;
        this.serviceName = serviceName;
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(BACKUP_KEY, OVERRIDE_KEY, DATABASE_KEY, FORCE_KEY,
                PLUGINS_KEY, THEMES_KEY, MULTISITE_KEY, CACHE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PORT_KEY, PREFIX_KEY, CHARSET_KEY, COLLATE_KEY, SCHEMA_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(FORCE_KEY, LOGIN_KEY, ADMIN_KEY);
        map.addAllowedKeys(MULTISITE_KEY, SETUP_KEY);
        map.addAllowedKeys(CACHE_KEY, ENABLED_KEY, PLUGIN_KEY);
        map.setAllowValue(DATABASE_KEY, true);
        map.setAllowValue(PLUGINS_KEY, true);
        map.setAllowValue(THEMES_KEY, true);
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
        return domain;
    }

    @Override
    public String getName() {
        return serviceName;
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
        map.put(CHARSET_KEY.toString(), m.mapValue(DATABASE_KEY, CHARSET_KEY));
        map.put(COLLATE_KEY.toString(), m.mapValue(DATABASE_KEY, COLLATE_KEY));
        map.put(SCHEMA_KEY.toString(), m.mapValue(DATABASE_KEY, SCHEMA_KEY));
        return map.size() == 0 ? null : map;
    }

    public void setMultiSite(MultiSite multiSite) throws ServiceException {
        statementsMap.putMapValue(MULTISITE_KEY.toString(),
                SETUP_KEY.toString(), multiSite);
    }

    @Override
    public MultiSite getMultiSite() {
        Object o = statementsMap.mapValue(MULTISITE_KEY, SETUP_KEY);
        if (o instanceof MultiSite) {
            return (MultiSite) o;
        } else {
            String name = statementsMap.mapValue(MULTISITE_KEY, SETUP_KEY);
            return name == null ? null : MultiSite.parse(name);
        }
    }

    public void setThemes(List<String> themes) throws ServiceException {
        statementsMap.putValue(THEMES_KEY.toString(), themes);
    }

    @Override
    public List<String> getThemes() {
        return statementsMap.valueAsStringList(THEMES_KEY);
    }

    public void setPlugins(List<String> plugins) throws ServiceException {
        statementsMap.putValue(PLUGINS_KEY.toString(), plugins);
    }

    @Override
    public List<String> getPlugins() {
        return statementsMap.valueAsStringList(PLUGINS_KEY);
    }

    public void setForceSslLogin(boolean force) {
        statementsMap.putMapValue(FORCE_KEY.toString(), LOGIN_KEY.toString(),
                force);
    }

    @Override
    public Boolean getForceSslLogin() {
        Object value = statementsMap.mapValue(FORCE_KEY, LOGIN_KEY);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    public void setForceSslAdmin(boolean force) {
        statementsMap.putMapValue(FORCE_KEY.toString(), ADMIN_KEY.toString(),
                force);
    }

    @Override
    public Boolean getForceSslAdmin() {
        Object value = statementsMap.mapValue(FORCE_KEY, ADMIN_KEY);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    public void setOverrideMode(OverrideMode mode) {
        statementsMap.putMapValue(OVERRIDE_KEY.toString(), MODE_KEY.toString(),
                mode);
    }

    @Override
    public OverrideMode getOverrideMode() {
        Object value = statementsMap.mapValue(OVERRIDE_KEY, MODE_KEY);
        if (value instanceof OverrideMode) {
            return (OverrideMode) value;
        }
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean() ? override : no;
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? override : no;
        }
        return null;
    }

    @Override
    public Boolean getCacheEnabled() {
        Object value = statementsMap.mapValue(CACHE_KEY, ENABLED_KEY);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    @Override
    public String getCachePlugin() {
        return statementsMap.mapValue(CACHE_KEY, PLUGIN_KEY);
    }

    public void setBackupTarget(URI uri) throws ServiceException {
        statementsMap.putMapValue(BACKUP_KEY.toString(), TARGET_KEY.toString(),
                uri);
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
