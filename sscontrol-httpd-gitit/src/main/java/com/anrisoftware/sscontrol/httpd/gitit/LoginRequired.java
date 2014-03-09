package com.anrisoftware.sscontrol.httpd.gitit;

/**
 * For what action for a page log-in is required.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum LoginRequired {

    /**
     * Log-in is not required for editing or reading pages.
     */
    none,

    /**
     * Log-in is required for editing pages.
     */
    modify,

    /**
     * Log-in is required for reading pages.
     */
    read
}
