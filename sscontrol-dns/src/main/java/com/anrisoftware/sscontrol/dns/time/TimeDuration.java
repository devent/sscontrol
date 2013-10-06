package com.anrisoftware.sscontrol.dns.time;

import static com.anrisoftware.sscontrol.dns.time.TimeDurationArgs.DURATION;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.google.inject.assistedinject.Assisted;

/**
 * Time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class TimeDuration {

	private final Object parent;

	private Duration duration;

	private Map<String, Object> args;

	/**
	 * @see TimeDurationFactory#create(Map)
	 */
	@Inject
	protected TimeDuration(@Assisted Object parent,
			@Assisted Map<String, Object> args) {
		this.parent = parent;
		this.args = args;
	}

	@Inject
	void setTimeDurationArgs(TimeDurationArgs aargs) {
		this.duration = aargs.duration(parent, args);
		this.args = null;
	}

	/**
	 * Returns the time duration.
	 * 
	 * @return the {@link Duration}.
	 */
	public Duration getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(DURATION, duration).toString();
	}
}
