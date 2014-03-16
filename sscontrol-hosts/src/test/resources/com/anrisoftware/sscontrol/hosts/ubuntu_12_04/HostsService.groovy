/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.ubuntu_12_04

hosts {
    ip "192.168.0.49", host: "srv1.ubuntutest.com", alias: "srv1"
    ip "192.168.0.50", host: "srv1.ubuntutest.org", alias: "srva, srvb"
    ip "192.168.0.51", host: "srv1.ubuntutest.me"
    ip "192.168.0.52", host: "srv1.ubuntutest.xx"
}
