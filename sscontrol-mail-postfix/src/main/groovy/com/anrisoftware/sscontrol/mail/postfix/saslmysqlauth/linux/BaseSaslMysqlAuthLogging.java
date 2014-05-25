/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux;

import static com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux.BaseSaslMysqlAuthLogger._.smtpd_deployed_debug;
import static com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux.BaseSaslMysqlAuthLogger._.smtpd_deployed_info;
import static com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux.BaseSaslMysqlAuthLogger._.smtpd_deployed_trace;
import static com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux.BaseSaslMysqlAuthLogger._.smtpd_pam_deployed_debug;
import static com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux.BaseSaslMysqlAuthLogger._.smtpd_pam_deployed_info;
import static com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux.BaseSaslMysqlAuthLogger._.smtpd_pam_deployed_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging for {@link BaseSaslMysqlAuth}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BaseSaslMysqlAuthLogger extends AbstractLogger {

    enum _ {

        smtpd_deployed_trace(
                "SMTPD configuration deployed to {} for {}: \n>>>\n{}<<<"),

        smtpd_deployed_debug("SMTPD configuration deployed to {} for {}."),

        smtpd_deployed_info(
                "SMTPD configuration deployed to {} for service '{}'."),

        smtpd_pam_deployed_trace(
                "SMTPD PAM configuration deployed to {} for {}: \n>>>n{}<<<"),

        smtpd_pam_deployed_debug(
                "SMTPD PAM configuration deployed to {} for {}."),

        smtpd_pam_deployed_info(
                "SMTPD PAM configuration deployed to {} for service '{}'.");

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
     * Sets the context of the logger to {@link BaseSaslMysqlAuth}.
     */
    public BaseSaslMysqlAuthLogger() {
        super(BaseSaslMysqlAuth.class);
    }

    void smtpdDeployed(LinuxScript script, File file, String config) {
        if (isTraceEnabled()) {
            trace(smtpd_deployed_trace, file, script, config);
        } else if (isDebugEnabled()) {
            debug(smtpd_deployed_debug, file, script);
        } else {
            info(smtpd_deployed_info, file, script.getName());
        }
    }

    void smtpdPamDeployed(LinuxScript script, File file, String config) {
        if (isTraceEnabled()) {
            trace(smtpd_pam_deployed_trace, file, script, config);
        } else if (isDebugEnabled()) {
            debug(smtpd_pam_deployed_debug, file, script);
        } else {
            info(smtpd_pam_deployed_info, file, script.getName());
        }
    }
}
