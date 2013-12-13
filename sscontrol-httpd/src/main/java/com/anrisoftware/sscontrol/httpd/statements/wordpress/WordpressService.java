package com.anrisoftware.sscontrol.httpd.statements.wordpress;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.database.DatabaseArgs;
import com.anrisoftware.sscontrol.core.database.DatabaseFactory;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.google.inject.assistedinject.Assisted;

/**
 * Wordpress service.
 * 
 * @see <a href='http://wordpress.org/">http://wordpress.org/</a>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WordpressService implements WebService {

    private static final String NAME1 = "name";

    public static final String NAME = "wordpress";

    private static final String ALIAS = "alias";

    private final WordpressServiceLogger log;

    @Inject
    private DatabaseFactory databaseFactory;

    private String alias;

    private Database database;

    /**
     * @see WordpressServiceFactory#create(Map)
     */
    @Inject
    WordpressService(WordpressServiceArgs argss, WordpressServiceLogger log,
            @Assisted Map<String, Object> args) {
        this.log = log;
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
        args.put(DatabaseArgs.DATABASE, name);
        Database database = databaseFactory.create(this, args);
        log.databaseSet(this, database);
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME1, NAME)
                .append(ALIAS, alias).toString();
    }
}