/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum;

import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.BACKUP_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.DEBUG_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.DRIVER_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.EMAIL_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.OVERRIDE_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.PREFIX_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.ROOT_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.SITE_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.TARGET_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.TEMPLATE_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.TYPE_KEY;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceStatement.USER_KEY;

import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.posixlocale.PosixLocale;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.languagestatements.LanguageStatements;
import com.anrisoftware.sscontrol.core.groovy.languagestatements.LanguageStatementsFactory;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTableFactory;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>FUDForum</i> service.
 *
 * @see <a href="http://fudforum.org>http://fudforum.org</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FudforumServiceImpl implements FudforumService {

    /**
     * The <i>FUDForum</i> service name.
     */
    public static final String SERVICE_NAME = "fudforum";

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    @Inject
    private FudforumServiceImplLogger log;

    private StatementsTable statementsTable;

    private LanguageStatements languageStatements;

    /**
     * @see FudforumServiceFactory#create(Map, Domain)
     */
    @Inject
    FudforumServiceImpl(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(DATABASE_KEY, OVERRIDE_KEY, BACKUP_KEY, TEMPLATE_KEY,
                ROOT_KEY, SITE_KEY);
        map.setAllowValue(true, DATABASE_KEY, TEMPLATE_KEY, ROOT_KEY, SITE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PORT_KEY, PREFIX_KEY, TYPE_KEY, DRIVER_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
        map.addAllowedKeys(ROOT_KEY, PASSWORD_KEY, EMAIL_KEY);
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        this.statementsTable = table;
    }

    @Inject
    public final void setLanguageStatements(LanguageStatementsFactory factory) {
        LanguageStatements language = factory.create(this, SERVICE_NAME);
        this.languageStatements = language;
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
        map.put(TYPE_KEY.toString(), m.mapValue(DATABASE_KEY, TYPE_KEY));
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
    public String getSite() {
        return statementsMap.value(SITE_KEY);
    }

    @Override
    public Locale getLanguage() throws ServiceException {
        try {
            return languageStatements.getLanguage();
        } catch (ParseException e) {
            throw log.errorParseLanguage(this, e);
        }
    }

    @Override
    public List<PosixLocale> getLocales() throws ServiceException {
        try {
            return languageStatements.getLocales();
        } catch (ParseException e) {
            throw log.errorParseLocales(this, e);
        }
    }

    @Override
    public String getTemplate() {
        return statementsMap.value(TEMPLATE_KEY);
    }

    @Override
    public String getRootLogin() {
        return statementsMap.value(ROOT_KEY);
    }

    @Override
    public String getRootPassword() {
        return statementsMap.mapValue(ROOT_KEY, PASSWORD_KEY);
    }

    @Override
    public String getRootEmail() {
        return statementsMap.mapValue(ROOT_KEY, EMAIL_KEY);
    }

    public Object methodMissing(String name, Object args) {
        try {
            return service.methodMissing(name, args);
        } catch (StatementsException e1) {
            try {
                return statementsTable.methodMissing(name, args);
            } catch (StatementsException e2) {
                return languageStatements.methodMissing(name, args);
            }
        }
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
