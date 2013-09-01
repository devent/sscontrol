package com.anrisoftware.sscontrol.httpd.apache.linux;

import static com.anrisoftware.sscontrol.httpd.apache.linux.FileAuthProviderLogger._.auth_users_deploy1;
import static com.anrisoftware.sscontrol.httpd.apache.linux.FileAuthProviderLogger._.auth_users_deploy2;
import static com.anrisoftware.sscontrol.httpd.apache.linux.FileAuthProviderLogger._.auth_users_deploy3;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.statements.auth.Auth;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging messages for {@link FileAuthProvider}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class FileAuthProviderLogger extends AbstractLogger {

	enum _ {

		auth_users_deploy1("Deploy auth users {} in {}, worker {}."),

		auth_users_deploy2("Deploy auth users {} in {}."),

		auth_users_deploy3("Deploy auth users for auth '{}'.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link FileAuthProvider}.
	 */
	public FileAuthProviderLogger() {
		super(FileAuthProvider.class);
	}

	void deployAuthUsers(LinuxScript script, ScriptCommandWorker worker,
			Auth auth) {
		if (isTraceEnabled()) {
			trace(auth_users_deploy1, auth, script, worker);
		} else if (isDebugEnabled()) {
			debug(auth_users_deploy2, auth, script);
		} else {
			info(auth_users_deploy3, auth.getName());
		}
	}
}
