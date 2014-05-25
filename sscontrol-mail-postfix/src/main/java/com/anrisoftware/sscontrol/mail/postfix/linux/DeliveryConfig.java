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
package com.anrisoftware.sscontrol.mail.postfix.linux;

import com.anrisoftware.sscontrol.mail.postfix.script.linux.BasePostfixScript;

/**
 * Delivery configuration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DeliveryConfig {

    /**
     * Returns the profile name of the storage.
     * 
     * @return the profile {@link String} name.
     */
    String getProfile();

    /**
     * Returns the delivery name.
     * 
     * @return the delivery {@link String} name.
     */
    String getDeliveryName();

    /**
     * Sets the parent script with the properties.
     * 
     * @param script
     *            the {@link BasePostfixScript}.
     */
    void setScript(BasePostfixScript script);

    /**
     * Returns the parent script with the properties.
     * 
     * @return the {@link BasePostfixScript}.
     */
    BasePostfixScript getScript();

    /**
     * Creates the configuration.
     */
    void deployDelivery();

}
