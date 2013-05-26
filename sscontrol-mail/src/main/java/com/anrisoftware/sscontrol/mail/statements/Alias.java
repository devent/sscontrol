package com.anrisoftware.sscontrol.mail.statements;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Alias for an user of the domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DomainAlias {

	private final String name;

	private final String destination;

	private boolean enabled;

	@Inject
	DomainAlias(@Assisted("name") String name,
			@Assisted("destination") String destination) {
		this.name = name;
		this.destination = destination;
		this.enabled = true;
	}

	public String getName() {
		return name;
	}

	public String getDestination() {
		return destination;
	}

	public DomainAlias enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("alias", name)
				.append("destination", destination).append("enabled", enabled)
				.toString();
	}
}
