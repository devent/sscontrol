package com.anrisoftware.sscontrol.dns.zone;

import static com.anrisoftware.sscontrol.dns.zone.ZoneTimeDurationArgs.MINIMUM;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.sscontrol.dns.time.TimeDuration;
import com.google.inject.assistedinject.Assisted;

/**
 * Zone time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ZoneTimeDuration extends TimeDuration {

	private final Duration minimum;

	/**
	 * @see ZoneTimeDurationFactory#create(DnsZone, Map)
	 */
	@Inject
	ZoneTimeDuration(ZoneTimeDurationArgs aargs, @Assisted DnsZone zone,
			@Assisted Map<String, Object> args) {
		super(zone, args);
		this.minimum = aargs.minimum(zone, args);
	}

	public Duration getMinimum() {
		return minimum;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append(MINIMUM, minimum).toString();
	}
}
