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
package com.anrisoftware.sscontrol.httpd.roundcube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.database.DatabaseArgs;
import com.anrisoftware.sscontrol.core.database.DatabaseFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Roundcube service.
 * 
 * @see <a href=http://roundcube.net/>http://roundcube.net/</a>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RoundcubeService implements WebService {

    public static final String SERVICE_NAME = "roundcube";

    private final DefaultWebService service;

    private final List<Host> hosts;

    @Inject
    private RoundcubeServiceLogger log;

    @Inject
    private DatabaseFactory databaseFactory;

    @Inject
    private HostFactory hostFactory;

    @Inject
    private DebugLoggingFactory debugFactory;

    private SmtpServerFactory smtpFactory;

    private Database database;

    private DebugLogging debug;

    private SmtpServer smtp;

    /**
     * @see RoundcubeServiceFactory#create(Map, Domain)
     */
    @Inject
    RoundcubeService(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.hosts = new ArrayList<Host>();
    }

    @Inject
    void setSmtpServerFactory(SmtpServerFactory factory) {
        this.smtp = factory.createDefault();
        this.smtpFactory = factory;
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

    public void database(Map<String, Object> args, String name) {
        args.put(DatabaseArgs.DATABASE, name);
        Database database = databaseFactory.create(this, args);
        log.databaseSet(this, database);
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public void host(String name) {
        host(new HashMap<String, Object>(), name);
    }

    public void host(Map<String, Object> args, String name) {
        args.put(HostArgs.HOST, name);
        Host host = hostFactory.create(this, args);
        log.hostAdded(this, host);
        hosts.add(host);
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void debug(Map<String, Object> args) {
        DebugLogging logging = debugFactory.create(args);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public void debug(int level) {
        DebugLogging logging = debugFactory.create(level);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public DebugLogging getDebugLogging() {
        if (debug == null) {
            this.debug = debugFactory.createOff();
        }
        return debug;
    }

    public void smtp(String host) {
        smtp(new HashMap<String, Object>(), host);
    }

    public void smtp(Map<String, Object> args, String host) {
        args.put(SmtpServerArgs.HOST, host);
        SmtpServer smtp = smtpFactory.create(this, args);
        log.smtpSet(this, smtp);
        this.smtp = smtp;
    }

    public SmtpServer getSmtp() {
        return smtp;
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
