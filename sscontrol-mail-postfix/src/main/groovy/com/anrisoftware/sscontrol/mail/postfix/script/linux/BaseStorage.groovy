/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.postfix.script.linux

import com.anrisoftware.propertiesutils.ContextProperties


/**
 * Sets the parent script for the storage.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseStorage {

    BasePostfixScript script

    void setScript(BasePostfixScript script) {
        this.script = script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    /**
     * Returns the storage/properties.
     */
    abstract ContextProperties getStorageProperties()

    /**
     * Returns the absolute path of the alias domains hash table file.
     * If the path is not absolute then it is assume to be under
     * the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "virtual_domains_file"}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     * @see #getStorageProperties()
     */
    File getVirtualDomainsFile() {
        profileFileProperty "virtual_domains_file", script.configurationDir, storageProperties
    }

    /**
     * Returns the absolute path of the alias maps hash table file.
     * If the path is not absolute then it is assume to be under
     * the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "virtual_alias_file"}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     * @see #getStorageProperties()
     */
    File getVirtualAliasFile() {
        profileFileProperty "virtual_alias_file", script.configurationDir, storageProperties
    }

    /**
     * Returns the absolute path of the virtual mailbox maps hash table file.
     * If the path is not absolute then it is assume to be under
     * the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "virtual_mailbox_file"}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     */
    File getVirtualMailboxFile() {
        profileFileProperty "virtual_mailbox_file", script.configurationDir, storageProperties
    }
}
