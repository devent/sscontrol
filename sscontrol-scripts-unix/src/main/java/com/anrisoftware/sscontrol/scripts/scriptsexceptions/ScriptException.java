/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.scriptsexceptions;

import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Indicates an error with a script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ScriptException extends Exception {

    private final Context<ScriptException> context;

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public ScriptException(String message, Throwable cause) {
        super(message, cause);
        this.context = new Context<ScriptException>(this);
    }

    /**
     * @see Exception#Exception(String)
     */
    public ScriptException(String message) {
        super(message);
        this.context = new Context<ScriptException>(this);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public ScriptException(Object message, Throwable cause) {
        super(message.toString(), cause);
        this.context = new Context<ScriptException>(this);
    }

    /**
     * @see Exception#Exception(String)
     */
    public ScriptException(Object message) {
        super(message.toString());
        this.context = new Context<ScriptException>(this);
    }

    /**
     * @see Context#addContext(String, Object)
     */
    public ScriptException add(String name, Object value) {
        context.addContext(name, value);
        return this;
    }

    /**
     * @see Context#addContext(String, Object)
     */
    public ScriptException add(Object name, Object value) {
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
