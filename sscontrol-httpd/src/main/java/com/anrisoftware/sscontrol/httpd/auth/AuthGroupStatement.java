package com.anrisoftware.sscontrol.httpd.auth;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * Authentication group statement.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum AuthGroupStatement {

    NAME_KEY,

    UPDATE_KEY,

    USER_KEY,

    PASSWORD_KEY,

    REQUIRE_KEY,

    VALID_KEY,

    EXCEPT_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }
}
