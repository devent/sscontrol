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
package com.anrisoftware.sscontrol.core.bindings;

import static com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTableLogger._.binding_address_invalid;
import static com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTableLogger._.binding_address_null;
import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;

/**
 * Logging for {@link BindingAddressesStatementsTable}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BindingAddressesStatementsTableLogger extends AbstractLogger {

    enum _ {

        binding_address_null("Binding address must be set for %s"),

        binding_address_invalid(
                "Binding address must be valid host address for %s");

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
     * Sets the context of the logger to {@link BindingAddressesStatementsTable}
     * .
     */
    public BindingAddressesStatementsTableLogger() {
        super(BindingAddressesStatementsTable.class);
    }

    void checkBindings(List<?> list, boolean requirePort, StatementsTable table) {
        int listMin = requirePort ? 1 : 0;
        isTrue(list.size() > listMin,
                format(binding_address_null.toString(), table.getService()));
        int addressIndex = list.size() > 1 ? 1 : 0;
        String address = list.get(addressIndex).toString();
        isTrue(InetAddressValidator.getInstance().isValid(address)
                || StringUtils.equals(list.get(1).toString(), "*"),
                format(binding_address_invalid.toString(), table.getService()));
    }
}
