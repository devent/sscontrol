package com.anrisoftware.sscontrol.httpd.domain;

import com.anrisoftware.sscontrol.httpd.user.DomainUser;

/**
 * Domain entry.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Domain {

    /**
     * Returns the domain name.
     * 
     * @return the domain {@link String} name.
     */
    String getName();

    /**
     * Returns the domain local user.
     * 
     * @param user
     *            the {@link DomainUser}.
     */
    DomainUser getDomainUser();

}
