/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.groovy;

import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Exception thrown on unknown statement or invalid statement key.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class StatementsException extends RuntimeException {

    private final Context<StatementsException> context;

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public StatementsException(String message, Throwable cause) {
        super(message, cause);
        this.context = new Context<StatementsException>(this);
    }

    /**
     * @see Exception#Exception(String)
     */
    public StatementsException(String message) {
        super(message);
        this.context = new Context<StatementsException>(this);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public StatementsException(Object message, Throwable cause) {
        super(message.toString(), cause);
        this.context = new Context<StatementsException>(this);
    }

    /**
     * @see Exception#Exception(String)
     */
    public StatementsException(Object message) {
        super(message.toString());
        this.context = new Context<StatementsException>(this);
    }

    /**
     * @see Context#addContext(String, Object)
     */
    public StatementsException add(String name, Object value) {
        context.addContext(name, value);
        return this;
    }

    /**
     * @see Context#addContext(String, Object)
     */
    public StatementsException add(Object name, Object value) {
        context.addContext(name.toString(), value);
        return this;
    }

    /**
     * @see Context#getContext()
     */
    public Map<String, Object> getContext() {
        return context.getContext();
    }

    @Override
    public String getMessage() {
        return context.message(super.getMessage());
    }

    @Override
    public String getLocalizedMessage() {
        return context.localizedMessage(super.getMessage());
    }

    @Override
    public String toString() {
        return context.toString();
    }
}
