/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.statements.wordpress;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.database.DatabaseArgs;
import com.anrisoftware.sscontrol.core.database.DatabaseFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.statements.webserviceargs.WebServiceArgs;
import com.anrisoftware.sscontrol.httpd.statements.webserviceargs.WebServiceLogger;
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

    @Inject
    private WordpressServiceLogger log;

    @Inject
    private DatabaseFactory databaseFactory;

    private DebugLoggingFactory debugFactory;

    private String alias;

    private Database database;

    private DebugLogging debug;

    private String id;

    private String ref;

    private String refDomain;

    private MultiSite multiSite;

    /**
     * @see WordpressServiceFactory#create(Map)
     */
    @Inject
    WordpressService(WebServiceArgs aargs, WebServiceLogger logger,
            @Assisted Domain domain, @Assisted Map<String, Object> args) {
        this.serviceLog = logger;
        this.domain = domain;
        if (aargs.haveAlias(args)) {
            setAlias(aargs.alias(this, args));
        }
        if (aargs.haveId(args)) {
            setId(aargs.id(this, args));
        }
        if (aargs.haveRef(args)) {
            setRef(aargs.ref(this, args));
        }
        if (aargs.haveRefDomain(args)) {
            setRefDomain(aargs.refDomain(this, args));
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

    public void setRefDomain(String refDomain) {
        this.refDomain = refDomain;
    }

    @Override
    public String getRefDomain() {
        return refDomain;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME1, NAME)
                .append(ALIAS, alias).toString();
    }
}
