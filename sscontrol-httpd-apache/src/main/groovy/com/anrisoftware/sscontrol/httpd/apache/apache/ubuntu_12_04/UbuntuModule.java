/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04;

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04.Ubuntu_12_04_ScriptFactory.NAME;
import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04.Ubuntu_12_04_ScriptFactory.PROFILE;
import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.String.format;
import groovy.lang.Script;

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashModule;
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheFcgiConfig;
import com.anrisoftware.sscontrol.httpd.apache.authfile.ubuntu_12_04.Ubuntu_12_04_AuthFileModule;
import com.anrisoftware.sscontrol.httpd.apache.authldap.ubuntu_12_04.Ubuntu_12_04_AuthLdapModule;
import com.anrisoftware.sscontrol.httpd.fcgi.FcgiConfig;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModModule;
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerModule;
import com.anrisoftware.sscontrol.scripts.killprocess.KillProcessModule;
import com.anrisoftware.sscontrol.scripts.localchangegroup.LocalChangeGroupModule;
import com.anrisoftware.sscontrol.scripts.localchangeuser.LocalChangeUserModule;
import com.anrisoftware.sscontrol.scripts.localgroupadd.LocalGroupAddModule;
import com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddModule;
import com.anrisoftware.sscontrol.scripts.mklink.MkLinkModule;
import com.anrisoftware.sscontrol.scripts.pack.PackModule;
import com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoModule;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.anrisoftware.sscontrol.scripts.unpack.UnpackModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Apache Ubuntu 12.04</i> services module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.ExecCommandModule());
        install(new ChangeFileOwnerModule());
        install(new ChangeFileModModule());
        install(new LocalGroupAddModule());
        install(new LocalUserAddModule());
        install(new LocalChangeGroupModule());
        install(new LocalChangeUserModule());
        install(new MkLinkModule());
        install(new UnpackModule());
        install(new PackModule());
        install(new CheckFileHashModule());
        install(new ProcessInfoModule());
        install(new KillProcessModule());
        install(new Ubuntu_12_04_AuthFileModule());
        install(new Ubuntu_12_04_AuthLdapModule());
        bind(FcgiConfig.class).to(ApacheFcgiConfig.class);
        bindScripts();
        bindEmptyServiceConfig();
    }

    private void bindScripts() {
        MapBinder<String, Script> binder;
        binder = newMapBinder(binder(), String.class, Script.class);
        binder.addBinding(format("%s.%s", NAME, PROFILE))
                .to(UbuntuScript.class);
    }

    private void bindEmptyServiceConfig() {
        MapBinder<String, ServiceConfig> map = newMapBinder(binder(),
                String.class, ServiceConfig.class);
        map.addBinding(format("%s.%s", PROFILE, "0")).to(
                EmptyServiceConfig.class);
    }
}
