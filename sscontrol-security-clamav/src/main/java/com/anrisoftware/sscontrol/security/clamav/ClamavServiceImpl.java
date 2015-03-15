/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTable;
import com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTableFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;

/**
 * <i>ClamAV</i> service.
 *
 * @see <a href="http://www.clamav.net/">http://www.clamav.net/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ClamavServiceImpl implements ClamavService {

    private static final String DEBUG_KEY = "debug";

    private static final String NAME = "service name";

    /**
     * The <i>ClamAV</i> service name.
     */
    public static final String SERVICE_NAME = "clamav";

    private StatementsTable statementsTable;

    private BindingAddressesStatementsTable bindingAddressesStatements;

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        this.statementsTable = table;
    }

    @Inject
    public final void setBindingAddressesStatements(
            BindingAddressesStatementsTableFactory factory) {
        this.bindingAddressesStatements = factory.create(this, SERVICE_NAME);
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public Map<String, Object> debugLogging(String key) {
        return statementsTable.tableKeys(DEBUG_KEY, key);
    }

    @Override
    public Map<String, List<Integer>> getBindingAddresses() {
        return bindingAddressesStatements.getBindingAddresses();
    }

    public Object methodMissing(String name, Object args) {
        try {
            return statementsTable.methodMissing(name, args);
        } catch (StatementsException s) {
            return bindingAddressesStatements.methodMissing(name, args);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME).toString();
    }
}
