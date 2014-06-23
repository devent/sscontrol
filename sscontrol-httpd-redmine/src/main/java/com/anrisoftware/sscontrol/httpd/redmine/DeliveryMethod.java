package com.anrisoftware.sscontrol.httpd.redmine;

/**
 * Mail delivery method.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum DeliveryMethod {

    smtp,

    sendmail,

    async_smtp,

    async_sendmail
}
