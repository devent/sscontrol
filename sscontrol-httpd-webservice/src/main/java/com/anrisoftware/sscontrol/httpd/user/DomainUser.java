package com.anrisoftware.sscontrol.httpd.user;

/**
 * Domain user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DomainUser {

    String getRefDomain();

    String getName();

    String getGroup();

    Integer getUid();

    Integer getGid();

}
