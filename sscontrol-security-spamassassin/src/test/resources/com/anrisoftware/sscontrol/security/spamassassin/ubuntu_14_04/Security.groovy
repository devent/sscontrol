/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin.ubuntu_14_04

import com.anrisoftware.sscontrol.security.spamassassin.ClearType
import com.anrisoftware.sscontrol.security.spamassassin.MessageType
import com.anrisoftware.sscontrol.security.spamassassin.RewriteType

security {
    service "spamassassin", {
        debug "log", level: 1
        clear ClearType.headers
        rewrite RewriteType.subject, header: "*SPAM*"
        add MessageType.spam, name: "Flag", header: "_YESNOCAPS_"
        add MessageType.all, name: "Level", header: "_STARS(*)_"
        add MessageType.all, name: "Status", header: "_YESNO_, score=_SCORE_"
        trusted networks: "192.168.0.40"
        spam score: 5.0
    }
}
