package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Roundcube SMTP server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SmtpServer {

    private static final String HOST = "host";

    private String host;

    private String user;

    private String password;

    /**
     * @see SmtpServerFactory#create(Object, Map)
     */
    @Inject
    SmtpServer(SmtpServerArgs aargs, @Assisted Object service,
            @Assisted Map<String, Object> args) {
        setHost(aargs.host(service, args));
        if (aargs.haveUser(args)) {
            setUser(aargs.user(service, args));
        }
        if (aargs.havePassword(args)) {
            setPassword(aargs.password(service, args));
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(HOST, host).toString();
    }
}
