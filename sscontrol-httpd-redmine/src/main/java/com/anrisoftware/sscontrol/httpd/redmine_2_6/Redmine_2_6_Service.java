package com.anrisoftware.sscontrol.httpd.redmine_2_6;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.redmine.DefaultRedmineService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Redmine 2.6</i> service.
 *
 * @see <a href="http://www.redmine.org/">http://www.redmine.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Redmine_2_6_Service extends DefaultRedmineService {

    private static final String SERVICE_NAME = "redmine_2_6";

    /**
     * @see Redmine_2_6_ServiceFactory#create(Map, Domain)
     */
    @Inject
    Redmine_2_6_Service(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        super(webServiceFactory, args, domain, SERVICE_NAME);
    }

}
