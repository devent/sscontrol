package com.anrisoftware.sscontrol.httpd.nginx.nginxconfig

import org.junit.BeforeClass
import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see NginxConfigList
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class NginxConfigListTest {

    @Test
    void "accumulate same locations"() {
        def list = factory.create()
        list << """proxy_redirect off;

location /sitefoo/ {
    proxy_pass http://127.0.0.1:8080;
}
"""
        list << """
location /sitefoo/ {
    dav_methods PUT DELETE MKCOL COPY MOVE;
    dav_access group:rw all:r;
}
"""
        list << """
location /sitefoo/ {
    limit_except GET {
        allow 192.168.1.0/32;
        deny  all;
    }
}
"""
        list << """
location /sitebar/ {
    proxy_pass http://127.0.0.1:8080;
}
"""
        assert list.size() == 2
    }

    static Injector injector

    static NginxConfigListFactory factory

    @BeforeClass
    static void createFactory() {
        this.injector = Guice.createInjector new NginxConfigListModule()
        this.factory = injector.getInstance NginxConfigListFactory
    }
}
