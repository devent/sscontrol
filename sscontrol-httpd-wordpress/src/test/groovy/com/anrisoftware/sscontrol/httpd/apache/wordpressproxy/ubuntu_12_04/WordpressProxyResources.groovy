/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpressproxy.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04.Ubuntu_12_04_Resources
import com.anrisoftware.sscontrol.httpd.wordpress.resources.ResourcesUtils

/**
 * Wordpress with Nginx proxy resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum WordpressProxyResources {

    profile("UbuntuProfile.groovy", WordpressProxyResources.class.getResource("UbuntuProfile.groovy")),
    phpConfDir("/etc/php5/cgi/conf.d", null),
    wordpressArchive("/tmp/web-wordpress-3.8.tar.gz", WordpressProxyResources.class.getResource("wordpress-3.8.tar.gz")),
    wordpressArchiveHash("/tmp/web-wordpress-3.8.tar.gz.sha1", WordpressProxyResources.class.getResource("wordpress-3.8.tar.gz.sha1")),
    wordpress_3_8_config("/var/www/www.test1.com/wordpress_3_8/wp-config-sample.php", WordpressProxyResources.class.getResource("wordpress_3_8_config_sample_php.txt")),
    wordpress_3_8_config_expected("/var/www/www.test1.com/wordpress_3_8/wp-config.php", WordpressProxyResources.class.getResource("wordpress_3_8_config_php_expected.txt")),
    aptitudeExpectedConf("/usr/bin/aptitude.out", WordpressProxyResources.class.getResource("aptitude_out_expected.txt")),
    httpdProxyDomainsScript("Httpd.groovy", WordpressProxyResources.class.getResource("HttpdWordpressReverseProxyDomains.groovy")),
    httpdProxyScript("Httpd.groovy", WordpressProxyResources.class.getResource("HttpdWordpressReverseProxy.groovy")),
    portsProxyExpectedConf("/etc/apache2/ports.conf", WordpressProxyResources.class.getResource("ports_reverseproxy_expected_conf.txt")),
    test1comProxyConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", WordpressProxyResources.class.getResource("test1_com_reverseproxy_conf.txt")),
    test1comSslProxyConf("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", WordpressProxyResources.class.getResource("test1_com_ssl_reverseproxy_conf.txt")),
    wwwtest1comDomainProxyConf("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressProxyResources.class.getResource("www_test1_com_reverseproxydomain_conf.txt")),
    wwwtest1comSslDomainProxyConf("/etc/apache2/sites-available/100-robobee-www.test1.com-ssl.conf", WordpressProxyResources.class.getResource("www_test1_com_ssl_reverseproxydomain_conf.txt")),
    wwwtest1comProxyConf("/etc/nginx/sites-available/100-robobee-www.test1.com.conf", WordpressProxyResources.class.getResource("www_test1_com_reverseproxy_conf.txt")),
    wwwtest1comSslProxyConf("/etc/nginx/sites-available/100-robobee-www.test1.com-ssl.conf", WordpressProxyResources.class.getResource("www_test1_com_ssl_reverseproxy_conf.txt")),
    chownProxyOut("/bin/chown.out", WordpressProxyResources.class.getResource("chown_reverseproxy_out.txt")),
    chmodProxyOut("/bin/chmod.out", WordpressProxyResources.class.getResource("chmod_reverseproxy_out.txt")),
    useraddProxyOut("/usr/sbin/useradd.out", WordpressProxyResources.class.getResource("useradd_reverseproxy_out.txt")),
    groupaddProxyOut("/usr/sbin/groupadd.out", WordpressProxyResources.class.getResource("groupadd_reverseproxy_out.txt")),
    // nginx
    nginxConfigurationDir("/etc/nginx", null),
    nginxSitesAvailableDir("/etc/nginx/sites-available", null),
    nginxSitesEnabledDir("/etc/nginx/sites-enabled", null),
    nginxConfigIncludeDir("/etc/nginx/conf.d", null),
    nginxRestartCommand("/etc/init.d/nginx", WordpressProxyResources.class.getResource("echo_command.txt")),
    nginxSigningKeyFile("/tmp/web-nginx_signing.key", WordpressProxyResources.class.getResource("nginx_signing_key.txt")),

    static void copyWordpressProxyFiles(File parent) {
        phpConfDir.asFile parent mkdirs()
        wordpressArchive.createFile parent
        wordpress_3_8_config.createFile parent
        nginxRestartCommand.createCommand parent
        nginxSigningKeyFile.createFile parent
    }

    static void setupWordpressProxyProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.service(["idapache2": "apache", "idproxy": "nginx"])
        entry.additional_mods "rpaf"
        entry.apache_restart_command "${Ubuntu_12_04_Resources.restartCommand.asFile(parent)} restart"
        entry.nginx_restart_command "${nginxRestartCommand.asFile(parent)} restart"
        entry.apache_configuration_directory Ubuntu_12_04_Resources.confDir.asFile(parent)
        entry.nginx_configuration_directory nginxConfigurationDir.asFile(parent)
        entry.apache_sites_available_directory Ubuntu_12_04_Resources.sitesAvailableDir.asFile(parent)
        entry.nginx_sites_available_directory nginxSitesAvailableDir.asFile(parent)
        entry.apache_sites_enabled_directory Ubuntu_12_04_Resources.sitesEnabledDir.asFile(parent)
        entry.nginx_sites_enabled_directory nginxSitesEnabledDir.asFile(parent)
        entry.apache_config_include_directory Ubuntu_12_04_Resources.configIncludeDir.asFile(parent)
        entry.nginx_config_include_directory nginxConfigIncludeDir.asFile(parent)
        entry.nginx_signing_key nginxSigningKeyFile.asFile(parent)
    }

    ResourcesUtils resources

    WordpressProxyResources(String path, URL resource) {
        this.resources = new ResourcesUtils(path: path, resource: resource)
    }

    String getPath() {
        resources.path
    }

    URL getResource() {
        resources.resource
    }

    File asFile(File parent) {
        resources.asFile parent
    }

    void createFile(File parent) {
        resources.createFile parent
    }

    void createCommand(File parent) {
        resources.createCommand parent
    }

    String replaced(File parent, def search, def replace) {
        resources.replaced parent, search, replace
    }

    String toString() {
        resources.toString()
    }
}
