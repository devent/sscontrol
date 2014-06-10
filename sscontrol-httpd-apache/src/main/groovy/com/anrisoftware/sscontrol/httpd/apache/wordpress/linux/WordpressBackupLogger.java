package com.anrisoftware.sscontrol.httpd.apache.wordpress.linux;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link WordpressBackup}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WordpressBackupLogger extends AbstractLogger {

    /**
     * Sets the context of the logger to {@link WordpressBackup}.
     */
    public WordpressBackupLogger() {
        super(WordpressBackup.class);
    }
}
