package com.anrisoftware.sscontrol.remote.user;

import java.io.File;
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

    private static final String GROUP_NAME = "name";

    private final Service service;

    private final String name;

    private final List<Key> keys;

    private final List<Require> requires;

    @Inject
    private UserArgsLogger log;

    @Inject
    private KeyFactory keyFactory;

    @Inject
    private GroupFactory groupFactory;

    private String password;

    private String passphrase;

    private Group group;

    private String comment;

    private Integer uid;

    private File home;

    private String login;

    /**
     * @see UserFactory#create(Service, Map)
     */
    @Inject
    User(UserArgs aargs, GroupFactory groupFactory, @Assisted Service service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.keys = new ArrayList<Key>();
        this.requires = new ArrayList<Require>();
        this.name = aargs.name(service, args);
        this.group = groupFactory.create(service, name);
        if (aargs.havePassword(args)) {
            this.password = aargs.password(service, args);
        }
        if (aargs.haveUid(args)) {
            this.uid = aargs.uid(service, args);
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

    public void group(String name) {
        Group group = groupFactory.create(service, name);
        log.groupSet(this, service, group);
        this.group = group;
    }

    public void group(Map<String, Object> args, String name) {
        args.put(GROUP_NAME, name);
        group(args);
    }

    public void group(Map<String, Object> args) {
        if (!args.containsKey(GROUP_NAME)) {
            args.put(GROUP_NAME, name);
        }
        Group group = groupFactory.create(service, args);
        log.groupSet(this, service, group);
        this.group = group;
    }

    public Group getGroup() {
        return group;
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
        this.home = new File(home);
    }

    public void setHome(File home) {
        this.home = home;
    }

    public File getHome() {
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

    public void require(Object... s) {
    }

    public void password() {
        Require require = Require.password;
        requires.add(require);
        log.addRequire(this, service, require);
    }

    public void passphrase() {
        Require require = Require.passphrase;
        requires.add(require);
        log.addRequire(this, service, require);
    }

    public List<Key> getKeys() {
        return keys;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }
}
