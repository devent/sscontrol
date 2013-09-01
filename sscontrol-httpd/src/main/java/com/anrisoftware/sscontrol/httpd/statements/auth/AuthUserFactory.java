package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.Map;

public interface AuthUserFactory {

	AuthUser create(Map<String, Object> map, String name);
}
