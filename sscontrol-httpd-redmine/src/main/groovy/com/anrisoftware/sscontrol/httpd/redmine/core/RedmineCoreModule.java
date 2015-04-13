/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.core;

import static com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod.async_sendmail;
import static com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod.async_smtp;
import static com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod.sendmail;
import static com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod.smtp;
import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Redmine</i> core module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RedmineCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        MapBinder<DeliveryMethod, SetupEmailSettingsMap> mapbinder = newMapBinder(
                binder(), DeliveryMethod.class, SetupEmailSettingsMap.class);
        mapbinder.addBinding(smtp).to(SmtpSetupEmailSettingsMap.class);
        mapbinder.addBinding(async_smtp).to(
                AsyncSmtpSetupEmailSettingsMap.class);
        mapbinder.addBinding(sendmail).to(SendmailSetupEmailSettingsMap.class);
        mapbinder.addBinding(async_sendmail).to(
                AsyncSendmailSetupEmailSettingsMap.class);
    }

}
