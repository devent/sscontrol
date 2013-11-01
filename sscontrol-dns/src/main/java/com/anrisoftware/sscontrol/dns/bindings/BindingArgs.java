package com.anrisoftware.sscontrol.dns.bindings;

import static org.apache.commons.lang3.StringUtils.trim;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;

/**
 * Parses binding arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BindingArgs {

	public static final String ADDRESS = "address";

	public static final String ADDRESSES = "addresses";

	@Inject
	private BindingLogger log;

	@Inject
	private StringToListFactory listFactory;

	public String address(Map<String, Object> args) {
		Object object = args.get(ADDRESS);
		log.checkAddress(object);
		return trim(object.toString());
	}

	public Collection<String> addresses(Map<String, Object> args) {
		Object object = args.get(ADDRESSES);
		log.checkAddress(object);
		return listFactory.create(object).getList();
	}
}
