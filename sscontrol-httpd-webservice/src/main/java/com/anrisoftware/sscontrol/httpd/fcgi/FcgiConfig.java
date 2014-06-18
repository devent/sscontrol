package com.anrisoftware.sscontrol.httpd.fcgi;

import java.util.List;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Configures the <i>php-fcgi</i>.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface FcgiConfig {

    /**
     * Sets the parent script.
     * 
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(LinuxScript script);

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List<String> config);

    /**
     * Enables the <i>php-fcgi</i>.
     */
    void enableFcgi();

    /**
     * Setups the domain for <i>php-fcgi</i>.
     * 
     * @param domain
     *            the {@link Domain}.
     */
    void deployConfig(Domain domain);

}
