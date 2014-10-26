/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.version;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

/**
 * Parses version with major, minor and revision.
 *
 * @see Version
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class VersionFormat extends Format {

    private static final char VERSION_SEP = '.';

    @Inject
    private VersionFormatLogger log;

    @Inject
    private VersionFactory versionFactory;

    /**
     * Formats the specified version.
     *
     * <pre>
     * major.minor.revision
     * </pre>
     *
     * @param obj
     *            the {@link Version}.
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer buff, FieldPosition pos) {
        if (obj instanceof Version) {
            formatDuration(buff, (Version) obj);
        }
        return buff;
    }

    private void formatDuration(StringBuffer buff, Version version) {
        buff.append(version.getMajor()).append(VERSION_SEP)
                .append(version.getMinor()).append(VERSION_SEP)
                .append(version.getRevision());
    }

    /**
     * Parses the specified string to a version.
     * <p>
     * <h2>Format</h2>
     * <p>
     * <ul>
     * <li>{@code "major[.minor[.revision]]"}
     * </ul>
     *
     * @return the parsed {@link Version}.
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return parse(source, pos);
    }

    /**
     * @see #parse(String, ParsePosition)
     */
    public Version parse(String source) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Version result = parse(source, pos);
        if (pos.getIndex() == 0) {
            throw log.errorParse(source, pos);
        }
        return result;
    }

    /**
     * @see #parseObject(String)
     *
     * @param pos
     *            the index {@link ParsePosition} position from where to start
     *            parsing.
     */
    public Version parse(String source, ParsePosition pos) {
        try {
            source = source.substring(pos.getIndex());
            Version version = decodeVersion(source, pos);
            pos.setErrorIndex(-1);
            pos.setIndex(pos.getIndex() + source.length());
            return version;
        } catch (NumberFormatException e) {
            log.errorParseNumber(e, source);
            pos.setIndex(0);
            pos.setErrorIndex(0);
            return null;
        }
    }

    private Version decodeVersion(String source, ParsePosition pos) {
        String[] str = StringUtils.split(source, VERSION_SEP);
        int major = Integer.valueOf(str[0]);
        int minor = 0;
        int rev = 0;
        if (str.length > 1) {
            minor = Integer.valueOf(str[1]);
        }
        if (str.length > 2) {
            rev = Integer.valueOf(str[2]);
        }
        return versionFactory.create(major, minor, rev);
    }
}
