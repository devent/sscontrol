/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Horde</i> service.
 * 
 * @see <a href="http://www.horde.org">http://www.horde.org</a>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class HordeService implements WebService {

    /**
     * The <i>Horde</i> service name.
     */
    public static final String SERVICE_NAME = "horde";

    private static final String NAME = "name";

    private static final String ALIAS = "alias";

    private final WebServiceLogger serviceLog;

    private final Domain domain;

    private final HordeServiceLogger log;

    private StatementsMap statementsMap;

    private DebugLoggingFactory debugFactory;

    private String alias;

    private DebugLogging debug;

    private String id;

    private String ref;

    private String refDomain;

    private String prefix;

    private OverrideMode overrideMode;

    /**
     * @see HordeServiceFactory#create(Map, Domain)
     */
    @Inject
    HordeService(HordeServiceLogger log, WebServiceLogger serviceLog,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.log = log;
        this.serviceLog = serviceLog;
        this.domain = domain;
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

    @Inject
    public void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, getName());
        this.statementsMap = map;
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

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
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
        if (debug == null) {
            this.debug = debugFactory.createOff();
        }
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

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        statementsMap.methodMissing(name, args);
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME)
                .append(ALIAS, alias).toString();
    }
}
