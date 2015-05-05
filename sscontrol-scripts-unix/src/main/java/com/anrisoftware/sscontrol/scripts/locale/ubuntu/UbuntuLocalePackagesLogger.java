package com.anrisoftware.sscontrol.scripts.locale.ubuntu;

import static com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuLocalePackagesLogger._.locale_packages;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuLocalePackagesLogger._.locales_error;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuLocalePackagesLogger._.locales_error_message;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;

/**
 * Logging for {@link UbuntuLocalePackages}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuLocalePackagesLogger extends AbstractLogger {

    enum _ {

        locales_error("Locales string does not match"),

        locales_error_message("Locales string '{}' does not match."),

        locale_packages("locale packages");

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
     * Sets the context of the logger to {@link UbuntuLocalePackages}.
     */
    public UbuntuLocalePackagesLogger() {
        super(UbuntuLocalePackages.class);
    }

    void localeMatches(UbuntuLocalePackages packages, boolean matches)
            throws ScriptException {
        if (!matches) {
            throw logException(new ScriptException(locales_error).add(
                    locale_packages, packages), locales_error_message,
                    packages.getLocale());
        }
    }
}
