package com.anrisoftware.sscontrol.remote.service;

import java.util.List;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.remote.user.User;

/**
 * Remote access service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RemoteService extends Service {

    /**
     * Returns the remote access service name.
     */
    @Override
    String getName();

    /**
     * Returns the local users.
     * 
     * @return the {@link List} of local {@link User} users.
     */
    List<User> getUsers();

    /**
     * Returns a list of the IP addresses where to bind the remote service.
     * 
     * @return the {@link Binding}.
     */
    Binding getBinding();

}
