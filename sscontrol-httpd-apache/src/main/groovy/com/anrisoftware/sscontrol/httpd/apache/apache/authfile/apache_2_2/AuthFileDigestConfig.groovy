package com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2

import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.apache.commons.lang3.StringUtils.split

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.auth.AuthService
import com.anrisoftware.sscontrol.httpd.auth.RequireUpdate
import com.anrisoftware.sscontrol.httpd.auth.RequireUser
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker

/**
 * Auth/file-digest configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileDigestConfig {

    @Inject
    AuthFileDigestConfigLogger log

    /**
     * Auth/file templates.
     */
    Templates authTemplates

    /**
     * File/auth configuration.
     */
    TemplateResource authConfigTemplate

    /**
     * File/auth commands.
     */
    TemplateResource authCommandsTemplate

    /**
     * @see ServiceConfig#getScript()
     */
    Object script

    /**
     * Creates the required users for auth-digest.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     *
     * @param users
     *            the {@link List} of {@link RequireUser} users.
     *
     * @return the {@link List} of the user configuration.
     */
    List createUsers(Domain domain, AuthService service, List users) {
        def file = passwordFile(domain, service)
        def oldusers = file.exists() ? FileUtils.readLines(file, charset) : []
        users.each { RequireUser user ->
            def found = findUser(oldusers, user)
            found.found ? updateUser(domain, service, found, oldusers) : insertUser(domain, service, user, oldusers)
        }
        return oldusers
    }

    Map findUser(List users, RequireUser user) {
        def found = null
        def index = -1
        for (int i = 0; i < users.size(); i++) {
            String str = users[i]
            String[] s = split(str, ":")
            if (user.name == s[0]) {
                found = user
                index = i
                break
            }
        }
        return [found: found, index: index]
    }

    void updateUser(Domain domain, AuthService service, Map found, List users) {
        RequireUser userfound = found.found
        int index = found.index
        switch (userfound.updateMode) {
            case RequireUpdate.password:
                users[index] = updatePassword domain, service, userfound
                break
            default:
                break
        }
    }

    void insertUser(Domain domain, AuthService service, RequireUser user, List users) {
        def worker = digestPassword auth: service, user: user
        def out = replaceChars worker.out, '\n', ''
        users << out
    }

    String updatePassword(Domain domain, AuthService service, RequireUser user) {
        def worker = digestPassword auth: service, user: user
        replaceChars worker.out, '\n', ''
    }

    /**
     * Executes the command to create the password for the user.
     *
     * @param args
     *            the command {@link Map} arguments.
     *
     * @return the {@link ScriptCommandWorker}.
     */
    ScriptCommandWorker digestPassword(Map args) {
        log.checkDigestPasswordArgs this, args
        scriptCommandFactory.create(authCommandsTemplate, "digestPassword", "args", args)()
    }

    /**
     * Returns the users authentication file for the domain.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     */
    File passwordFile(Domain domain, AuthService service) {
        def location = FilenameUtils.getBaseName(service.location)
        def dir = new File(authSubdirectory, domainDir(domain))
        new File("${location}-digest.passwd", dir)
    }

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
        this.authTemplates = templatesFactory.create "Apache_2_2_AuthFile"
        authCommandsTemplate = authTemplates.getResource "commands"
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
