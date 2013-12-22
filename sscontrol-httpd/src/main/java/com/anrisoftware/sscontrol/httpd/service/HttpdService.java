package com.anrisoftware.sscontrol.httpd.service;

import java.util.List;
import java.util.Set;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Httpd service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface HttpdService extends Service {

    /**
     * Returns the httpd service name.
     */
    @Override
    String getName();

    /**
     * Returns a list of the IP addresses where to bind the DNS service.
     * 
     * @return the {@link Binding}.
     */
    Binding getBinding();

    /**
     * Returns all domains of the service.
     *
     * @return the {@link List} of the {@link Domain} domains.
     */
    List<Domain> getDomains();

    /**
     * Returns a set of the virtual domains of the service.
     *
     * @return the {@link Set} of the virtual {@link Domain} domains.
     */
    Set<Domain> getVirtualDomains();

    /**
     * Returns debug logging.
     * 
     * @return the {@link DebugLogging}.
     */
    DebugLogging getDebug();

}
