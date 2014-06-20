/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.proxy.linux

import static org.apache.commons.io.FileUtils.*

import org.apache.commons.math3.util.FastMath

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory
import com.anrisoftware.globalpom.format.byteformat.UnitMultiplier
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.proxy.linux.AbstractProxyConfig

/**
 * <i>Nginx</i> proxy.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractNginxProxyConfig extends AbstractProxyConfig {

    @Override
    String sizeValue(long value) {
        String u = ""
        if (value > UnitMultiplier.MEBI.value) {
            value = FastMath.round(value/UnitMultiplier.MEBI.value)
            u = "m"
        }
        if (value > UnitMultiplier.KIBI.value) {
            value = FastMath.round(value/UnitMultiplier.KIBI.value)
            u = "k"
        }
        "$value$u"
    }
}
