/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.proxy_nginx_apache_ubuntu_12_04_piwikphpmyadmin

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Piwik</i> with <i>Nginx</i> proxy resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum PiwikProxyResources {

    profile("UbuntuProfile.groovy", PiwikProxyResources.class.getResource("UbuntuProfile.groovy")),
    phpConfDir("/etc/php5/cgi/conf.d", null),
    httpdProxyDomainsScript("Httpd.groovy", PiwikProxyResources.class.getResource("HttpdPiwikReverseProxyDomains.groovy")),
    httpdProxyScript("Httpd.groovy", PiwikProxyResources.class.getResource("HttpdPiwikReverseProxy.groovy")),
    // piwik
    // apache
    runcommandsLogExpected("/runcommands.log", PiwikProxyResources.class.getResource("runcommands_expected.txt")),
    apacheRestartCommand("/etc/init.d/apache2", PiwikProxyResources.class.getResource("echo_command.txt")),
    apacheStopCommand("/etc/init.d/apache2", PiwikProxyResources.class.getResource("echo_command.txt")),
    a2enmodCommand("/usr/sbin/a2enmod", PiwikProxyResources.class.getResource("echo_command.txt")),
    a2dismodCommand("/usr/sbin/a2dismod", PiwikProxyResources.class.getResource("echo_command.txt")),
    a2ensiteCommand("/usr/sbin/a2ensite", PiwikProxyResources.class.getResource("echo_command.txt")),
    a2dissiteCommand("/usr/sbin/a2dissite", PiwikProxyResources.class.getResource("echo_command.txt")),
    apache2Command("/usr/sbin/apache2", PiwikProxyResources.class.getResource("echo_command.txt")),
    apache2ctlCommand("/usr/sbin/apache2ctl", PiwikProxyResources.class.getResource("httpd_status_command.txt")),
    htpasswdCommand("/usr/bin/htpasswd", PiwikProxyResources.class.getResource("echo_command.txt")),
    htdigestCommand("/usr/bin/htdigest", PiwikProxyResources.class.getResource("echo_command.txt")),
    netstatCommand("/bin/netstat", PiwikProxyResources.class.getResource("echo_command.txt")),
    apacheConfDir("/etc/apache2", null),
    apacheSitesAvailableDir("/etc/apache2/sites-available", null),
    apacheSitesEnabledDir("/etc/apache2/sites-enabled", null),
    apacheConfigIncludeDir("/etc/apache2/conf.d", null),
    apacheDefaultConf("/etc/apache2/sites-available/default", PiwikProxyResources.class.getResource("apache_default.txt")),
    apacheDefaultSslConf("/etc/apache2/sites-available/default-ssl", PiwikProxyResources.class.getResource("apache_default_ssl.txt")),
    sitesDir("/var/www", null),
    // mysql
    mysqldumpCommand("/usr/bin/mysqldump", PiwikProxyResources.class.getResource("echo_command.txt")),
    // expected
    aptitudeOutExpected("/usr/bin/aptitude.out", PiwikProxyResources.class.getResource("aptitude_out_expected.txt")),
    apachePortsConfExpected("/etc/apache2/ports.conf", PiwikProxyResources.class.getResource("apache_ports_conf_expected.txt")),
    test1comProxyConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", PiwikProxyResources.class.getResource("test1comconf_proxy_expected.txt")),
    test1comSslProxyConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", PiwikProxyResources.class.getResource("test1comsslconf_proxy_expected.txt")),
    test1comDomainProxyConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", PiwikProxyResources.class.getResource("test1comconf_domain_expected.txt")),
    test1comSslDomainProxyConfExpected("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", PiwikProxyResources.class.getResource("test1comsslconf_domain_expected.txt")),
    chownProxyOutExpected("/bin/chown.out", PiwikProxyResources.class.getResource("chown_out_expected.txt")),
    chmodProxyOutExpected("/bin/chmod.out", PiwikProxyResources.class.getResource("chmod_out_expected.txt")),
    useraddProxyOutExpected("/usr/sbin/useradd.out", PiwikProxyResources.class.getResource("useradd_out_expected.txt")),
    groupaddProxyOutExpected("/usr/sbin/groupadd.out", PiwikProxyResources.class.getResource("groupadd_out_expected.txt")),
    // nginx
    nginxConfigurationDir("/etc/nginx", null),
    nginxSitesAvailableDir("/etc/nginx/sites-available", null),
    nginxSitesEnabledDir("/etc/nginx/sites-enabled", null),
    nginxConfigIncludeDir("/etc/nginx/conf.d", null),
    nginxRestartCommand("/etc/init.d/nginx", PiwikProxyResources.class.getResource("echo_command.txt")),

    static void copyPiwikProxyFiles(File parent) {
        // ubuntu
        netstatCommand.createFile parent
        // piwik
        phpConfDir.asFile parent mkdirs()
        // apache
        sitesDir.asFile parent mkdirs()
        apacheRestartCommand.createCommand parent
        apacheStopCommand.createCommand parent
        a2enmodCommand.createCommand parent
        a2dismodCommand.createCommand parent
        a2dissiteCommand.createCommand parent
        a2ensiteCommand.createCommand parent
        apache2Command.createCommand parent
        apache2ctlCommand.createCommand parent
        htpasswdCommand.createCommand parent
        // nginx
        apacheConfDir.asFile(parent).mkdirs()
        apacheDefaultConf.createFile parent
        apacheDefaultSslConf.createFile parent
        nginxRestartCommand.createCommand parent
    }

    static void setupPiwikProxyProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.service(["idapache2": "apache", "idproxy": "nginx"])
        // ubuntu
        entry.netstat_command netstatCommand.asFile(parent)
        // piwik
        // apache
        entry.apache_restart_command apacheRestartCommand.asFile(parent)
        entry.apache_configuration_directory apacheConfDir.asFile(parent)
        entry.apache_sites_available_directory apacheSitesAvailableDir.asFile(parent)
        entry.apache_sites_enabled_directory apacheSitesEnabledDir.asFile(parent)
        entry.apache_config_include_directory apacheConfigIncludeDir.asFile(parent)
        entry.enable_mod_command a2enmodCommand.asFile(parent)
        entry.disable_mod_command a2dismodCommand.asFile(parent)
        entry.enable_site_command a2ensiteCommand.asFile(parent)
        entry.disable_site_command a2dissiteCommand.asFile(parent)
        entry.apache_command apache2Command.asFile(parent)
        entry.apache_control_command apache2ctlCommand.asFile(parent)
        entry.htpasswd_command htpasswdCommand.asFile(parent)
        entry.sites_directory sitesDir.asFile(parent)
        entry.additional_mods "rpaf"
        // nginx
        entry.nginx_restart_command nginxRestartCommand.asFile(parent)
        entry.nginx_configuration_directory nginxConfigurationDir.asFile(parent)
        entry.nginx_sites_available_directory nginxSitesAvailableDir.asFile(parent)
        entry.nginx_sites_enabled_directory nginxSitesEnabledDir.asFile(parent)
        entry.nginx_config_include_directory nginxConfigIncludeDir.asFile(parent)
    }

    ResourcesUtils resources

    PiwikProxyResources(String path, URL resource) {
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
