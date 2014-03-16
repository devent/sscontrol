/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-api.
 *
 * sscontrol-api is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-api is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-api. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.api;

/**
 * Pre-script that can configure the script compile before the script is loaded.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServicePreScript {

    /**
     * Configures the specified compiler.
     * 
     * @param compiler
     *            the compiler.
     * 
     * @throws Exception
     *             if some error occur.
     */
    void configureCompiler(Object compiler) throws Exception;

}
