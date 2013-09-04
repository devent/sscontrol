package com.anrisoftware.sscontrol.httpd.apache.linux;

import static com.anrisoftware.sscontrol.httpd.apache.linux.PhpmyadminConfigLogger._.reconfigure_service_debug;
import static com.anrisoftware.sscontrol.httpd.apache.linux.PhpmyadminConfigLogger._.reconfigure_service_info;
import static com.anrisoftware.sscontrol.httpd.apache.linux.PhpmyadminConfigLogger._.reconfigure_service_trace;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging messages for {@link PhpmyadminConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PhpmyadminConfigLogger extends AbstractLogger {

	enum _ {

		reconfigure_service_trace("Reconfigure phpmyadmin for {}, {}."),

		reconfigure_service_debug("Reconfigure phpmyadmin for {}, {}."),

		reconfigure_service_info("Reconfigure phpmyadmin for service '{}'.");

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
	 * Creates a logger for {@link PhpmyadminConfig}.
	 */
	public PhpmyadminConfigLogger() {
		super(PhpmyadminConfig.class);
	}

	void reconfigureService(LinuxScript script, Object worker) {
		if (isTraceEnabled()) {
			trace(reconfigure_service_trace, script, worker);
		} else if (isDebugEnabled()) {
			debug(reconfigure_service_debug, script);
		} else {
			info(reconfigure_service_info, script.getName());
		}
	}
}
