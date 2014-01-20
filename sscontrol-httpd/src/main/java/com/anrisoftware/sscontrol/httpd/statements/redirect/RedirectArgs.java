package com.anrisoftware.sscontrol.httpd.statements.redirect;

import static com.anrisoftware.sscontrol.httpd.statements.redirect.DomainPlaceholder.DOMAIN_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Parses redirect arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RedirectArgs {

    @Inject
    private RedirectArgsLogger log;

    String to(Domain domain, Map<String, Object> args) {
        Object to = args.get("to");
        log.checkTo(domain, to);
        return replace(to.toString(), DOMAIN_PLACEHOLDER, domain.getName());
    }

}
