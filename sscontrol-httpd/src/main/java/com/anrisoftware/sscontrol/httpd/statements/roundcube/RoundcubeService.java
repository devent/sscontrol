package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import static com.anrisoftware.sscontrol.httpd.statements.roundcube.DatabaseArgs.DATABASE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

    public static final String NAME = "roundcube";

    private final RoundcubeServiceLogger log;

    private final List<Host> hosts;

    @Inject
    private DatabaseFactory databaseFactory;

    @Inject
    private HostFactory hostFactory;

    private String alias;

    private Database database;

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
        args.put(DATABASE, name);
        Database database = databaseFactory.create(args);
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
}
