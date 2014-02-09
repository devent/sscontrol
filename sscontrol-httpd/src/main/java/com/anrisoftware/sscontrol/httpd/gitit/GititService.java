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
package com.anrisoftware.sscontrol.httpd.gitit;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceArgs;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Gitit</i> service.
 *
 * @see <a href='http://gitit.net">http://gitit.net</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class GititService implements WebService {

    /**
     * The <i>Gitit</i> service name.
     */
    public static final String SERVICE_NAME = "gitit";

    private static final String REPOSITORY_TYPE = "repository type";

    private static final String NAME = "name";

    private static final String ALIAS = "alias";

    private final WebServiceLogger serviceLog;

    private final Domain domain;

    private final GititServiceLogger log;

    private DebugLoggingFactory debugFactory;

    private String alias;

    private DebugLogging debug;

    private String id;

    private String ref;

    private String refDomain;

    private String prefix;

    private OverrideMode overrideMode;

    private RepositoryType type;

    private Boolean caching;

    private Boolean idleGc;

    /**
     * @see GititServiceFactory#create(Domain, Map)
     */
    @Inject
    GititService(GititServiceLogger log, WebServiceArgs aargs,
            WebServiceLogger logger, @Assisted Domain domain,
            @Assisted Map<String, Object> args) {
        this.log = log;
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
        if (aargs.havePrefix(args)) {
            setPrefix(aargs.prefix(this, args));
        }
        if (log.haveType(args)) {
            setType(log.type(this, args));
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

    public void setType(RepositoryType type) {
        this.type = type;
    }

    public RepositoryType getType() {
        return type;
    }

    public void caching(Map<String, Object> args) {
        boolean caching = log.caching(this, args);
        log.cachingSet(this, caching);
        this.caching = caching;
    }

    public void setCaching(Boolean caching) {
        this.caching = caching;
    }

    public Boolean getCaching() {
        return caching;
    }

    public void idle(Map<String, Object> args) {
        boolean gc = log.idleGc(this, args);
        log.idleGcSet(this, gc);
        this.idleGc = gc;
    }

    public void setIdleGc(Boolean idleGc) {
        this.idleGc = idleGc;
    }

    public Boolean getIdleGc() {
        return idleGc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME)
                .append(ALIAS, alias).append(REPOSITORY_TYPE, type).toString();
    }
}
