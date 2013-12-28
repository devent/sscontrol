package com.anrisoftware.sscontrol.remote.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.assistedinject.Assisted;

/**
 * Local user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class User {

    private final Service service;

    private final String name;

    private final List<Key> keys;

    @Inject
    private UserArgsLogger log;

    @Inject
    private KeyFactory keyFactory;

    private String password;

    private String passphrase;

    private String comment;

    private Integer uid;

    private Integer gid;

    private String home;

    private String login;

    /**
     * @see UserFactory#create(Service, Map)
     */
    @Inject
    User(UserArgs aargs, @Assisted Service service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.keys = new ArrayList<Key>();
        this.name = aargs.name(service, args);
        if (aargs.havePassword(args)) {
        this.password = aargs.password(service, args);
        }
        if (aargs.haveUid(args)) {
            this.uid = aargs.uid(service, args);
        }
        if (aargs.haveGid(args)) {
            this.gid = aargs.gid(service, args);
        }
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getGid() {
        return gid;
    }

    public void comment(String comment) {
        log.checkComment(this, service, comment);
        this.comment = comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void home(String home) {
        log.checkHome(this, service, home);
        this.home = home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getHome() {
        return home;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void passphrase(String passphrase) {
        log.checkPassphrase(service, passphrase);
        this.passphrase = passphrase;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void access(Map<String, Object> args) throws ServiceException {
        Key key = keyFactory.create(service, args);
        log.keyAdded(this, service, key);
        keys.add(key);
    }

    public List<Key> getKeys() {
        return keys;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }
}
