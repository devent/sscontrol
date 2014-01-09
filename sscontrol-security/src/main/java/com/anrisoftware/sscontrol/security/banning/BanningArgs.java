/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static org.joda.time.Duration.standardSeconds;

import java.text.ParseException;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.format.duration.DurationFormatFactory;
import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Parses arguments for ignoring addresses.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BanningArgs {

    private static final String APP = "app";

    private static final String TYPE = "type";

    private static final String BACKEND = "backend";

    private static final String BANNING = "time";

    private static final String RETRIES = "retries";

    @Inject
    private BanningArgsLogger log;

    @Inject
    private DurationFormatFactory durationFormatFactory;

    boolean haveRetries(Map<String, Object> args) {
        return args.containsKey(RETRIES);
    }

    int retries(Service service, Map<String, Object> args) {
        Object retries = args.get(RETRIES);
        log.checkRetries(retries, service);
        return ((Number) retries).intValue();
    }

    boolean haveBanning(Map<String, Object> args) {
        return args.containsKey(BANNING);
    }

    Duration banning(Service service, Map<String, Object> args)
            throws ParseException {
        Object time = args.get(BANNING);
        log.checkBanning(time, service);
        if (time instanceof Number) {
            return standardSeconds(((Number) time).longValue());
        } else {
            return durationFormatFactory.create().parse(time.toString());
        }
    }

    boolean haveBackend(Map<String, Object> args) {
        return args.containsKey(BACKEND);
    }

    Backend backend(Service service, Map<String, Object> args) {
        Object backend = args.get(BACKEND);
        log.checkBackend(backend, service);
        if (backend instanceof Backend) {
            return (Backend) backend;
        } else {
            return Backend.valueOf(backend.toString());
        }
    }

    boolean haveType(Map<String, Object> args) {
        return args.containsKey(TYPE);
    }

    Type type(Service service, Map<String, Object> args) {
        Object type = args.get(TYPE);
        log.checkType(type, service);
        if (type instanceof Backend) {
            return (Type) type;
        } else {
            return Type.valueOf(type.toString());
        }
    }

    boolean haveApp(Map<String, Object> args) {
        return args.containsKey(APP);
    }

    String app(Service service, Map<String, Object> args) {
        Object app = args.get(APP);
        log.checkApp(app, service);
        return app.toString();
    }

}
