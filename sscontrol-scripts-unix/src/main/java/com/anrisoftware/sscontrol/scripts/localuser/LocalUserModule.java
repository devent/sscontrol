/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.localuser;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @see LocalChangeGroupFactory
 * @see LocalChangePasswordFactory
 * @see LocalChangeUserFactory
 * @see LocalGroupAddFactory
 * @see LocalUserAddFactory
 * @see LocalUserInfoFactory
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class LocalUserModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(LocalChangeGroup.class,
                LocalChangeGroup.class).build(LocalChangeGroupFactory.class));
        install(new FactoryModuleBuilder().implement(LocalChangePassword.class,
                LocalChangePassword.class).build(
                LocalChangePasswordFactory.class));
        install(new FactoryModuleBuilder().implement(LocalChangeUser.class,
                LocalChangeUser.class).build(LocalChangeUserFactory.class));
        install(new FactoryModuleBuilder().implement(LocalGroupAdd.class,
                LocalGroupAdd.class).build(LocalGroupAddFactory.class));
        install(new FactoryModuleBuilder().implement(LocalUserAdd.class,
                LocalUserAdd.class).build(LocalUserAddFactory.class));
        install(new FactoryModuleBuilder().implement(LocalUserInfo.class,
                LocalUserInfo.class).build(LocalUserInfoFactory.class));
    }

}
