package com.anrisoftware.sscontrol.hosts.utils;

import java.util.Locale;

public interface HostFormatFactory {

	HostFormat create();

	HostFormat create(Locale locale);
}
