/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.webserviceargs;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Parses override mode arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class OverrideModeArgs {

    private static final String OVERRIDE_MODE = "mode";

    @Inject
    private OverrideModeArgsLogger log;

    /**
     * Parses the override mode.
     * 
     * @param service
     *            the {@link WebService}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link OverrideMode}.
     * 
     * @throws NullPointerException
     *             if the override mode is {@code null}.
     * 
     * @throws IllegalArgumentException
     *             if the override mode is not {@link YesNoFlag} or
     *             {@link OverrideMode}.
     */
    public OverrideMode override(WebService service, Map<String, Object> args) {
        Object mode = args.get(OVERRIDE_MODE);
        log.checkOverrideMode(service, mode);
        if (mode instanceof String) {
            return OverrideMode.valueOf(mode.toString());
        }
        if (mode instanceof YesNoFlag) {
            YesNoFlag flag = (YesNoFlag) mode;
            if (flag == YesNoFlag.no) {
                return OverrideMode.no;
            }
            if (flag == YesNoFlag.yes) {
                return OverrideMode.override;
            }
        }
        if (mode instanceof OverrideMode) {
            return (OverrideMode) mode;
        }
        throw log.invalidOverrideMode(service, mode);
    }

}
