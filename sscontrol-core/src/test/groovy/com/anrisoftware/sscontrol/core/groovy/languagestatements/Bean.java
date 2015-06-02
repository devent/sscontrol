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

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.posixlocale.PosixLocale;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * @see LanguageStatementsTest
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Bean {

    final LanguageStatements map;

    @Inject
    Bean(LanguageStatementsFactory factory) {
        this.map = factory.create(this, "bean");
    }

    public Locale getLanguage() throws ParseException {
        return map.getLanguage();
    }

    public List<PosixLocale> getLocales() throws ParseException {
        return map.getLocales();
    }

    Object methodMissing(String name, Object args) throws ServiceException {
        map.methodMissing(name, args);
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
