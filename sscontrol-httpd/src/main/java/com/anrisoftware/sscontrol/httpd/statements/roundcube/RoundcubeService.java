package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.database.DatabaseArgs;
import com.anrisoftware.sscontrol.core.database.DatabaseFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.google.inject.assistedinject.Assisted;

/**
 * Roundcube service.
 * 
 * @see <a href=http://roundcube.net/>http://roundcube.net/</a>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RoundcubeService implements WebService {

    private static final String NAME1 = "name";

    public static final String NAME = "roundcube";

    private static final String ALIAS = "alias";

    private final RoundcubeServiceLogger log;

    private final List<Host> hosts;

    @Inject
    private DatabaseFactory databaseFactory;

    @Inject
    private HostFactory hostFactory;

    @Inject
    private DebugLoggingFactory debugFactory;

    private SmtpServerFactory smtpFactory;

    private String alias;

    private Database database;

    private DebugLogging debug;

    private SmtpServer smtp;

    /**
     * @see RoundcubeServiceFactory#create(Map)
     */
    @Inject
    RoundcubeService(RoundcubeServiceArgs argss, RoundcubeServiceLogger log,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.hosts = new ArrayList<Host>();
        if (argss.haveAlias(args)) {
            setAlias(argss.alias(args));
        }
    }

    @Inject
    void setSmtpServerFactory(SmtpServerFactory factory) {
        this.smtp = factory.createDefault();
        this.smtpFactory = factory;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        log.aliasSet(this, alias);
    }

    public String getAlias() {
        return alias;
    }

    public void database(Map<String, Object> args, String name) {
        args.put(DatabaseArgs.DATABASE, name);
        Database database = databaseFactory.create(this, args);
        log.databaseSet(this, database);
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public void host(String name) {
        host(new HashMap<String, Object>(), name);
    }

    public void host(Map<String, Object> args, String name) {
        args.put(HostArgs.HOST, name);
        Host host = hostFactory.create(this, args);
        log.hostAdded(this, host);
        hosts.add(host);
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void debug(Map<String, Object> args) {
        DebugLogging logging = debugFactory.create(args);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public void debug(int level) {
        DebugLogging logging = debugFactory.create(level);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public DebugLogging getDebugLogging() {
        if (debug == null) {
            this.debug = debugFactory.createOff();
        }
        return debug;
    }

    public void smtp(String host) {
        smtp(new HashMap<String, Object>(), host);
    }

    public void smtp(Map<String, Object> args, String host) {
        args.put(SmtpServerArgs.HOST, host);
        SmtpServer smtp = smtpFactory.create(this, args);
        log.smtpSet(this, smtp);
        this.smtp = smtp;
    }

    public SmtpServer getSmtp() {
        return smtp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME1, NAME)
                .append(ALIAS, alias).toString();
    }
}
