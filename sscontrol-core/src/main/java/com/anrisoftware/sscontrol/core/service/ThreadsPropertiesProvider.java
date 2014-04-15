package com.anrisoftware.sscontrol.core.service;

import java.net.URL;

import com.anrisoftware.globalpom.threads.properties.PropertiesThreads;
import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

@SuppressWarnings("serial")
public class ThreadsPropertiesProvider extends
        AbstractContextPropertiesProvider {

    private static final URL RES = ThreadsPropertiesProvider.class
            .getResource("/script_threads.properties");

    protected ThreadsPropertiesProvider() {
        super(PropertiesThreads.class, RES);
    }

}
