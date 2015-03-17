/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite;

import java.util.Map;

import com.anrisoftware.sscontrol.source.service.SourceServiceFactory;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;

/**
 * Factory to create the <i>Gitolite</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface GitoliteServiceFactory extends SourceServiceFactory {

    /**
     * Creates the <i>Gitolite</i> service.
     *
     * @return the {@link SourceSetupService}.
     */
    @Override
    SourceSetupService create(Map<String, Object> map);
}
