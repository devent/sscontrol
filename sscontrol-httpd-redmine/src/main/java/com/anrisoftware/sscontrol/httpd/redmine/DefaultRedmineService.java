/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine;

import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.AUTH_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.BACKEND_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.BACKUP_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.DEBUG_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.DOMAIN_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.ENCODING_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.INSTALL_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.LANGUAGE_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.MAIL_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.METHOD_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.NAME_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.OPENSSL_VERIFY_MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.PROVIDER_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.SCM_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.SCRIPT_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.SSL_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.START_TLS_AUTO_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.TARGET_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.TRACKING_KEY;
import static com.anrisoftware.sscontrol.httpd.redmine.RedmineServiceStatement.USER_KEY;

import java.net.URI;
import java.util.ArrayList;
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
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;

/**
 * <i>Redmine</i> service.
 *
 * @see <a href="http://www.redmine.org/">http://www.redmine.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class DefaultRedmineService implements RedmineService {

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    private final String serviceName;

    private StatementsTable statementsTable;

    public DefaultRedmineService(DefaultWebServiceFactory webServiceFactory,
            Map<String, Object> args, Domain domain, String serviceName) {
        this.serviceName = serviceName;
        this.service = webServiceFactory.create(serviceName, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(BACKEND_KEY, DATABASE_KEY, MAIL_KEY, LANGUAGE_KEY,
                SCM_KEY, OVERRIDE_KEY, BACKUP_KEY, TRACKING_KEY);
        map.setAllowValue(true, BACKEND_KEY, DATABASE_KEY, MAIL_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PROVIDER_KEY, ENCODING_KEY);
        map.addAllowedKeys(MAIL_KEY, PORT_KEY, METHOD_KEY, DOMAIN_KEY,
                AUTH_KEY, USER_KEY, PASSWORD_KEY, SSL_KEY, START_TLS_AUTO_KEY,
                OPENSSL_VERIFY_MODE_KEY);
        map.addAllowedKeys(LANGUAGE_KEY, NAME_KEY);
        map.addAllowedKeys(SCM_KEY, INSTALL_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
        map.addAllowedKeys(TRACKING_KEY, SCRIPT_KEY);
        map.putValue(BACKEND_KEY.toString(), args.get(BACKEND_KEY.toString()));
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
    public String getBackend() {
        return statementsMap.value(BACKEND_KEY);
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
        map.put(PROVIDER_KEY.toString(), m.mapValue(DATABASE_KEY, PROVIDER_KEY));
        map.put(ENCODING_KEY.toString(), m.mapValue(DATABASE_KEY, ENCODING_KEY));
        return map.size() == 0 ? null : map;
    }

    @Override
    public Map<String, Object> getMail() {
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
        map.put(HOST_KEY.toString(), m.value(MAIL_KEY));
        map.put(PORT_KEY.toString(), m.mapValue(MAIL_KEY, PORT_KEY));
        map.put(METHOD_KEY.toString(), m.mapValue(MAIL_KEY, METHOD_KEY));
        map.put(DOMAIN_KEY.toString(), m.mapValue(MAIL_KEY, DOMAIN_KEY));
        map.put(AUTH_KEY.toString(), m.mapValue(MAIL_KEY, AUTH_KEY));
        map.put(USER_KEY.toString(), m.mapValue(MAIL_KEY, USER_KEY));
        map.put(PASSWORD_KEY.toString(), m.mapValue(MAIL_KEY, PASSWORD_KEY));
        map.put(SSL_KEY.toString(), m.mapValue(MAIL_KEY, SSL_KEY));
        map.put(START_TLS_AUTO_KEY.toString(),
                m.mapValue(MAIL_KEY, START_TLS_AUTO_KEY));
        map.put(OPENSSL_VERIFY_MODE_KEY.toString(),
                m.mapValue(MAIL_KEY, OPENSSL_VERIFY_MODE_KEY));
        return map.size() == 0 ? null : map;
    }

    @Override
    public String getLanguageName() {
        return statementsMap.mapValue(LANGUAGE_KEY, NAME_KEY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ScmInstall> getScms() {
        Object v = statementsMap.mapValue(SCM_KEY, INSTALL_KEY);
        if (v == null) {
            return null;
        }
        if (v instanceof List) {
            return (List<ScmInstall>) v;
        } else {
            List<ScmInstall> list = new ArrayList<ScmInstall>();
            list.add((ScmInstall) v);
            return list;
        }
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
    public URI getTrackingScript() {
        return statementsMap.mapValueAsURI(TRACKING_KEY, SCRIPT_KEY);
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
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
