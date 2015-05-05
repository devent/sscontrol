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

import static com.anrisoftware.globalpom.format.durationsimpleformat.UnitMultiplier.parseUnitMultiplier;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.startsWith;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.format.durationsimpleformat.DurationSimpleFormat;
import com.anrisoftware.globalpom.format.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * Simple duration attribute renderer. The attribute renderer is called with the
 * format string of {@code "simple"} or {@code "simpleround"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class SimpleDurationRenderer implements AttributeRenderer {

    private static final DecimalFormat SIMPLE_NUMBER_FORMAT = new DecimalFormat(
            "0");

    private static final String SIMPLE_KEY = "simple";

    private static final String SIMPLE_ROUND_KEY = "simpleround";

    private static final String SEP_KEY = "-";

    @Inject
    private DurationSimpleFormatFactory durationSimpleFormatFactory;

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        Duration duration = (Duration) o;
        if (StringUtils.equals(formatString, SIMPLE_ROUND_KEY)) {
            return DurationSimpleFormat.roundSizeUnit(duration.getMillis());
        }
        if (startsWith(formatString, SIMPLE_KEY)) {
            return toStringSimple(formatString, duration);
        } else {
            return duration.toString();
        }
    }

    private String toStringSimple(String formatString, Duration duration) {
        String[] split = split(formatString, SEP_KEY);
        if (split.length == 1) {
            return durationSimpleFormatFactory.create(SIMPLE_NUMBER_FORMAT)
                    .format(duration);
        } else {
            return durationSimpleFormatFactory.create(SIMPLE_NUMBER_FORMAT)
                    .format(duration, parseUnitMultiplier(split[1]));
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return Duration.class;
    }

}
