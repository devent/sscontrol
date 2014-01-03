/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.api;

import java.util.Collection;
import java.util.List;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.mail.certificate.Certificate;
import com.anrisoftware.sscontrol.mail.resetdomains.ResetDomains;
import com.anrisoftware.sscontrol.mail.statements.Database;
import com.anrisoftware.sscontrol.mail.statements.Domain;
import com.anrisoftware.sscontrol.mail.statements.MasqueradeDomains;

/**
 * Mail service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface MailService extends Service {

    /**
     * Returns the mail service name.
     */
    @Override
    public String getName();

    Binding getBinding();

    void setDebug(DebugLogging debug);

    DebugLogging getDebug();

    /**
     * Returns the relay host.
     * 
     * @return the relay host {@link String} or {@code null}.
     */
    String getRelayHost();

    /**
     * Returns the domain name of the server.
     * 
     * @return the domain name of the server.
     */
    String getDomainName();

    /**
     * The domain name that locally-posted mail appears to come from, and that
     * locally posted mail is delivered to.
     * 
     * @return the origin domain name.
     */
    String getOrigin();

    /**
     * Returns the list of additional domains that are delivered to local mail
     * users.
     * 
     * @return the {@link Collection} of domains.
     */
    Collection<String> getDestinations();

    /**
     * Returns the domains that should be stripped of the sub-domains.
     * 
     * @return the {@link MasqueradeDomains}.
     */
    MasqueradeDomains getMasqueradeDomains();

    /**
     * Returns the certificate files for TLS.
     * 
     * @return the {@link Certificate}.
     */
    Certificate getCertificate();

    /**
     * Returns the to the mail service known domains list.
     * 
     * @return an unmodifiable {@link List} of {@link Domain} domains.
     */
    List<Domain> getDomains();

    ResetDomains getResetDomains();

    Database getDatabase();

}
