package com.anrisoftware.sscontrol.httpd.gitit;

/**
 * Authentication method.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum AuthMethod {

    /**
     * Users will be logged in and registered using forms in the web interface.
     */
    form,

    /**
     * Users will be logged in with HTTP authentication.
     */
    http,

    /**
     * Some generic authentication method that uses the <code>REMOTE_USER</code>
     * that is set to the name of the authenticated user.
     */
    generic,

    /**
     * Attempts to log in through <a href="https://rpxnow.com">rpxnow.com</a>.
     */
    rpx,
}
