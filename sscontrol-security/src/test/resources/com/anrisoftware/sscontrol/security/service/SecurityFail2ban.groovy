/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service

security {
    service "fail2ban", {
        debug level: 1
        jail "apache", notify: "root@localhost", {
            banning retries: 3, time: "PT10M"
        }
        jail "ssh", notify: "root@localhost", {
            ignore address: "192.0.0.1"
            banning retries: 3, time: "PT10M", backend: polling, type: deny
        }
        jail "postfix", notify: "root@localhost", {
            ignore addresses: "192.0.0.1, 192.0.0.2"
            banning retries: 3, time: "PT10M", backend: auto, type: reject
        }
    }
}
