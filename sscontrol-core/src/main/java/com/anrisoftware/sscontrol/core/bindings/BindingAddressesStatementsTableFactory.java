package com.anrisoftware.sscontrol.core.bindings;

/**
 * Factory to create the binding addresses.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface BindingAddressesStatementsTableFactory {

    /**
     * Creates the binding addresses.
     *
     * @param service
     *            the {@link Object} service.
     *
     * @param name
     *            the {@link String} the service name.
     *
     * @return the {@link BindingAddressesStatementsTable}.
     */
    BindingAddressesStatementsTable create(Object service, String name);
}
