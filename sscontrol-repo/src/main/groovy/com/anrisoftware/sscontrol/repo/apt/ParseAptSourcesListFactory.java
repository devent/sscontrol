package com.anrisoftware.sscontrol.repo.apt;

import com.anrisoftware.sscontrol.repo.service.RepoService;

/**
 * Factory to create the <i>Apt</i> sources list parser.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface ParseAptSourcesListFactory {

    ParseAptSourcesList create(RepoService service, Object script);
}
