/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.userkey.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.GroupFactory
import com.anrisoftware.sscontrol.remote.user.Require
import com.anrisoftware.sscontrol.remote.user.User
import com.anrisoftware.sscontrol.remote.user.UserFactory

/**
 * Local users SSH/keys script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UserKeyScript implements RemoteScript {

    @Inject
    private UserKeyScriptLogger log

    @Inject
    UserFactory userFactory

    @Inject
    GroupFactory groupFactory

    /**
     * The {@link Templates} for the script.
     */
    Templates scriptTemplates

    /**
     * Resource create SSH/keys.
     */
    TemplateResource createKeyTemplate

    LinuxScript script

    @Override
    void deployRemoteScript(RemoteService service) {
        createSshkeys()
    }

    /**
     * Create local user SSH/keys.
     */
    void createSshkeys() {
        RemoteService service = this.service
        service.users.each { User user ->
            def sshkeyfile = new File(String.format(sshkeyPattern, user.name))
            if (sshkeyfile.isFile()) {
                updateSshkey user, sshkeyfile
            } else {
                createSshkey user, sshkeyfile
            }
        }
    }

    /**
     * Updates the SSH/key for the user.
     *
     * @param user
     *            the {@link User}.
     *
     * @param keyfile
     *            the user key {@link File}.
     */
    void updateSshkey(User user, File keyfile) {
        if (!user.requires.contains(Require.passphrase)) {
            return
        }
        keyfile.delete()
        createSshkey user, keyfile
    }

    /**
     * Creates the SSH/key for the user.
     *
     * @param user
     *            the {@link User}.
     *
     * @param keyfile
     *            the user key {@link File}.
     */
    void createSshkey(User user, File keyfile) {
        if (!user.passphrase) {
            return
        }
        def args = ["command": keyGenCommand, "passphrase": user.passphrase, "keyFile": keyfile]
        def worker = scriptCommandFactory.create(createKeyTemplate,
                "createKey", "args", args)()
        log.sshkeyCreated script, user, worker
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script
        this.scriptTemplates = templatesFactory.create "UserKeyScript"
        this.createKeyTemplate = scriptTemplates.getResource "createkey"
    }

    @Override
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
