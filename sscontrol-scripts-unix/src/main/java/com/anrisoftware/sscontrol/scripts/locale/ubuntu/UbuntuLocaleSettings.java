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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu;

import static java.lang.String.format;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.scripts.locale.core.LocaleInfo;
import com.anrisoftware.sscontrol.scripts.locale.core.LocaleInfoFactory;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Ubuntu</i> settings needed for the specified locale.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UbuntuLocaleSettings {

    private static final String PACKAGE_LANGUAGE_PACK_PATTERN_KEY = "package_language_pack_pattern";

    private final String locale;

    @Inject
    private UbuntuLocalePackagesPropertiesProvider propertiesProvider;

    @Inject
    private LocaleInfoFactory infoFactory;

    private LocaleInfo info;

    private List<String> packagesList;

    private String supportedLocaleFileName;

    private String convertName;

    private String convertedCharset;

    /**
     * @see UbuntuLocaleSettingsFactory#create(String)
     */
    @Inject
    UbuntuLocaleSettings(@Assisted String locale) {
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
    public UbuntuLocaleSettings parseLocale() throws ScriptException {
        this.info = infoFactory.create(locale).parseLocale();
        return this;
    }

    /**
     * Returns the packages needed for the locale.
     *
     * @return the {@link List} of {@link String} packages.
     *
     * @throws ScriptException
     *             if there was an error retrieving the package names.
     */
    public List<String> getPackages() throws ScriptException {
        if (packagesList == null) {
            this.packagesList = createPackages();
        }
        return packagesList;
    }

    /**
     * Returns the supported locale file name.
     *
     * @return the {@link String} file.
     *
     * @throws ScriptException
     *             if there was an error retrieving the file name.
     */
    public String getSupportedLocaleFileName() throws ScriptException {
        if (supportedLocaleFileName == null) {
            this.supportedLocaleFileName = createSupportedLocaleFileName();
        }
        return supportedLocaleFileName;
    }

    /**
     * Returns the locale name, converted for the system.
     *
     * @return the {@link String} name.
     */
    public String getConvertName() {
        if (convertName == null) {
            this.convertName = createConvertName();
        }
        return convertName;
    }

    /**
     * Returns the locale character name, converted for the system.
     *
     * @return the {@link String} name.
     */
    public String getConvertedCharset() {
        if (convertedCharset == null) {
            this.convertedCharset = createConvertedCharset();
        }
        return convertedCharset;
    }

    /**
     * Returns the locale information.
     *
     * @return the locale {@link LocaleInfo} information.
     */
    public LocaleInfo getLocaleInfo() {
        return info;
    }

    /**
     * Returns the locale name.
     *
     * @return the locale {@link String} name.
     */
    public String getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(locale).toString();
    }

    private List<String> createPackages() {
        List<String> packagesList = new ArrayList<String>();
        addLanguagePack(packagesList);
        return packagesList;
    }

    private void addLanguagePack(List<String> list) {
        String languagePackPattern = propertiesProvider.get().getProperty(
                PACKAGE_LANGUAGE_PACK_PATTERN_KEY);
        String languagePackPackage = format(languagePackPattern,
                info.getLanguage());
        list.add(languagePackPackage);
    }

    private String createSupportedLocaleFileName() {
        return info.getLanguage();
    }

    private String createConvertName() {
        String language = info.getLanguage();
        String country = info.getCountry();
        String charset = info.getCharset();
        StringBuilder builder = new StringBuilder();
        builder.append(language);
        builder.append("_");
        if (country == null) {
            builder.append(language.toUpperCase());
        } else {
            builder.append(country.toUpperCase());
        }
        builder.append(".");
        if (charset == null) {
            builder.append(convertCharsetName(Charset.defaultCharset().name()));
        } else {
            builder.append(convertCharsetName(charset));
        }
        return builder.toString();
    }

    private String convertCharsetName(String name) {
        return name.toUpperCase();
    }

    private String createConvertedCharset() {
        String charset = info.getCharset();
        return charset.toUpperCase();
    }

}
