package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import static java.lang.String.format;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Roundcube database credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Database {

    private static final String DATABASE_URL = "%s://%s:%s@%s/%s";

    private static final String DATABASE = "database";

    private String provider;

    private String user;

    private String password;

    private String database;

    private String host;

    /**
     * @see DatabaseFactory#create(Object, Map)
     */
    @Inject
    Database(DatabaseArgs argss, @Assisted Object service,
            @Assisted Map<String, Object> args) {
        setProvider(argss.provider(service, args));
        if (argss.haveUser(args)) {
            setUser(argss.user(args));
        }
        if (argss.havePassword(args)) {
            setPassword(argss.password(args));
        }
        if (argss.haveHost(args)) {
            setHost(argss.host(args));
        }
        if (argss.haveDatabase(args)) {
            setDatabase(argss.database(args));
        }
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(DATABASE,
                format(DATABASE_URL, provider, user, password, host, database))
                .toString();
    }
}
