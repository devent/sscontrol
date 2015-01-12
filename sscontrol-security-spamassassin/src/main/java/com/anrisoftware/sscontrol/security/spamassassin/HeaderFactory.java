/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin;

import java.util.Map;

/**
 * Factory to create the message header.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface HeaderFactory {

    /**
     * Creates the message header.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>type, the message {@link MessageType} type;</li>
     *            <li>name, the header {@link String} name;</li>
     *            <li>text, the header {@link String} text;</li>
     *            <li>enabled, {@code true} if the header is enabled;</li>
     *            </ul>
     *
     * @return the {@link Header} header.
     */
    Header create(Map<String, Object> args);
}
