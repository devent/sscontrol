/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSource

/**
 * Installs and configures <i>Gitit</i> with the help of <i>hsenv</i>
 * from source for <i>Ubuntu</i> 12.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuHsenvFromSourceConfig extends HsenvFromSource {

    @Inject
    HsenvPropertiesProvider hsenvProperties

    @Override
    ContextProperties getHsenvProperties() {
        hsenvProperties.get()
    }
}
