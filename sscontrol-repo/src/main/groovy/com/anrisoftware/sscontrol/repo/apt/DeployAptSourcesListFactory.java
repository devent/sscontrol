package com.anrisoftware.sscontrol.repo.apt;

import java.util.Map;

import com.anrisoftware.sscontrol.repo.service.RepoService;

/**
 * Factory to create the <i>Apt</i> sources list deployer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface DeployAptSourcesListFactory {

    DeployAptSourcesList create(RepoService service,
            @SuppressWarnings("rawtypes") Map sources, Object script);
}
