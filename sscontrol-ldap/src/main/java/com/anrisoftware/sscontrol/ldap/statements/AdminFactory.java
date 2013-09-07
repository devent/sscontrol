package com.anrisoftware.sscontrol.ldap.statements;

import java.util.Map;

public interface AdminFactory {

	Admin create(Map<String, Object> args, String name);
}
