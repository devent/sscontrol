package com.anrisoftware.sscontrol.core.bindings;

/**
 * Factory to create a binding.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface BindingFactory {

    /**
     * Creates a binding.
     * 
     * @return the {@link Binding}.
     */
    Binding create();
}
