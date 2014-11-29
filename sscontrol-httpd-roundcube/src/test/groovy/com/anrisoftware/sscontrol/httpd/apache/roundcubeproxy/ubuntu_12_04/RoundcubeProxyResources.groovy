/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcubeproxy.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04.Ubuntu_12_04_Resources
import com.anrisoftware.sscontrol.httpd.roundcube.resources.ResourcesUtils

/**
 * <i>Roundcube Nginx</i> proxy resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RoundcubeProxyResources {

    profile("UbuntuProfile.groovy", RoundcubeProxyResources.class.getResource("UbuntuProfile.groovy")),
    roundcubeArchive("/tmp/web-roundcubemail-1.0.3.tar.gz", RoundcubeProxyResources.class.getResource("roundcubemail-1.0.3.tar.gz")),
    // proxy
    proxyHttpdScript("Httpd.groovy", RoundcubeProxyResources.class.getResource("HttpdRoundcubeReverseProxy.groovy")),
    proxyDomainsHttpdScript("Httpd.groovy", RoundcubeProxyResources.class.getResource("HttpdRoundcubeReverseProxyDomains.groovy")),
    proxySampleConf("/var/www/www.test1.com/roundcube_1_0/config/config.inc.php.sample", RoundcubeProxyResources.class.getResource("config_inc_php_sample.txt")),
    proxyPortsConfExpected("/etc/apache2/ports.conf", RoundcubeProxyResources.class.getResource("proxy_ports_conf_expected.txt")),
    proxyDomainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", RoundcubeProxyResources.class.getResource("proxy_domains_conf.txt")),
    proxyConfigIncExpected("/var/www/www.test1.com/roundcube_1_0/config/config.inc.php", RoundcubeProxyResources.class.getResource("reverseproxy_config_inc_php_expected.txt")),
    proxyAptitudeOutExpected("/usr/bin/aptitude.out", RoundcubeProxyResources.class.getResource("reverseproxy_aptitude_out_expected.txt")),
    proxyA2enmodOutExpected("/usr/sbin/a2enmod.out", RoundcubeProxyResources.class.getResource("reverseproxy_a2enmod_out_expected.txt")),
    proxyChownOutExpected("/bin/chown.out", RoundcubeProxyResources.class.getResource("reverseproxy_chown_out_expected.txt")),
    proxyChmodOutExpected("/bin/chmod.out", RoundcubeProxyResources.class.getResource("reverseproxy_chmod_out_expected.txt")),
    proxyGroupaddOutExpected("/usr/sbin/groupadd.out", RoundcubeProxyResources.class.getResource("reverseproxy_groupadd_out.txt")),
    proxyUseraddOutExpected("/usr/sbin/useradd.out", RoundcubeProxyResources.class.getResource("reverseproxy_useradd_out_expected.txt")),
    proxyLogsDir("/var/www/www.test1.com/roundcube_1_0/logs", new URL("file://")),
    proxyTempDir("/var/www/www.test1.com/roundcube_1_0/temp", new URL("file://")),
    proxyTest1comProxyConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", RoundcubeProxyResources.class.getResource("reverseproxy_test1com_conf_expected.txt")),
    proxyTest1comSslProxyConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", RoundcubeProxyResources.class.getResource("reverseproxy_test1comssl_conf_expected.txt")),
    proxyWwwtest1comDomainProxyConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", RoundcubeProxyResources.class.getResource("reverseproxydomain_wwwtest1com_conf_expected.txt")),
    proxyWwwtest1comSslDomainProxyConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com-ssl.conf", RoundcubeProxyResources.class.getResource("reverseproxydomain_wwwtest1comssl_conf_expected.txt")),
    proxyWwwtest1comProxyConfExpected("/etc/nginx/sites-available/100-robobee-www.test1.com.conf", RoundcubeProxyResources.class.getResource("reverseproxy_wwwtest1com_conf_expected.txt")),
    proxywwwtest1comSslProxyConfExpected("/etc/nginx/sites-available/100-robobee-www.test1.com-ssl.conf", RoundcubeProxyResources.class.getResource("reverseproxy_wwwtest1comssl_conf_expected.txt")),
    // nginx
    nginxConfigurationDir("/etc/nginx", null),
    nginxSitesAvailableDir("/etc/nginx/sites-available", null),
    nginxSitesEnabledDir("/etc/nginx/sites-enabled", null),
    nginxConfigIncludeDir("/etc/nginx/conf.d", null),
    nginxRestartCommand("/etc/init.d/nginx", RoundcubeProxyResources.class.getResource("echo_command.txt")),
    nginxSigningKeyFile("/tmp/web-nginx_signing.key", RoundcubeProxyResources.class.getResource("nginx_signing_key.txt")),

    static void copyProxyFiles(File parent) {
        roundcubeArchive.createFile parent
        proxySampleConf.createFile parent
        nginxRestartCommand.createCommand parent
        nginxSigningKeyFile.createFile parent
    }

    static void setupProxyProperties(def profile, File parent) {
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

    RoundcubeProxyResources(String path, URL resource) {
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
