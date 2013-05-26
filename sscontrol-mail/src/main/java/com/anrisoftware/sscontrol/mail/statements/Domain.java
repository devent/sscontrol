package com.anrisoftware.sscontrol.mail.statements;

import static com.anrisoftware.sscontrol.mail.statements.Alias.DESTINATION_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * The domain where mails are received to and distributed to.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Domain {

	private final DomainLogger log;

	private final String name;

	private final List<Alias> aliases;

	private final AliasFactory aliasFactory;

	private final CatchallFactory catchallFactory;

	private boolean enabled;

	@Inject
	Domain(DomainLogger logger, AliasFactory aliasFactory,
			CatchallFactory catchallFactory, @Assisted String name) {
		this.log = logger;
		this.aliasFactory = aliasFactory;
		this.catchallFactory = catchallFactory;
		this.name = name;
		this.enabled = true;
		this.aliases = new ArrayList<Alias>();
	}

	public String getName() {
		return name;
	}

	public void catchall(Map<String, Object> args) {
		String destination = args.get("destination").toString();
		Catchall alias = catchallFactory.create(this, destination);
		log.catchallAdded(this, alias);
		aliases.add(alias);
	}

	public void alias(Map<String, Object> args, String name) {
		alias(args, name, null);
	}

	public Alias alias(Map<String, Object> args, String name, Object statements) {
		log.checkAliasName(this, name);
		log.checkDestination(this, args);
		String destination = args.get(DESTINATION_KEY).toString();
		Alias alias = aliasFactory.create(this, name, destination);
		aliases.add(alias);
		log.aliasAdded(this, alias);
		return alias;
	}

	public void user(Map<String, Object> args, String name) {
		user(args, name, null);
	}

	public Domain user(Map<String, Object> args, String name, Object statements) {
		return this;
	}

	public Domain enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("enabled", enabled).toString();
	}
}
