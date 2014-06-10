/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress;

import java.net.URI;
import java.util.ArrayList;
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
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.list.StringToListFactory;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgs;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
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

    private static final String TARGET_KEY = "target";

    private static final String BACKUP_KEY = "backup";

    private static final String PREFIX_KEY = "prefix";

    private static final String REF_DOMAIN_KEY = "refDomain";

    private static final String REF_KEY = "ref";

    private static final String ID_KEY = "id";

    private static final String NAME = "name";

    public static final String SERVICE_NAME = "wordpress";

    private static final String ALIAS_KEY = "alias";

    private final WebServiceLogger serviceLog;

    private final Domain domain;

    private final List<String> themes;

    private final List<String> plugins;

    private final StatementsMap statementsMap;

    @Inject
    private WordpressServiceLogger log;

    @Inject
    private DatabaseFactory databaseFactory;

    @Inject
    private StringToListFactory toListFactory;

    @Inject
    private ForceFactory forceFactory;

    @Inject
    private OverrideModeArgs overrideModeArgs;

    @Inject
    private ToURIFactory toURIFactory;

    private DebugLoggingFactory debugFactory;

    private Database database;

    private DebugLogging debug;

    private MultiSite multiSite;

    private Force force;

    private OverrideMode overrideMode;

    /**
     * @see WordpressServiceFactory#create(Map, Domain)
     */
    @Inject
    WordpressService(WebServiceLogger serviceLog,
            StatementsMapFactory mapFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.serviceLog = serviceLog;
        this.domain = domain;
        this.multiSite = MultiSite.none;
        this.themes = new ArrayList<String>();
        this.plugins = new ArrayList<String>();
        this.statementsMap = mapFactory.create(this, SERVICE_NAME);
        statementsMap.addAllowed(ALIAS_KEY);
        statementsMap.addAllowed(ID_KEY);
        statementsMap.addAllowed(REF_KEY);
        statementsMap.addAllowed(REF_DOMAIN_KEY);
        statementsMap.addAllowed(PREFIX_KEY);
        statementsMap.addAllowed(BACKUP_KEY);
        if (serviceLog.haveAlias(args)) {
            statementsMap.putValue(ALIAS_KEY, serviceLog.alias(this, args));
        }
        if (serviceLog.haveId(args)) {
            statementsMap.putValue(ID_KEY, serviceLog.id(this, args));
        }
        if (serviceLog.haveRef(args)) {
            statementsMap.putValue(REF_KEY, serviceLog.ref(this, args));
        }
        if (serviceLog.haveRefDomain(args)) {
            statementsMap.putValue(REF_DOMAIN_KEY,
                    serviceLog.refDomain(this, args));
        }
        if (serviceLog.havePrefix(args)) {
            statementsMap.putValue(PREFIX_KEY, serviceLog.prefix(this, args));
        }
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

    public void setAlias(String alias) {
        statementsMap.putValue(ALIAS_KEY, alias);
        serviceLog.aliasSet(this, alias);
    }

    public String getAlias() {
        return statementsMap.value(ALIAS_KEY);
    }

    public void setId(String id) {
        statementsMap.putValue(ID_KEY, id);
        serviceLog.idSet(this, id);
    }

    @Override
    public String getId() {
        return statementsMap.value(ID_KEY);
    }

    public void setRef(String ref) {
        statementsMap.putValue(REF_KEY, ref);
        serviceLog.refSet(this, ref);
    }

    @Override
    public String getRef() {
        return statementsMap.value(REF_KEY);
    }

    public void setRefDomain(String domain) {
        statementsMap.putValue(REF_DOMAIN_KEY, domain);
        serviceLog.refDomainSet(this, domain);
    }

    @Override
    public String getRefDomain() {
        return statementsMap.value(REF_DOMAIN_KEY);
    }

    public void setPrefix(String prefix) {
        statementsMap.putValue(PREFIX_KEY, prefix);
        serviceLog.prefixSet(this, prefix);
    }

    public String getPrefix() {
        return statementsMap.value(PREFIX_KEY);
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

    public DebugLogging getDebugLogging() {
        if (debug == null) {
            this.debug = debugFactory.createOff();
        }
        return debug;
    }

    public void multisite(MultiSite type) {
        this.multiSite = type;
    }

    public void multisite(String string) {
        this.multiSite = MultiSite.parse(string);
    }

    public MultiSite getMultiSite() {
        return multiSite;
    }

    public void themes(String themes) {
        List<String> list = toListFactory.create(themes).getList();
        log.themesAdded(this, list);
        this.themes.addAll(list);
    }

    public void themes(List<String> themes) {
        log.themesAdded(this, themes);
        this.themes.addAll(themes);
    }

    public List<String> getThemes() {
        return themes;
    }

    public void plugins(String plugins) {
        List<String> list = toListFactory.create(plugins).getList();
        log.pluginsAdded(this, list);
        this.plugins.addAll(list);
    }

    public void plugins(List<String> plugins) {
        log.pluginsAdded(this, plugins);
        this.plugins.addAll(plugins);
    }

    public List<String> getPlugins() {
        return plugins;
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

    public void setBackupTarget(URI uri) {
        statementsMap.putMapValue(BACKUP_KEY, TARGET_KEY, uri);
    }

    public URI getBackupTarget() {
        Object path = statementsMap.mapValue(BACKUP_KEY, TARGET_KEY);
        return toURIFactory.create().convert(path);
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        statementsMap.methodMissing(name, args);
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME)
                .append(ALIAS_KEY, getAlias()).toString();
    }
}
