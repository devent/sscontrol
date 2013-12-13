package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.Map;

/**
 * Factory to create the SMTP server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface SmtpServerFactory {

    /**
     * Creates the SMTP server.
     * 
     * @return the {@link SmtpServer}.
     */
    SmtpServer createDefault();

    /**
     * Creates the SMTP server.
     * 
     * @param service
     *            the Roundcube service.
     * 
     * @param args
     *            the arguments {@link Map}.
     * 
     * @return the {@link SmtpServer}.
     */
    SmtpServer create(Object service, Map<String, Object> args);
}
