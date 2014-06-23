package com.anrisoftware.sscontrol.httpd.redmine;

/**
 * Email authentication methods.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum AuthenticationMethod {

    none,

    plain,

    login,

    cram_md5
}
