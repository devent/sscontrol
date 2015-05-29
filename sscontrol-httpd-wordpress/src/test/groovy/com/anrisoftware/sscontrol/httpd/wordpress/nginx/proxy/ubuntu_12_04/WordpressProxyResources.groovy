/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.wordpress.nginx.proxy.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04.Ubuntu_12_04_Resources
import com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04.WordpressResources
import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Wordpress with Nginx proxy resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum WordpressProxyResources {

    profile("UbuntuProfile.groovy", WordpressProxyResources.class.getResource("UbuntuProfile.groovy")),
    // base
    wordpressConfig("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    wordpressConfigExpected("/var/www/www.test1.com/wordpress_4/wp-config.php", WordpressProxyResources.class.getResource("wordpress_config_php_expected.txt")),
    // alias
    aliasDomainsScript("Httpd.groovy", WordpressProxyResources.class.getResource("AliasWordpressDomains.groovy")),
    aliasProxyScript("HttpdProxy.groovy", WordpressProxyResources.class.getResource("AliasWordpressProxy.groovy")),
    aliasAptitudeOutExpected("/usr/bin/aptitude.out", WordpressProxyResources.class.getResource("base_aptitude_out_expected.txt")),
    aliasPortsConfExpected("/etc/apache2/ports.conf", WordpressProxyResources.class.getResource("base_portsconf_expected.txt")),
    aliasTest1comConfProxyExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", WordpressProxyResources.class.getResource("alias_test1comconf_proxy_expected.txt")),
    aliasTest1comSslConfProxyExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", WordpressProxyResources.class.getResource("alias_test1comsslconf_proxy_expected.txt")),
    aliasWwwtest1comConfDomainExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressProxyResources.class.getResource("alias_wwwtest1comconf_domain_expected.txt")),
    aliasWwwtest1comSslConfDomainExpected("/etc/apache2/sites-available/100-robobee-www.test1.com-ssl.conf", WordpressProxyResources.class.getResource("alias_wwwtest1comsslconf_domain_expected.txt")),
    aliasWwwtest1comConfProxyExpected("/etc/nginx/sites-available/100-robobee-www.test1.com.conf", WordpressProxyResources.class.getResource("alias_wwwtest1comconf_proxy_expected.txt")),
    aliasWwwtest1comSslConfProxyExpected("/etc/nginx/sites-available/100-robobee-www.test1.com-ssl.conf", WordpressProxyResources.class.getResource("alias_wwwtest1comsslconf_proxy_expected.txt")),
    aliasChownOutExpected("/bin/chown.out", WordpressProxyResources.class.getResource("base_chown_out.txt")),
    aliasChmodOutExpected("/bin/chmod.out", WordpressProxyResources.class.getResource("base_chmod_out.txt")),
    aliasUseraddOutExpected("/usr/sbin/useradd.out", WordpressProxyResources.class.getResource("base_useradd_out.txt")),
    aliasGroupaddOutExpected("/usr/sbin/groupadd.out", WordpressProxyResources.class.getResource("base_groupadd_out.txt")),
    // nginx
    nginxConfigurationDir("/etc/nginx", null),
    nginxSitesAvailableDir("/etc/nginx/sites-available", null),
    nginxSitesEnabledDir("/etc/nginx/sites-enabled", null),
    nginxConfigIncludeDir("/etc/nginx/conf.d", null),
    nginxRestartCommand("/etc/init.d/nginx", WordpressProxyResources.class.getResource("echo_command.txt")),

    static void copyWordpressProxyFiles(File parent) {
        nginxRestartCommand.createCommand parent
    }

    static void setupWordpressProxyProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        def gs = ""
        entry.service(["idapache2${gs}": "apache", "idproxy": "nginx"])
        entry.additional_mods "rpaf"
        entry.apache_restart_command Ubuntu_12_04_Resources.apache2Command.asFile(parent)
        entry.nginx_restart_command nginxRestartCommand.asFile(parent)
        entry.apache_configuration_directory Ubuntu_12_04_Resources.confDir.asFile(parent)
        entry.nginx_configuration_directory nginxConfigurationDir.asFile(parent)
        entry.apache_sites_available_directory Ubuntu_12_04_Resources.sitesAvailableDir.asFile(parent)
        entry.nginx_sites_available_directory nginxSitesAvailableDir.asFile(parent)
        entry.apache_sites_enabled_directory Ubuntu_12_04_Resources.sitesEnabledDir.asFile(parent)
        entry.nginx_sites_enabled_directory nginxSitesEnabledDir.asFile(parent)
        entry.apache_config_include_directory Ubuntu_12_04_Resources.configIncludeDir.asFile(parent)
        entry.nginx_config_include_directory nginxConfigIncludeDir.asFile(parent)
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
