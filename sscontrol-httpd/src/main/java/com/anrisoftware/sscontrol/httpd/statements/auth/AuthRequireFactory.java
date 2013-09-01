package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.Map;

public interface AuthRequireFactory {

	AuthRequireValidUser validUser();

	AuthRequireGroup group(Map<String, Object> map);
}
