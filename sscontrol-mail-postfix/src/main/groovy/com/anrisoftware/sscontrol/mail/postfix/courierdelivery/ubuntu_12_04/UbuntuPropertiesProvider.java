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
package com.anrisoftware.sscontrol.mail.postfix.courierdelivery.ubuntu_12_04;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Courier/MySQL/delivery Ubuntu 12.04 properties provider.
 * 
 * <ul>
 * <li>{@code "/postfix_couriermysqldelivery_ubuntu_12_04.properties"}
 * </ul>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
@SuppressWarnings("serial")
public class UbuntuPropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = UbuntuPropertiesProvider.class
            .getResource("/postfix_couriermysqldelivery_ubuntu_12_04.properties");

	UbuntuPropertiesProvider() {
		super(UbuntuPropertiesProvider.class, RESOURCE);
	}
}
