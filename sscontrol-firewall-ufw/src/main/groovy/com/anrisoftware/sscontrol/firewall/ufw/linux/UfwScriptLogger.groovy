/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.linux

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link LinuxScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UfwScriptLogger extends AbstractLogger {

    static final String RULES_DEPLOYED = "Firewall rules deployed for {}, worker {}."
    static final String RULES_DEPLOYED2 = "Firewall rules deployed."

    /**
     * Create logger for {@link LinuxScript}.
     */
    UfwScriptLogger() {
        super(UfwScript.class)
    }

    void deployedRules(UfwScript script, def worker) {
        if (log.debugEnabled) {
            log.debug RULES_DEPLOYED, script, worker
        } else {
            log.info RULES_DEPLOYED2
        }
    }
}
