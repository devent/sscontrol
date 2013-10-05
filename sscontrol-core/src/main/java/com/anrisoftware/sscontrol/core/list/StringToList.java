package com.anrisoftware.sscontrol.core.list;

import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.strip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Returns a list of items.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class StringToList {

	@Inject
	private StringToListPropertiesProvider properties;

	private final Object property;

	private List<String> list;

	@Inject
	StringToList(@Assisted Object property) {
		this.property = property;
	}

	/**
	 * Returns the list of the items.
	 * 
	 * @return the items of the property as {@link List}.
	 */
	public List<String> getList() {
		if (list == null) {
			synchronized (this) {
				list = createList0();
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<String> createList0() {
		List<String> list = new ArrayList<String>();
		if (property instanceof Collection) {
			list.addAll((Collection<? extends String>) property);
		} else {
			String[] str = split(property.toString(), getSeparators());
			for (String string : str) {
				list.add(strip(string));
			}
		}
		return list;
	}

	private String getSeparators() {
		return properties.get().getProperty("list_separators");
	}
}
