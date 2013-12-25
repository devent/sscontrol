/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-profile.
 *
 * sscontrol-profile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-profile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-profile. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.profile

foo = "test"

profile "ubuntu_10.04", {
    hosts {
        install_command "aptitude update && aptitude install %s"
        echo_command "echo"
        set_enabled true
        set_gstring "gstring $foo"
        set_multiple "aaa", "bbb"
        set_number 11
        set_method_enabled()
        property_with_variables "$one $two $three"
    }
    hostname {
        install_command "aptitude update && aptitude install %s"
        echo_command "echo"
    }
    httpd {
        service([
            "idapache2": "apache",
            "idproxy": "nginx"
        ])
    }
}
