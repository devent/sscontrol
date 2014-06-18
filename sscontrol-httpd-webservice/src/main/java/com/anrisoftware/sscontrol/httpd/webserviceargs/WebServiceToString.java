package com.anrisoftware.sscontrol.httpd.webserviceargs;

import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.ALIAS;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.PREFIX;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.REF;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.REFDOMAIN;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Returns a string representation of a web service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WebServiceToString {

    private static final String NAME = "name";
    private static final String DOMAIN = "domain";

    /**
     * Returns a string representation of a web service.
     * 
     * @param service
     *            the {@link WebService}.
     * 
     * @return the {@link String} representation.
     */
    public String toString(WebService service) {
        ToStringBuilder builder;
        builder = new ToStringBuilder(this);
        builder.append(NAME, service.getName());
        builder.append(DOMAIN, service.getDomain());
        if (service.getAlias() != null) {
            builder.append(ALIAS, service.getAlias());
        }
        if (service.getPrefix() != null) {
            builder.append(PREFIX, service.getPrefix());
        }
        if (service.getRef() != null) {
            builder.append(REF, service.getRef());
        }
        if (service.getRefDomain() != null) {
            builder.append(REFDOMAIN, service.getRefDomain());
        }
        return builder.toString();
    }
}
