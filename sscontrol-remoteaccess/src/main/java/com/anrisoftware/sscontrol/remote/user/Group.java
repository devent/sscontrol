package com.anrisoftware.sscontrol.remote.user;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Local user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Group {

    private final Service service;

    private final String name;

    private Integer gid;

    /**
     * @see GroupFactory#create(Service, Map)
     */
    @AssistedInject
    Group(@Assisted Service service, @Assisted String name) {
        this.service = service;
        this.name = name;
    }

    /**
     * @see GroupFactory#create(Service, Map)
     */
    @AssistedInject
    Group(GroupArgs aargs, @Assisted Service service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.name = aargs.name(service, args);
        if (aargs.haveGid(args)) {
            this.gid = aargs.gid(service, args);
        }
    }

    public Service getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public Integer getGid() {
        return gid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }
}
