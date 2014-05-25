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
package com.anrisoftware.sscontrol.mail.postfix.openldapstorage.ubuntu_10_04;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * MySQL/storage Ubuntu 10.04 properties provider.
 * 
 * <ul>
 * <li>{@code /postfix_mysql_ubuntu_10_04.properties}
 * </ul>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
@SuppressWarnings("serial")
public class OpenldapStoragePropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = OpenldapStoragePropertiesProvider.class
			.getResource("/postfix_mysqlstorage_ubuntu_10_04.properties");

	OpenldapStoragePropertiesProvider() {
		super(OpenldapStoragePropertiesProvider.class, RESOURCE);
	}
}
