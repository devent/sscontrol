package com.anrisoftware.sscontrol.mail.certificate;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.mail.api.MailService;

/**
 * Parses the certificate arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CertificateArgs {

    private static final String CERT = "cert";

    private static final String KEY = "key";

    private static final String CA = "ca";

    @Inject
    private CertificateLogger log;

    URI cert(MailService service, Map<String, Object> args)
            throws ServiceException {
        Object file = args.get(CERT);
        log.checkCert(service, file);
        return toURI(service, file);
    }

    URI key(MailService service, Map<String, Object> args)
            throws ServiceException {
        Object file = args.get(KEY);
        log.checkKey(service, file);
        return toURI(service, file);
    }

    boolean haveCa(Map<String, Object> args) {
        return args.containsKey(CA);
    }

    URI ca(MailService service, Map<String, Object> args)
            throws ServiceException {
        Object file = args.get(CA);
        log.checkCa(service, file);
        return toURI(service, file);
    }

    private URI toURI(MailService service, Object cert)
            throws ServiceException {
        if (cert instanceof URI) {
            return fromURI(cert);
        } else if (cert instanceof URL) {
            return fromURL(service, cert);
        } else if (cert instanceof File) {
            return ((File) cert).toURI();
        }
        return new File(cert.toString()).toURI();
    }

    private URI fromURI(Object cert) {
        URI uri = (URI) cert;
        if (!uri.isAbsolute()) {
            return new File(uri).toURI();
        }
        return uri;
    }

    private URI fromURL(MailService service, Object cert)
            throws ServiceException {
        try {
            return ((URL) cert).toURI();
        } catch (URISyntaxException e) {
            throw log.syntaxError(service, e, cert);
        }
    }

}
