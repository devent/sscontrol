package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.google.inject.assistedinject.Assisted;

/**
 * Phpmyadmin service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PhpmyadminService implements WebService {

	public static final String NAME = "phpmyadmin";

	@Inject
	private PhpmyadminServiceLogger log;

	private boolean usePma;

	@Inject
	private AdminUser adminUser;

	@Inject
	private ControlUser controlUser;

	@Inject
	private Server server;

	@Inject
	PhpmyadminService(@Assisted Map<String, Object> map) {
		if (map.containsKey("pma")) {
			this.usePma = (Boolean) map.get("pma");
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	public void setUsePma(boolean use) {
		this.usePma = use;
	}

	public boolean isUsePma() {
		return usePma;
	}

	public void admin(Map<String, Object> map, String admin) {
		adminUser.setUser(admin);
		adminUser.setPassword(map.get("password"));
		log.adminSet(this, adminUser);
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void control(Map<String, Object> map, String user) {
		controlUser.setUser(user);
		controlUser.setPassword(map.get("password"));
		controlUser.setDatabase(map.get("database"));
		log.controlSet(this, controlUser);
	}

	public ControlUser getControlUser() {
		return controlUser;
	}

	public void server(Map<String, Object> map, String host) {
		server.setHost(host);
		server.setPort((Integer) map.get("port"));
		log.serverSet(this, server);
	}

	public Server getServer() {
		return server;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(NAME).append(adminUser)
				.append(controlUser).append(server).toString();
	}
}
