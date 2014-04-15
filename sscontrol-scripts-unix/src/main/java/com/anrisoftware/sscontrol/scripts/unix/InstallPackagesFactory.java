package com.anrisoftware.sscontrol.scripts.unix;

import java.util.Map;

import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * Factory to create the install packages.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface InstallPackagesFactory {

    /**
     * Create the install packages.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @param parent
     *            the {@link Object} parent script.
     * 
     * @param threads
     *            the {@link Threads} pool.
     * 
     * @return the {@link InstallPackages}.
     */
    InstallPackages create(Map<String, Object> args, Object parent,
            Threads threads);
}
