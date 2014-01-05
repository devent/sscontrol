/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.saslauth.linux;

import static com.anrisoftware.sscontrol.mail.postfix.saslauth.linux.BaseSaslAuthLogger._.chroot_directory_created_debug;
import static com.anrisoftware.sscontrol.mail.postfix.saslauth.linux.BaseSaslAuthLogger._.chroot_directory_created_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging for {@link BaseSaslAuth}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BaseSaslAuthLogger extends AbstractLogger {

    enum _ {

        chroot_directory_created_debug(
                "SASL chroot directory '{}' created for {}."),

        chroot_directory_created_info(
                "SASL chroot directory '{}' created for service '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link BaseSaslAuth}.
     */
    public BaseSaslAuthLogger() {
        super(BaseSaslAuth.class);
    }

    void chrootDirectoryCreated(LinuxScript script, File dir) {
        if (isDebugEnabled()) {
            debug(chroot_directory_created_debug, dir, script);
        } else {
            info(chroot_directory_created_info, dir, script.getName());
        }
    }
}
