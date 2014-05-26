/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.banning;

import java.text.ParseException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.sscontrol.core.api.Service;
import com.google.inject.assistedinject.Assisted;

/**
 * Banning options.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Banning {

    private static final String APPLICATION = "application";

    private static final String TYPE = "type";

    private static final String BACKEND = "backend";

    private static final String BANNING_TIME = "banning time";

    private static final String MAX_RETRIES = "max retries";

    private Integer maxRetries;

    private Duration banningTime;

    private Backend backend;

    private Type type;

    private String application;

    /**
     * @see BanningFactory#create(Service, Map)
     */
    @Inject
    Banning(BanningArgs aargs, @Assisted Service service,
            @Assisted Map<String, Object> args) throws ParseException {
        if (aargs.haveRetries(args)) {
            this.maxRetries = aargs.retries(service, args);
        }
        if (aargs.haveBanning(args)) {
            this.banningTime = aargs.banning(service, args);
        }
        if (aargs.haveBackend(args)) {
            this.backend = aargs.backend(service, args);
        }
        if (aargs.haveType(args)) {
            this.type = aargs.type(service, args);
        }
        if (aargs.haveApp(args)) {
            this.application = aargs.app(service, args);
        }
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public Duration getBanningTime() {
        return banningTime;
    }

    public Backend getBackend() {
        return backend;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getApplication() {
        return application;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder = maxRetries != null ? builder.append(MAX_RETRIES, maxRetries)
                : builder;
        builder = banningTime != null ? builder.append(BANNING_TIME,
                banningTime) : builder;
        builder = backend != null ? builder.append(BACKEND, backend) : builder;
        builder = type != null ? builder.append(TYPE, type) : builder;
        builder = application != null ? builder
                .append(APPLICATION, application) : builder;
        return builder.toString();
    }
}
