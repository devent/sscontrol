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
package com.anrisoftware.sscontrol.core.list;

import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.strip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Returns a list of items.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class StringToList {

    @Inject
    private StringToListPropertiesProvider properties;

    private final Object property;

    private List<Object> list;

    /**
     * @see StringToListFactory#create(Object)
     */
    @Inject
    StringToList(@Assisted Object property) {
        this.property = property;
    }

    /**
     * Returns the list of the items.
     *
     * @return the items of the property as {@link List}.
     */
    public List<Object> getList() {
        if (list == null) {
            synchronized (this) {
                list = createList0();
            }
        }
        return list;
    }

    private List<Object> createList0() {
        List<Object> list = new ArrayList<Object>();
        if (property instanceof Collection) {
            list.addAll((Collection<?>) property);
        } else {
            String[] str = split(property.toString(), getSeparators());
            for (String string : str) {
                list.add(strip(string));
            }
        }
        return list;
    }

    private String getSeparators() {
        return properties.get().getProperty("list_separators");
    }
}
