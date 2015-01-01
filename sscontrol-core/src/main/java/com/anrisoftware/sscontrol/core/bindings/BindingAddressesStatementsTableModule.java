package com.anrisoftware.sscontrol.core.bindings;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the binding addresses statements table.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BindingAddressesStatementsTableModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(
                BindingAddressesStatementsTable.class,
                BindingAddressesStatementsTable.class).build(
                BindingAddressesStatementsTableFactory.class));
    }

}
