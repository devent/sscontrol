package com.anrisoftware.sscontrol.scripts.unix;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;

@Singleton
public class CommandTemplatesProvider implements Provider<Templates> {

    private static final String TEMPLATES_NAME = "ScriptsUnixTemplates";

    @Inject
    private TemplatesFactory templatesFactory;

    private Templates commandTemplates;

    @Override
    public Templates get() {
        lazyCreateTemplates();
        return commandTemplates;
    }

    private void lazyCreateTemplates() {
        if (commandTemplates == null) {
            this.commandTemplates = templatesFactory.create(TEMPLATES_NAME);
        }
    }

}
