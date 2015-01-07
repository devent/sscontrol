/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.httpd.citadel.AuthMethod;

/**
 * Renders the timeout duration in seconds.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class TimeoutDurationAttributeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (StringUtils.equals(formatString, "seconds")) {
            return toStringSeconds((Duration) o);
        } else {
            return o.toString();
        }
    }

    private String toStringSeconds(Duration duration) {
        StringBuilder builder = new StringBuilder();
        builder.append(duration.getStandardSeconds());
        builder.append("s");
        return builder.toString();
    }

    @Override
    public Class<?> getAttributeType() {
        return AuthMethod.class;
    }

}
