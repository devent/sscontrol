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

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Message header.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HeaderImpl implements Header {

    private static final String ENABLED_KEY = "enabled";

    private static final String TEXT = "text";

    private static final String HEADER_KEY = "header";

    private static final String NAME_KEY = "name";

    private static final String TYPE_KEY = "type";

    private final MessageType type;

    private final String name;

    private final String text;

    private final boolean enabled;

    /**
     * @see HeaderFactory#create(Map)
     */
    @Inject
    HeaderImpl(@Assisted Map<String, Object> args) {
        this.type = (MessageType) args.get(TYPE_KEY);
        this.name = args.get(NAME_KEY).toString();
        this.text = args.get(HEADER_KEY).toString();
        if (args.containsKey(ENABLED_KEY)) {
            this.enabled = (Boolean) args.get(ENABLED_KEY);
        } else {
            this.enabled = true;
        }
    }

    @Override
    public MessageType getMessageType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(TYPE_KEY, type)
                .append(NAME_KEY, name).append(TEXT, text)
                .append(ENABLED_KEY, enabled).toString();
    }
}
