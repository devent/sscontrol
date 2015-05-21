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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * <i>Ubuntu</i> locale installer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class UbuntuInstallLocale implements
        Callable<UbuntuInstallLocale> {

    private static final String LOG_KEY = "log";

    private static final String LOCALES_KEY = "locales";

    private final Map<String, Object> args;

    private Threads threads;

    private Object parent;

    /**
     * Sets the arguments.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code locales} the {@link List} list of the locale
     *            {@link String} names, for example
     *            {@code ["de_DE-ISO-8859-1", "pt_BR.ISO-8859-1"]}.
     *            </ul>
     *
     */
    protected UbuntuInstallLocale(Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
    }

    @Inject
    public final void setUbuntuInstallLocaleLogger(UbuntuInstallLocaleLogger log) {
        log.locales(args, parent);
    }

    /**
     * Returns the list of the locales.
     *
     * @return the {@link List} list of the locale {@link String} names,
     */
    @SuppressWarnings("unchecked")
    public List<String> getLocales() {
        return (List<String>) args.get(LOCALES_KEY);
    }

    /**
     * Returns the logger.
     *
     * @return the {@link Logger} logger,
     */
    public Logger getLog() {
        return (Logger) args.get(LOG_KEY);
    }

    public final void setThreads(Threads threads) {
        this.threads = threads;
    }

    public Threads getThreads() {
        return threads;
    }

    public final void setParent(Object parent) {
        this.parent = parent;
    }

    public Object getParent() {
        return parent;
    }

    public Map<String, Object> getArgs() {
        return Collections.unmodifiableMap(args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
