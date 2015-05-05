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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuInstallLocale;
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Ubuntu 12.04</i> locale installer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Ubuntu_12_04_InstallLocale extends UbuntuInstallLocale {

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    @Inject
    private InstallPackagesFactory installPackagesFactory;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private TemplatesFactory templatesFactory;

    /**
     * @see Ubuntu_12_04_InstallLocaleFactory#create(Map, Object, Threads)
     */
    @Inject
    Ubuntu_12_04_InstallLocale(Ubuntu_12_04_InstallLocaleLogger log,
            @Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        super(args);
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        log.installCommand(args, parent);
        log.system(args, parent);
    }

    @Override
    public Ubuntu_12_04_InstallLocale call() throws Exception {
        Templates templates = templatesFactory.create("ScriptsUnixTemplates");
        TemplateResource templateResource = templates.getResource("mkln");
        scriptExecFactory.create(args, parent, threads, templateResource,
                "unix").call();
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
