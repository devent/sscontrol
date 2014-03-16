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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.database.DatabaseArgs;
import com.anrisoftware.sscontrol.core.database.DatabaseFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
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

    private static final String NAME1 = "name";

    public static final String NAME = "wordpress";

    private static final String ALIAS = "alias";

    private final WebServiceLogger serviceLog;

    private final Domain domain;

    private final List<String> themes;

    private final List<String> plugins;

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

    private DebugLoggingFactory debugFactory;

    private String alias;

    private Database database;

    private DebugLogging debug;

    private String id;

    private String ref;

    private String refDomain;

    private MultiSite multiSite;

    private Force force;

    private String prefix;

    private OverrideMode overrideMode;

    /**
     * @see WordpressServiceFactory#create(Map, Domain)
     */
    @Inject
    WordpressService(WebServiceLogger serviceLog,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.serviceLog = serviceLog;
        this.domain = domain;
        this.multiSite = MultiSite.none;
        this.themes = new ArrayList<String>();
        this.plugins = new ArrayList<String>();
        if (serviceLog.haveAlias(args)) {
            this.alias = serviceLog.alias(this, args);
        }
        if (serviceLog.haveId(args)) {
            this.id = serviceLog.id(this, args);
        }
        if (serviceLog.haveRef(args)) {
            this.ref = serviceLog.ref(this, args);
        }
        if (serviceLog.haveRefDomain(args)) {
            this.refDomain = serviceLog.refDomain(this, args);
        }
        if (serviceLog.havePrefix(args)) {
            this.prefix = serviceLog.prefix(this, args);
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
        return NAME;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        serviceLog.aliasSet(this, alias);
    }

    public String getAlias() {
        return alias;
    }

    public void setId(String id) {
        this.id = id;
        serviceLog.idSet(this, id);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setRef(String ref) {
        this.ref = ref;
        serviceLog.refSet(this, ref);
    }

    @Override
    public String getRef() {
        return ref;
    }

    public void setRefDomain(String domain) {
        this.refDomain = domain;
        serviceLog.refDomainSet(this, domain);
    }

    @Override
    public String getRefDomain() {
        return refDomain;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        serviceLog.prefixSet(this, prefix);
    }

    public String getPrefix() {
        return prefix;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME1, NAME)
                .append(ALIAS, alias).toString();
    }
}
