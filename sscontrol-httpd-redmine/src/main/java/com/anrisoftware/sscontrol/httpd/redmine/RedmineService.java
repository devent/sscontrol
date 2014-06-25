/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.database.DatabaseArgs;
import com.anrisoftware.sscontrol.core.database.DatabaseFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Redmine</i> service.
 * 
 * @see <a href="http://www.redmine.org/">http://www.redmine.org/</a>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RedmineService implements WebService {

    private static final String INSTALL_KEY = "install";

    private static final String SCM_KEY = "scm";

    private static final String NAME_KEY = "name";

    private static final String LANGUAGE_KEY = "language";

    private static final String PASSWORD_KEY = "password";

    private static final String USER_KEY = "user";

    private static final String AUTH_KEY = "auth";

    private static final String DOMAIN_KEY = "domain";

    private static final String METHOD_KEY = "method";

    private static final String PORT_KEY = "port";

    private static final String HOST_KEY = "host";

    private static final String MAIL_KEY = "mail";

    /**
     * The <i>Redmine</i> service name.
     */
    public static final String SERVICE_NAME = "redmine";

    private final DefaultWebService service;

    private final RedmineServiceLogger log;

    private final String backend;

    private final StatementsMap statementsMap;

    @Inject
    private DebugLoggingFactory debugFactory;

    @Inject
    private DatabaseFactory databaseFactory;

    private DebugLogging debug;

    private OverrideMode overrideMode;

    private Database database;

    /**
     * @see RedmineServiceFactory#create(Map, Domain)
     */
    @Inject
    RedmineService(RedmineServiceLogger log,
            DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.log = log;
        this.backend = log.backend(this, args);
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(statementsMap);
    }

    private void setupStatements(StatementsMap map) {
        map.addAllowed(MAIL_KEY);
        map.addAllowed(LANGUAGE_KEY);
        map.addAllowed(SCM_KEY);
    }

    @Override
    public Domain getDomain() {
        return service.getDomain();
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    public void setAlias(String alias) {
        service.setAlias(alias);
    }

    @Override
    public String getAlias() {
        return service.getAlias();
    }

    public void setId(String id) {
        service.setId(id);
    }

    @Override
    public String getId() {
        return service.getId();
    }

    public void setRef(String ref) {
        service.setRef(ref);
    }

    @Override
    public String getRef() {
        return service.getRef();
    }

    public void setRefDomain(String ref) {
        service.setRefDomain(ref);
    }

    @Override
    public String getRefDomain() {
        return service.getRefDomain();
    }

    public void setPrefix(String prefix) {
        service.setPrefix(prefix);
    }

    @Override
    public String getPrefix() {
        return service.getPrefix();
    }

    public String getBackend() {
        return backend;
    }

    public void database(Map<String, Object> args, String name) {
        args.put(DatabaseArgs.DATABASE, name);
        Database database = databaseFactory.create(this, args);
        log.databaseSet(this, database);
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public void debug(boolean enabled) {
        DebugLogging logging = debugFactory.create(enabled ? 1 : 0);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public void debug(Map<String, Object> args) {
        DebugLogging logging = debugFactory.create(args);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public DebugLogging getDebugLogging() {
        return debug;
    }

    public void override(Map<String, Object> args) {
        OverrideMode mode = log.override(this, args);
        log.overrideModeSet(this, mode);
        this.overrideMode = mode;
    }

    public void setOverrideMode(OverrideMode mode) {
        this.overrideMode = mode;
    }

    public OverrideMode getOverrideMode() {
        return overrideMode;
    }

    public void setMailHost(String host) {
        statementsMap.putMapValue(MAIL_KEY, HOST_KEY, host);
    }

    public String getMailHost() {
        return statementsMap.mapValue(MAIL_KEY, HOST_KEY);
    }

    public void setMailPort(int port) {
        statementsMap.putMapValue(MAIL_KEY, PORT_KEY, port);
    }

    public Integer getMailPort() {
        return statementsMap.mapValue(MAIL_KEY, PORT_KEY);
    }

    public void setMailDeliveryMethod(DeliveryMethod method) {
        statementsMap.putMapValue(MAIL_KEY, METHOD_KEY, method);
    }

    public DeliveryMethod getMailDeliveryMethod() {
        return statementsMap.mapValue(MAIL_KEY, METHOD_KEY);
    }

    public void setMailDomain(String domain) {
        statementsMap.putMapValue(MAIL_KEY, DOMAIN_KEY, domain);
    }

    public String getMailDomain() {
        return statementsMap.mapValue(MAIL_KEY, DOMAIN_KEY);
    }

    public void setMailAuthMethod(AuthenticationMethod method) {
        statementsMap.putMapValue(MAIL_KEY, AUTH_KEY, method);
    }

    public AuthenticationMethod getMailAuthMethod() {
        return statementsMap.mapValue(MAIL_KEY, AUTH_KEY);
    }

    public String getMailUser() {
        return statementsMap.mapValue(MAIL_KEY, USER_KEY);
    }

    public String getMailPassword() {
        return statementsMap.mapValue(MAIL_KEY, PASSWORD_KEY);
    }

    public void setLanguageName(String name) {
        statementsMap.putMapValue(LANGUAGE_KEY, NAME_KEY, name);
    }

    public String getLanguageName() {
        return statementsMap.mapValue(LANGUAGE_KEY, NAME_KEY);
    }

    public void setScmInstall(List<ScmInstall> list) {
        statementsMap.putMapValue(SCM_KEY, INSTALL_KEY, list);
    }

    @SuppressWarnings("unchecked")
    public List<ScmInstall> getScmInstall() {
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

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        return service.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
