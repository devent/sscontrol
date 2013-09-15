package com.anrisoftware.sscontrol.mail.resetdomains;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.google.inject.assistedinject.Assisted;

/**
 * Reset before adding the specified domains/users/aliases.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ResetDomains {

	private Map<String, Object> args;

	private ResetDomainsLogger log;

	private boolean resetDomains;

	private boolean resetUsers;

	private boolean resetAliases;

	@Inject
	ResetDomains(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setResetDomainsLogger(ResetDomainsLogger logger) {
		this.log = logger;
		setResetDomains(args.get("domains"));
		setResetUsers(args.get("users"));
		setResetAliases(args.get("aliases"));
		args = null;
	}

	private void setResetDomains(Object flag) {
		log.checkFlag(flag);
		if (flag instanceof YesNoFlag) {
			this.resetDomains = ((YesNoFlag) flag).asBoolean();
		} else {
			this.resetDomains = (Boolean) flag;
		}
		setResetUsers(resetDomains);
		setResetAliases(resetDomains);
	}

	public boolean isResetDomains() {
		return resetDomains;
	}

	private void setResetUsers(Object flag) {
		log.checkFlag(flag);
		if (flag instanceof YesNoFlag) {
			this.resetUsers = ((YesNoFlag) flag).asBoolean();
		} else {
			this.resetUsers = (Boolean) flag;
		}
		setResetAliases(resetDomains);
	}

	public boolean isResetUsers() {
		return resetUsers;
	}

	private void setResetAliases(Object flag) {
		log.checkFlag(flag);
		if (flag instanceof YesNoFlag) {
			this.resetAliases = ((YesNoFlag) flag).asBoolean();
		} else {
			this.resetAliases = (Boolean) flag;
		}
	}

	public boolean isResetAliases() {
		return resetAliases;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("reset domains", resetDomains)
				.append("reset users", resetUsers)
				.append("reset aliases", resetAliases).toString();
	}
}
