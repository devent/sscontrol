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
package com.anrisoftware.sscontrol.mail.postfix.courierdelivery.ubuntu_12_04

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

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.mail.postfix.courierdelivery.linux.CourierMysqlDeliveryConfig
import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04.Ubuntu_12_04_ScriptFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>Courier MySql Ubuntu 12.04</i> delivery.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuConfig extends CourierMysqlDeliveryConfig {

    @Inject
    UbuntuPropertiesProvider courierMysqlProperties

    @Inject
    RestartServicesFactory restartServicesFactory

    @Override
    void deployDelivery() {
        super.deployDelivery();
        restartServices()
    }

    /**
     * Restarts the Courier/Mysql services.
     */
    void restartServices() {
        restartServicesFactory.create(
                log: log,
                command: courierRestartCommand,
                services: courierServices,
                this, threads)()
    }

    /**
     * Returns the restart command.
     *
     * <ul>
     * <li>property {@code "courier_restart_command"}</li>
     * </ul>
     *
     * @see #getDeliveryProperties()
     */
    String getCourierRestartCommand() {
        profileProperty "courier_restart_command", deliveryProperties
    }

    /**
     * Returns the services to restart.
     *
     * <ul>
     * <li>property {@code "courier_restart_services"}</li>
     * </ul>
     *
     * @see #getDeliveryProperties()
     */
    List getCourierServices() {
        profileListProperty "courier_restart_services", deliveryProperties
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ScriptFactory.PROFILE_NAME
    }

    @Override
    ContextProperties getDeliveryProperties() {
        courierMysqlProperties.get()
    }
}

