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
package com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import groovy.lang.Script;

import com.anrisoftware.sscontrol.mail.postfix.courierdelivery.ubuntu_12_04.Ubuntu_12_04_CourierMysqlDeliveryModule;
import com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_12_04.Ubuntu_12_04_HashStorageModule;
import com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_12_04.Ubuntu_12_04_MysqlStorageModule;
import com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.ubuntu_12_04.Ubuntu_12_04_SaslMysqlAuthModule;
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModule;
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds Postfix/Ubuntu 12.04 scripts.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        bindScripts();
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.UnixScriptsDefaultsModule());
        install(new ChangeFileModule());
        install(new LocalUserModule());
        install(new Ubuntu_12_04_HashStorageModule());
        install(new Ubuntu_12_04_MysqlStorageModule());
        install(new Ubuntu_12_04_SaslMysqlAuthModule());
        install(new Ubuntu_12_04_CourierMysqlDeliveryModule());
    }

    private void bindScripts() {
        MapBinder<String, Script> binder;
        binder = newMapBinder(binder(), String.class, Script.class);
        binder.addBinding("postfix.ubuntu_12_04").to(UbuntuScript.class);
    }
}
