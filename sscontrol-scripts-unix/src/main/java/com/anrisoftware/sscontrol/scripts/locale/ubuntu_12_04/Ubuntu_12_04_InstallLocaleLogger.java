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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04;

import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.argument_blank_null;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_attach_locale;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_attach_locale_message;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_generate_locale;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_generate_locale_message;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_install_packages;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_install_packages_message;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_load_locales;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.error_load_locales_message;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.the_file;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.the_locale;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.the_packages;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.posixlocale.PosixLocale;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;

/**
 * Logging for {@link Ubuntu_12_04_InstallLocale}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_InstallLocaleLogger extends AbstractLogger {

    static final String LOCALES_DIRECTORY_KEY = "localesDirectory";
    static final String DPKG_RECONFIGURE_COMMAND_KEY = "dpkgReconfigureCommand";
    static final String CHARSET_KEY = "charset";
    static final String INSTALL_COMMAND_KEY = "installCommand";
    static final String SYSTEM_KEY = "system";

    enum _ {

        argument_blank_null("Argument '%s' cannot be null or blank."),

        error_install_packages("Error install packages"),

        error_install_packages_message("Error install packages '{}': {}"),

        the_locale("locale install"),

        the_packages("packages"),

        error_generate_locale("Error generate locale"),

        error_generate_locale_message("Error generate locale {}.{}: {}"),

        error_attach_locale("Error attach locale"),

        error_attach_locale_message("Error attach locale to file '{}': {}"),

        the_file("file"),

        error_load_locales("Error load locales"),

        error_load_locales_message("Error load locales from file '{}': {}");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link Ubuntu_12_04_InstallLocale}.
     */
    public Ubuntu_12_04_InstallLocaleLogger() {
        super(Ubuntu_12_04_InstallLocale.class);
    }

    void checkInstallCommand(Map<String, Object> args, Object parent) {
        Object object = args.get(INSTALL_COMMAND_KEY);
        notNull(object, argument_blank_null.toString(), INSTALL_COMMAND_KEY);
        notBlank(object.toString(), argument_blank_null.toString(),
                INSTALL_COMMAND_KEY);
    }

    void checkSystem(Map<String, Object> args, Object parent) {
        Object object = args.get(SYSTEM_KEY);
        notNull(object, argument_blank_null.toString(), SYSTEM_KEY);
        notBlank(object.toString(), argument_blank_null.toString(), SYSTEM_KEY);
    }

    void checkCharset(Map<String, Object> args, Object parent) {
        Object object = args.get(CHARSET_KEY);
        notNull(object, argument_blank_null.toString(), CHARSET_KEY);
    }

    void checkDpkgReconfigureCommand(Map<String, Object> args, Object parent) {
        Object object = args.get(DPKG_RECONFIGURE_COMMAND_KEY);
        notNull(object, argument_blank_null.toString(),
                DPKG_RECONFIGURE_COMMAND_KEY);
        notBlank(object.toString(), argument_blank_null.toString(),
                DPKG_RECONFIGURE_COMMAND_KEY);
    }

    void checkLocalesDirectory(Map<String, Object> args, Object parent) {
        Object object = args.get(LOCALES_DIRECTORY_KEY);
        notNull(object, argument_blank_null.toString(), LOCALES_DIRECTORY_KEY);
    }

    ScriptException errorInstallPackages(
            Ubuntu_12_04_InstallLocale installLocale, Exception e,
            List<String> packages) {
        return logException(
                new ScriptException(error_install_packages, e).add(the_locale,
                        installLocale).add(the_packages, packages),
                error_install_packages_message, packages,
                e.getLocalizedMessage());
    }

    ScriptException errorGenerateLocale(
            Ubuntu_12_04_InstallLocale installLocale, Exception e,
            PosixLocale locale) {
        return logException(new ScriptException(error_generate_locale, e).add(
                the_locale, installLocale), error_generate_locale_message,
                locale.getLocale(), locale.getCharset(),
                e.getLocalizedMessage());
    }

    ScriptException errorAttachLocale(Ubuntu_12_04_InstallLocale installLocale,
            IOException e, File file) {
        return logException(
                new ScriptException(error_attach_locale, e).add(the_locale,
                        installLocale).add(the_file, file),
                error_attach_locale_message, file, e.getLocalizedMessage());
    }

    ScriptException errorLoadLocales(Ubuntu_12_04_InstallLocale installLocale,
            IOException e, File file) {
        return logException(
                new ScriptException(error_load_locales, e).add(the_locale,
                        installLocale).add(the_file, file),
                error_load_locales_message, file, e.getLocalizedMessage());
    }

}
