package com.anrisoftware.sscontrol.httpd.statements.wordpress;

import java.util.Map;

/**
 * Parses arguments for Wordpress service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WordpressServiceArgs {

    private static final String ALIAS = "alias";

    boolean haveAlias(Map<String, Object> args) {
        return args.containsKey(ALIAS);
    }

    String alias(Map<String, Object> args) {
        return args.get(ALIAS).toString();
    }

}
