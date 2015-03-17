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

import java.net.URI;
import java.util.Map;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;

/**
 * <i>Gitolite</i> service.
 *
 * @see <a href="http://gitolite.com/">http://gitolite.com/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface GitoliteService extends SourceSetupService {

    /**
     * Returns the override mode.
     *
     * <pre>
     * source {
     *     setup "gitolite", {
     *         override mode: update
     *     }
     * }
     * </pre>
     *
     * @return the override {@link OverrideMode} mode or {@code null}.
     */
    OverrideMode getOverrideMode();

    /**
     * Returns the service prefix path.
     *
     * <pre>
     * source {
     *     setup "gitolite", {
     *         prefix path: "~/bin"
     *     }
     * }
     * </pre>
     *
     * @return the prefix {@link String} path or {@code null}.
     */
    String getPrefix();

    /**
     * Returns the service local user.
     *
     * <ul>
     * <li>{@code "user"}, the user {@link String} name;</li>
     * <li>{@code "group"}, the group {@link String} name;</li>
     * <li>{@code "uid"}, the user {@link Integer} ID;</li>
     * <li>{@code "gid"}, the group {@link Integer} ID;</li>
     * </ul>
     *
     * <pre>
     * source {
     *     setup "gitolite", {
     *         user "git", group: "git", uid: 99, gid: 99
     *     }
     * }
     * </pre>
     *
     * @return the {@link Map} of the local user attributes or {@code null}.
     */
    Map<String, Object> getUser();

    /**
     * Returns the administrator public key resource.
     * <p>
     *
     * <pre>
     * source {
     *     setup "gitolite", {
     *         admin key: "yourname.pub"
     *     }
     * }
     * </pre>
     *
     * @return the {@link URI} of the resource or {@code null}.
     */
    URI getAdminKey();

}
