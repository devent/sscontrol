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
package com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import groovy.lang.Script;

import com.anrisoftware.sscontrol.mail.postfix.courierdelivery.ubuntu_10_04.UbuntuCourierMysqlDeliveryConfig;
import com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04.UbuntuHashStorageConfig;
import com.anrisoftware.sscontrol.mail.postfix.linux.DeliveryConfig;
import com.anrisoftware.sscontrol.mail.postfix.linux.StorageConfig;
import com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_10_04.UbuntuMysqlStorageConfig;
import com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.ubuntu_10_04.Ubuntu_10_04_SaslMysqlAuthModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds Postfix/Ubuntu 10.04 scripts.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

	@Override
	protected void configure() {
		bindScripts();
		bindStorage();
		bindDelivery();
        install(new Ubuntu_10_04_SaslMysqlAuthModule());
	}

	private void bindScripts() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding("postfix.ubuntu_10_04").to(UbuntuScript.class);
	}

	private void bindStorage() {
		MapBinder<String, StorageConfig> binder;
		binder = newMapBinder(binder(), String.class, StorageConfig.class);
		binder.addBinding("hash.ubuntu_10_04")
				.to(UbuntuHashStorageConfig.class);
		binder.addBinding("mysql.ubuntu_10_04").to(
				UbuntuMysqlStorageConfig.class);
	}

	private void bindDelivery() {
		MapBinder<String, DeliveryConfig> binder;
		binder = newMapBinder(binder(), String.class, DeliveryConfig.class);
		binder.addBinding("courier.mysql.ubuntu_10_04").to(
				UbuntuCourierMysqlDeliveryConfig.class);
	}
}
