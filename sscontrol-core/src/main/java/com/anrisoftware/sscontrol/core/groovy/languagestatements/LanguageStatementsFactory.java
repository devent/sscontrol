/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.groovy.languagestatements;

/**
 * Factory to create the language statements.
 *
 * <pre>
 * language "en" // or
 * language Locale.US // or
 * language "en", locales: "de, pt" // or
 * language "en", locales: ["de_DE.ISO-88591, pt_BR.ISO-88591"] // or
 * language "en", locales: [Locale.FRANCE, Locale.GERMANY]
 * </pre>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface LanguageStatementsFactory {

    /**
     * Creates the language statements.
     *
     * @param service
     *            the {@link Object} service.
     *
     * @param name
     *            the {@link String} the service name.
     *
     * @return the {@link LanguageStatements}.
     */
    LanguageStatements create(Object service, String name);
}
