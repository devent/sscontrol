/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.linux;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.core.bindings.Address;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;

/**
 * Renderer for postfix bind addresses.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class BindAddressesRenderer implements AttributeRenderer {

    private static final String ALL = "all";

    private static final String LOOPBACK_ONLY = "loopback-only";

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return format((Address) o);
    }

    private String format(Address address) {
        if (address.getAddress() instanceof BindingAddress) {
            return format((BindingAddress) address.getAddress());
        } else {
            return address.getAddress().toString();
        }
    }

    private String format(BindingAddress address) {
        switch (address) {
        case local:
        case loopback:
            return LOOPBACK_ONLY;
        default:
            return ALL;
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return Address.class;
    }

}
