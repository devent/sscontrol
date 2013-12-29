package com.anrisoftware.sscontrol.remote.api;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.remote.service.RemoteService;

/**
 * Remote script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RemoteScript {

    /**
     * Sets the parent script with the properties.
     * 
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(LinuxScript script);

    /**
     * Returns the parent script with the properties.
     * 
     * @return the {@link LinuxScript}.
     */
    LinuxScript getScript();

    /**
     * Deploys the remote script.
     * 
     * @param service
     *            the {@link RemoteScript}.
     */
    void deployRemoteScript(RemoteService service);
}
