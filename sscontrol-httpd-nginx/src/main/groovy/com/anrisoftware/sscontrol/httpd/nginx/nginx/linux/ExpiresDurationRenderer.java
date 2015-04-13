/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import static com.anrisoftware.globalpom.format.durationsimpleformat.UnitMultiplier.SECONDS;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.format.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * Expires duration attribute renderer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ExpiresDurationRenderer implements AttributeRenderer {

    private static final DecimalFormat SIMPLE_NUMBER_FORMAT = new DecimalFormat(
            "0");

    private static final String EXPIRES_SECONDS = "expires-s";

    @Inject
    private DurationSimpleFormatFactory durationSimpleFormatFactory;

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        Duration duration = (Duration) o;
        if (StringUtils.equals(formatString, EXPIRES_SECONDS)) {
            return durationSimpleFormatFactory.create(SIMPLE_NUMBER_FORMAT)
                    .format(duration, SECONDS);
        } else {
            return duration.toString();
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return Duration.class;
    }

}
