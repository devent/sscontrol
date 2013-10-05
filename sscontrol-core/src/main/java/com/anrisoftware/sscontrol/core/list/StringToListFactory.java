package com.anrisoftware.sscontrol.core.list;

/**
 * Factory to create the list of the items.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface StringToListFactory {

	/**
	 * Creates the list of the items from the specified property.
	 * 
	 * @param property
	 *            the property value which items should be returned as list.
	 * 
	 * @return the {@link StringToList}.
	 */
	StringToList create(Object property);
}
