/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.resources.ToURIFactory;
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
import com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgs;
import com.google.inject.assistedinject.Assisted;

/**
 * Wordpress service.
 * 
 * @see <a href='http://wordpress.org/">http://wordpress.org/</a>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WordpressService implements WebService {

    private static final String SETUP_KEY = "setup";

    private static final String MULTISITE_KEY = "multisite";

    private static final String ADMIN_KEY = "admin";

    private static final String LOGIN_KEY = "login";

    private static final String HOST_KEY = "host";

    private static final String PASSWORD_KEY = "password";

    private static final String USER_KEY = "user";

    private static final String THEMES_KEY = "themes";

    private static final String PLUGINS_KEY = "plugins";

    private static final String FORCE_KEY = "force";

    private static final String DATABASE_KEY = "database";

    private static final String TARGET_KEY = "target";

    private static final String BACKUP_KEY = "backup";

    private static final String NAME = "name";

    public static final String SERVICE_NAME = "wordpress";

    private static final String ALIAS_KEY = "alias";

    private final Domain domain;

    private final StatementsMap statementsMap;

    private final DefaultWebService service;

    @Inject
    private WordpressServiceLogger log;

    @Inject
    private ForceFactory forceFactory;

    @Inject
    private OverrideModeArgs overrideModeArgs;

    @Inject
    private ToURIFactory toURIFactory;

    @Inject
    private DatabaseFactory databaseFactory;

    private DebugLoggingFactory debugFactory;

    private DebugLogging debug;

    private Force force;

    private OverrideMode overrideMode;

    private Database database;

    /**
     * @see WordpressServiceFactory#create(Map, Domain)
     */
    @Inject
    WordpressService(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain)
            throws ServiceException {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        this.domain = domain;
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args)
            throws ServiceException {
        map.addAllowed(BACKUP_KEY, DATABASE_KEY, FORCE_KEY, PLUGINS_KEY,
                THEMES_KEY, MULTISITE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
        map.addAllowedKeys(FORCE_KEY, LOGIN_KEY, ADMIN_KEY);
        map.addAllowedKeys(MULTISITE_KEY, SETUP_KEY);
        map.setAllowValue(DATABASE_KEY, true);
        map.setAllowValue(PLUGINS_KEY, true);
        map.setAllowValue(THEMES_KEY, true);
    }

    @Inject
    void setDebugLoggingFactory(DebugLoggingFactory factory) {
        this.debugFactory = factory;
        this.debug = factory.createOff();
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

    public void database(Map<String, Object> args, String name) {
        args.put(DatabaseArgs.DATABASE, name);
        this.database = databaseFactory.create(this, args);
    }

    public Database getDatabase() {
        return database;
    }

    public void debug(boolean enabled) {
        DebugLogging logging = debugFactory.create(enabled ? 1 : 0);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public DebugLogging getDebugLogging() {
        if (debug == null) {
            this.debug = debugFactory.createOff();
        }
        return debug;
    }

    public void setMultiSite(MultiSite multiSite) throws ServiceException {
        statementsMap.putMapValue(MULTISITE_KEY, SETUP_KEY, multiSite);
    }

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
        statementsMap.putValue(THEMES_KEY, themes);
    }

    public List<String> getThemes() {
        return statementsMap.valueAsList(THEMES_KEY);
    }

    public void setPlugins(List<String> plugins) throws ServiceException {
        statementsMap.putValue(PLUGINS_KEY, plugins);
    }

    public List<String> getPlugins() {
        return statementsMap.valueAsList(PLUGINS_KEY);
    }

    public void force(Map<String, Object> args) {
        Force force = forceFactory.create(args);
        log.forceSet(this, force);
        this.force = force;
    }

    public void setForce(Force force) {
        this.force = force;
    }

    public Force getForce() {
        return force;
    }

    public void override(Map<String, Object> args) {
        setOverrideMode(overrideModeArgs.override(this, args));
    }

    public void setOverrideMode(OverrideMode mode) {
        this.overrideMode = mode;
    }

    public OverrideMode getOverrideMode() {
        return overrideMode;
    }

    public void setBackupTarget(URI uri) throws ServiceException {
        statementsMap.putMapValue(BACKUP_KEY, TARGET_KEY, uri);
    }

    public URI getBackupTarget() {
        Object path = statementsMap.mapValue(BACKUP_KEY, TARGET_KEY);
        if (path == null) {
            return null;
        } else {
            return toURIFactory.create().convert(path);
        }
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        service.methodMissing(name, args);
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME)
                .append(ALIAS_KEY, getAlias()).toString();
    }
}
