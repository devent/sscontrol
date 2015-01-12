/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;

/**
 * <i>Spamassassin</i> service.
 *
 * @see <a href="http://www.spamassassin.org/">http://www.spamassassin.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SpamassassinServiceImpl implements SpamassassinService {

    private static final String HEADERS_KEY = "headers";

    private static final String CLEAR_KEY = "clear";

    private static final String SCORE_KEY = "score";

    private static final String NETWORKS_KEY = "networks";

    private static final String HEADER_KEY = "header";

    private static final String SPAM_KEY = "spam";

    private static final String TRUSTED_KEY = "trusted";

    private static final String REWRITE_KEY = "rewrite";

    private static final String DEBUG_KEY = "debug";

    private static final String NAME = "service name";

    /**
     * The <i>Fail2ban</i> service name.
     */
    public static final String SERVICE_NAME = "spamassassin";

    @Inject
    private SpamassassinServiceImplLogger log;

    @Inject
    private HeaderFactory headerFactory;

    private List<Header> headers;

    private StatementsTable statementsTable;

    private StatementsMap statementsMap;

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, SERVICE_NAME);
        map.addAllowed(CLEAR_KEY, TRUSTED_KEY, SPAM_KEY);
        map.addAllowedKeys(CLEAR_KEY, HEADERS_KEY);
        map.addAllowedKeys(TRUSTED_KEY, NETWORKS_KEY);
        map.addAllowedKeys(SPAM_KEY, SCORE_KEY);
        map.setAllowValue(true, CLEAR_KEY);
        this.statementsMap = map;
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY, REWRITE_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        table.addAllowedKeys(REWRITE_KEY, HEADER_KEY);
        this.statementsTable = table;
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
    public ClearType getClearHeaders() {
        return statementsMap.value(CLEAR_KEY);
    }

    @Override
    public Map<String, Object> getRewriteHeaders() {
        return statementsTable.tableKeys(REWRITE_KEY, HEADER_KEY);
    }

    @Override
    public List<Header> getAddHeaders() {
        return headers;
    }

    @Override
    public List<String> getTrustedNetworks() {
        return statementsMap.mapValueAsStringList(TRUSTED_KEY, NETWORKS_KEY);
    }

    @Override
    public Double getSpamScore() {
        Number value = (Number) statementsMap.mapValue(SPAM_KEY, SCORE_KEY);
        if (value != null) {
            return value.doubleValue();
        } else {
            return null;
        }
    }

    public void add(Map<String, Object> args, MessageType type) {
        args.put("type", type);
        Header header = headerFactory.create(args);
        if (headers == null) {
            this.headers = new ArrayList<Header>();
        }
        headers.add(header);
        log.addHeaderAdded(this, header);
    }

    public Object methodMissing(String name, Object args) {
        try {
            return statementsMap.methodMissing(name, args);
        } catch (StatementsException s) {
            return statementsTable.methodMissing(name, args);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME).toString();
    }
}
