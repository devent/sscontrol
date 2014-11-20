/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube;

import static java.util.Collections.unmodifiableMap;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Roundcube</i> service.
 *
 * @see <a href="http://roundcube.net/">http://roundcube.net/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RoundcubeService implements WebService {

    private static final String DOMAIN_KEY = "domain";

    private static final String SERVER_KEY = "server";

    private static final String SMTP_KEY = "smtp";

    private static final String DRIVER_KEY = "driver";

    private static final String LEVEL_KEY = "level";

    private static final String DEBUG_KEY = "debug";

    private static final String MODE_KEY = "mode";

    private static final String OVERRIDE_KEY = "override";

    private static final String HOST_KEY = "host";

    private static final String PASSWORD_KEY = "password";

    private static final String USER_KEY = "user";

    private static final String DATABASE_KEY = "database";

    private static final String TARGET_KEY = "target";

    private static final String BACKUP_KEY = "backup";

    private static final String NAME = "name";

    public static final String SERVICE_NAME = "roundcube";

    private static final String ALIAS_KEY = "alias";

    private final Domain domain;

    private final StatementsMap statementsMap;

    private final DefaultWebService service;

    private StatementsTable statementsTable;

    /**
     * @see RoundcubeServiceFactory#create(Map, Domain)
     */
    @Inject
    RoundcubeService(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain)
            throws ServiceException {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        this.domain = domain;
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args)
            throws ServiceException {
        map.addAllowed(OVERRIDE_KEY, BACKUP_KEY, DATABASE_KEY, SMTP_KEY);
        map.addAllowedKeys(OVERRIDE_KEY, MODE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                DRIVER_KEY);
        map.addAllowedKeys(BACKUP_KEY, TARGET_KEY);
        map.addAllowedKeys(SMTP_KEY, USER_KEY, PASSWORD_KEY);
        map.setAllowValue(true, DATABASE_KEY, SMTP_KEY);
    }

    @Inject
    public void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(factory, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY, SERVER_KEY, HOST_KEY);
        table.addAllowedKeys(DEBUG_KEY, LEVEL_KEY);
        table.addAllowedKeys(SERVER_KEY, HOST_KEY);
        table.addAllowedKeys(HOST_KEY, DOMAIN_KEY);
        this.statementsTable = table;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    public StatementsMap getStatementsMap() {
        return service.getStatementsMap();
    }

    public void setAlias(String alias) throws ServiceException {
        service.setAlias(alias);
    }

    @Override
    public String getAlias() {
        return service.getAlias();
    }

    public void setId(String id) throws ServiceException {
        service.setId(id);
    }

    @Override
    public String getId() {
        return service.getId();
    }

    public void setRef(String ref) throws ServiceException {
        service.setRef(ref);
    }

    @Override
    public String getRef() {
        return service.getRef();
    }

    public void setRefDomain(String ref) throws ServiceException {
        service.setRefDomain(ref);
    }

    @Override
    public String getRefDomain() {
        return service.getRefDomain();
    }

    public void setPrefix(String prefix) throws ServiceException {
        service.setPrefix(prefix);
    }

    @Override
    public String getPrefix() {
        return service.getPrefix();
    }

    /**
     * Returns the debug level settings for the modules:
     * <ul>
     * <li>roundcube</li>
     * <li>php</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      debug "php", level: 1
     *      debug "roundcube", level: 1
     * }
     * </pre>
     *
     * @return the debug logging {@link Map} settings or {@code null}.
     */
    public Map<String, Object> getDebug() {
        return statementsTable.tableKeys(DEBUG_KEY, LEVEL_KEY);
    }

    /**
     * Returns the override mode in case the service is already installed inside
     * the service prefix.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      override mode: no
     * }
     * </pre>
     *
     * @return the {@link OverrideMode} or the {@link YesNoFlag} or {@code null}
     */
    public Object getOverrideMode() {
        return statementsMap.mapValue(OVERRIDE_KEY, MODE_KEY);
    }

    /**
     * Returns the backup target.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      backup target: "/var/backups"
     * }
     * </pre>
     *
     * @return the backup {@link URI} target or {@code null}.
     */
    public URI getBackupTarget() {
        return statementsMap.mapValueAsURI(BACKUP_KEY, TARGET_KEY);
    }

    /**
     * Returns the database settings.
     * <ul>
     * <li>"database" the database name;</li>
     * <li>"user" optionally, the database user name;</li>
     * <li>"password" optionally, the user password;</li>
     * <li>"host" optionally, the database host;</li>
     * <li>"driver" optionally, the database driver;</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      database "roundcubedb", user: "userdb", password: "userpassdb", host: "localhost", driver: "mysql"
     * }
     * </pre>
     *
     * @return the {@link Map} with the database settings or {@code null}.
     */
    public Map<String, Object> getDatabase() {
        StatementsMap smap = statementsMap;
        String database = smap.value(DATABASE_KEY);
        if (database != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(DATABASE_KEY, database);
            map.put(USER_KEY, smap.mapValue(DATABASE_KEY, USER_KEY));
            map.put(PASSWORD_KEY, smap.mapValue(DATABASE_KEY, PASSWORD_KEY));
            map.put(HOST_KEY, smap.mapValue(DATABASE_KEY, HOST_KEY));
            map.put(DRIVER_KEY, smap.mapValue(DATABASE_KEY, DRIVER_KEY));
            return unmodifiableMap(map);
        } else {
            return null;
        }
    }

    /**
     * Returns the SMTP server settings.
     * <ul>
     * <li>"server" the SMTP server;</li>
     * <li>"user" optionally, the SMTP login user name;</li>
     * <li>"password" optionally, the SMTP login password;</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      smtp "tls://%h", user: "usersmtp", password: "passwordsmtp"
     * }
     * </pre>
     *
     * @return the {@link Map} with the SMTP server settings or {@code null}.
     */
    public Map<String, Object> getSmtpServer() {
        StatementsMap smap = statementsMap;
        String smtp = smap.value(SMTP_KEY);
        if (smtp != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(SERVER_KEY, smtp);
            map.put(USER_KEY, smap.mapValue(SMTP_KEY, USER_KEY));
            map.put(PASSWORD_KEY, smap.mapValue(SMTP_KEY, PASSWORD_KEY));
            return unmodifiableMap(map);
        } else {
            return null;
        }
    }

    /**
     * Returns the IMAP hosts.
     * <ul>
     * <li>name := host</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      server "Default Server", host: "mail.example.com"
     *      server "Webmail Server", host: "webmail.example.com"
     * }
     * </pre>
     *
     * @return the {@link Map} with the IMAP hosts or {@code null}.
     */
    public Map<String, String> getImapServers() {
        return statementsTable.tableKeys(SERVER_KEY, HOST_KEY);
    }

    /**
     * Returns the default IMAP host.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      server "default", host: "localhost"
     * }
     * </pre>
     *
     * @return the {@link String} host or {@code null}.
     */
    public String getImapServer() {
        StatementsTable tmap = statementsTable;
        Map<String, Object> map = tmap.tableKeys(SERVER_KEY, HOST_KEY);
        if (map != null) {
            return (String) map.get("default");
        } else {
            return null;
        }
    }

    /**
     * Returns the IMAP domains.
     * <ul>
     * <li>host := domain</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      host "example.com", domain: "mail.example.com"
     *      host "otherdomain.com", domain: "othermail.example.com"
     * }
     * </pre>
     *
     * @return the {@link Map} with the IMAP domains or {@code null}.
     */
    public Map<String, String> getImapDomains() {
        return statementsTable.tableKeys(HOST_KEY, DOMAIN_KEY);
    }

    /**
     * Returns the default IMAP domain.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      host "example.com"
     * }
     * </pre>
     *
     * @return the {@link String} the default domain or {@code null}.
     */
    public String getImapDomain() {
        Set<String> values = statementsTable.tableValues(HOST_KEY);
        if (values != null) {
            return new ArrayList<String>(values).get(0);
        } else {
            return null;
        }
    }

    public Object methodMissing(String name, Object args)
            throws StatementsException {
        try {
            service.methodMissing(name, args);
        } catch (StatementsException e) {
            statementsTable.methodMissing(name, args);
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME)
                .append(ALIAS_KEY, getAlias()).toString();
    }
}
