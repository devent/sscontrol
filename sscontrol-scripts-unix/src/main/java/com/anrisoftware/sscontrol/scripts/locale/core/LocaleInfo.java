/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.locale.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;
import com.google.inject.assistedinject.Assisted;

/**
 * Parses the locale string and returns the language, country and character set
 * code.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 *
 * @since 1.0
 */
public class LocaleInfo {

    private static final String LOCALE_NAME = "locale name";

    private final static Pattern LOCALE_PATTERN = Pattern
            .compile("^([a-z]+)(?:_([a-z]+))?(?:\\.((utf.*)|(iso.*)))?$");

    private final String locale;

    @Inject
    private LocaleInfoLogger log;

    private String language;

    private String country;

    private String charset;

    /**
     * @see LocaleInfoFactory#create(String)
     */
    @Inject
    LocaleInfo(@Assisted String locale) {
        this.locale = locale;
    }

    /**
     * Parses the locale and returns the locale information.
     *
     * @return the {@link LocaleInfo}.
     *
     * @throws ScriptException
     *             the the locale does not match.
     */
    public LocaleInfo parseLocale() throws ScriptException {
        Matcher matcher = LOCALE_PATTERN.matcher(locale.toLowerCase());
        log.localeMatches(this, matcher.matches());
        this.language = matcher.group(1);
        if (matcher.groupCount() > 2) {
            this.country = matcher.group(2);
        }
        if (matcher.groupCount() > 3) {
            this.charset = matcher.group(3);
        }
        return this;
    }

    public String getLocale() {
        return locale;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getCharset() {
        return charset;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(LOCALE_NAME, locale).toString();
    }
}
