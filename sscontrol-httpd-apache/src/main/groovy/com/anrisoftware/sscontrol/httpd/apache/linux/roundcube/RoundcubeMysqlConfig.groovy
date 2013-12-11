package com.anrisoftware.sscontrol.httpd.apache.linux.roundcube;

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeService

/**
 * Roundcube MySQL database back-end.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RoundcubeMysqlConfig implements RoundcubeDatabaseConfig {

    public static final String NAME = "mysql";

    private ApacheScript script;

    Templates roundcubeTemplates

    TemplateResource roundcubeConfigTemplate

    @Override
    void setupDatabase(RoundcubeService service) {
        def database
        // TODO Auto-generated method stub

    }

    @Override
    public String getDatabase() {
        return NAME;
    }

    @Override
    void setScript(ApacheScript script) {
        this.script = script;
        roundcubeTemplates = templatesFactory.create "Roundcube_0_9"
        roundcubeConfigTemplate = roundcubeTemplates.getResource "config"
    }

    @Override
    ApacheScript getScript() {
        return script;
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
