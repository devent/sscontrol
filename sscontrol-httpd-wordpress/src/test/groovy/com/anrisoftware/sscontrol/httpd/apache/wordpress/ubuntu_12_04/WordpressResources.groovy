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
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 Wordpress</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum WordpressResources {

    profile("UbuntuProfile.groovy", WordpressResources.class.getResource("UbuntuProfile.groovy")),
    wordpressArchive("/tmp/web-wordpress-4.1.1.tar.gz", WordpressResources.class.getResource("wordpress-4.1.tar.gz")),
    wordpressArchive_de_DE("/tmp/web-wordpress-4.1.1-de_DE.tar.gz", WordpressResources.class.getResource("wordpress-4.1.1-de_DE.tar.gz")),
    phpConfDir("/etc/php5/cgi/conf.d", null),
    portsConf("/etc/apache2/ports.conf", WordpressResources.class.getResource("ports_conf.txt")),
    // basic
    httpdScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpress.groovy")),
    httpdBackupScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressBackup.groovy")),
    httpdDebugScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressDebug.groovy")),
    basicPortsConfExpected("/etc/apache2/ports.conf", WordpressResources.class.getResource("basic_ports_conf_expected.txt")),
    basicDomainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", WordpressResources.class.getResource("basic_domains_conf.txt")),
    basicWwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressResources.class.getResource("basic_wwwtest1com_conf_expected.txt")),
    basicWwwtest1comFcgiScriptExpected("/var/www/php-fcgi-scripts/www.test1.com/php-fcgi-starter", WordpressResources.class.getResource("basic_php_fcgi_starter_expected.txt")),
    basicWwwtest1comPhpiniExpected("/var/www/php-fcgi-scripts/www.test1.com/domain_php.ini", WordpressResources.class.getResource("basic_php_ini_expected.txt")),
    basicWordpressConfigSample("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    basicWordpressConfigExpected("/var/www/www.test1.com/wordpress_4/wp-config.php", WordpressResources.class.getResource("basic_wordpress_config_expected.txt")),
    basicAptitudeOutExpected("/usr/bin/aptitude.out", WordpressResources.class.getResource("basic_aptitude_out_expected.txt")),
    basicE2enmodOutExpected("/usr/sbin/a2enmod.out", WordpressResources.class.getResource("basic_a2enmod_out_expected.txt")),
    basicChownOutExpected("/bin/chown.out", WordpressResources.class.getResource("basic_chown_out_expected.txt")),
    basicChmodOutExpected("/bin/chmod.out", WordpressResources.class.getResource("basic_chmod_out_expected.txt")),
    basicGroupaddOutExpected("/usr/sbin/groupadd.out", WordpressResources.class.getResource("basic_groupadd_out_expected.txt")),
    basicUseraddOutExpected("/usr/sbin/useradd.out", WordpressResources.class.getResource("basic_useradd_out_expected.txt")),
    basicTarOutExpected("/bin/tar.out", WordpressResources.class.getResource("basic_tar_out_expected.txt")),
    basicWwwtest1comWordpressCacheDir("/var/www/www.test1.com/wordpress_4/wp-content/cache", null),
    basicWwwtest1comWordpressPluginsDir("/var/www/www.test1.com/wordpress_4/wp-content/plugins", null),
    basicWwwtest1comWordpressThemesDir("/var/www/www.test1.com/wordpress_4/wp-content/themes", null),
    basicWwwtest1comWordpressUploadsDir("/var/www/www.test1.com/wordpress_4/wp-content/uploads", null),
    // de_DE
    basicdeWordpressConfigSample("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    basicdeWordpressConfigExpected("/var/www/www.test1.com/wordpress_4/wp-config.php", WordpressResources.class.getResource("basicde_wordpress_config_expected.txt")),
    basicdeTarOutExpected("/bin/tar.out", WordpressResources.class.getResource("basicde_tar_out_expected.txt")),
    // debug
    debugWordpressConfigSample("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    debugWordpressConfigExpected("/var/www/www.test1.com/wordpress_4/wp-config.php", WordpressResources.class.getResource("debug_wordpress_config_expected.txt")),
    // per reference
    httpdRefScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressRef.groovy")),
    refPortsConfExpected("/etc/apache2/ports.conf", WordpressResources.class.getResource("basic_ports_conf_expected.txt")),
    refDomainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", WordpressResources.class.getResource("ref_domains_conf.txt")),
    refWwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressResources.class.getResource("ref_wwwtest1com_conf_expected.txt")),
    refWwwtest1comSslConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com-ssl.conf", WordpressResources.class.getResource("ref_wwwtest1com_ssl_conf_expected.txt")),
    refWwwtest1comFcgiScriptExpected("/var/www/php-fcgi-scripts/www.test1.com/php-fcgi-starter", WordpressResources.class.getResource("basic_php_fcgi_starter_expected.txt")),
    refWordpressConfigSample("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    refWordpressConfigExpected("/var/www/www.test1.com/wordpress_4/wp-config.php", WordpressResources.class.getResource("basic_wordpress_config_expected.txt")),
    refChownOutExpected("/bin/chown.out", WordpressResources.class.getResource("ref_chown_out_expected.txt")),
    refChmodRetExpected("/bin/chmod.out", WordpressResources.class.getResource("ref_chmod_out_expected.txt")),
    // Wordpress Root Alias
    httpdRootScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressRoot.groovy")),
    rootWordpressConfigSample("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    rootWwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressResources.class.getResource("root_wwwtest1com_conf_expected.txt")),
    // Wordpress Multi-Site Sub-Domain
    httpdMsSubdomainScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressMsSubdomain.groovy")),
    subdomainWwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressResources.class.getResource("subdomain_wwwtest1com_conf_expected.txt")),
    subdomainWwwblogfoocomConfExpected("/etc/apache2/sites-available/100-robobee-www.blogfoo.com.conf", WordpressResources.class.getResource("subdomain_wwwblogfoocom_conf_expected.txt")),
    subdomainWwwblogbarcomConfExpected("/etc/apache2/sites-available/100-robobee-www.blogbar.com.conf", WordpressResources.class.getResource("subdomain_wwwblogbarcom_conf_expected.txt")),
    subdomainChownOutExpected("/bin/chown.out", WordpressResources.class.getResource("subdomain_chown_out_expected.txt")),
    subdomainChmodOutExpected("/bin/chmod.out", WordpressResources.class.getResource("subdomain_chmod_out_expected.txt")),
    subdomainGroupaddOutExpected("/usr/sbin/groupadd.out", WordpressResources.class.getResource("subdomain_groupadd_out_expected.txt")),
    subdomainUseraddOutExpected("/usr/sbin/useradd.out", WordpressResources.class.getResource("subdomain_useradd_out_expected.txt")),
    subdomainWordpressConfigSample("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    subdomainWordpressConfigExpected("/var/www/www.test1.com/wordpress_4/wp-config.php", WordpressResources.class.getResource("subdomain_wordpress_config_php_expected.txt")),
    // Wordpress Themes, Plugins
    httpdPluginsScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressPlugins.groovy")),
    brokenLinkCheckerArchive("/tmp/web-broken-link-checker.zip", WordpressResources.class.getResource("broken-link-checker.zip")),
    hyphenatorArchive("/tmp/web-hyphenator.zip", WordpressResources.class.getResource("hyphenator.zip")),
    picochicArchive("/tmp/web-picochic.zip", WordpressResources.class.getResource("picochic.zip")),
    tagebuchArchive("/tmp/web-tagebuch.zip", WordpressResources.class.getResource("tagebuch.zip")),
    plguinsWordpressConfigSample("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    pluginsUnzipOutExpected("/usr/bin/unzip.out", WordpressResources.class.getResource("plugins_unzip_out_expected.txt")),
    // no override
    httpdScriptNoOverride("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressNoOverride.groovy")),
    nooverrideWwwTest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressResources.class.getResource("nooverride_wwwtest1com_expected.txt")),
    nooverrideWwwTest1comSslConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com-ssl.conf", WordpressResources.class.getResource("nooverride_wwwtest1com_ssl_expected.txt")),
    nooverrideWordpressConfig("/var/www/www.test1.com/myprefix/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    nooverrideWordpressConfigExpected("/var/www/www.test1.com/myprefix/wp-config.php", WordpressResources.class.getResource("basic_wordpress_config_expected.txt")),
    nooverrideChownOutExpected("/bin/chown.out", WordpressResources.class.getResource("nooverride_chown_out_expected.txt")),
    nooverrideChmodOutExpected("/bin/chmod.out", WordpressResources.class.getResource("nooverride_chmod_out_expected.txt")),
    nooverrideTarOutExpected("/bin/tar.out", WordpressResources.class.getResource("nooverride_tar_out_expected.txt")),
    // backup
    test1WordpressDir("/var/www/www.test1.com/wordpress_4", null),
    backupTarOutExpected("/bin/tar.out", WordpressResources.class.getResource("backup_tar_out_expected.txt")),
    mysqldumpOutExpected("/usr/bin/mysqldump.out", WordpressResources.class.getResource("backup_mysqldump_out_expected.txt")),
    backupTarget("/var/backups", null),
    // hypercache
    hypercacheHttpdScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressHypercache.groovy")),
    hypercacheArchive("/tmp/hyper-cache.zip", WordpressResources.class.getResource("hyper-cache.zip")),
    hypercacheWwwTest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressResources.class.getResource("hypercache_wwwtest1com_expected.txt")),
    hypercacheWordpressConfig("/var/www/www.test1.com/wordpress_4/wp-config-sample.php", WordpressResources.class.getResource("wordpress_config_sample_php.txt")),
    hypercacheWordpressConfigExpected("/var/www/www.test1.com/wordpress_4/wp-config.php", WordpressResources.class.getResource("hypercache_wordpress_config_expected.txt")),
    hypercacheChownOutExpected("/bin/chown.out", WordpressResources.class.getResource("hypercache_chown_out_expected.txt")),
    hypercacheChmodOutExpected("/bin/chmod.out", WordpressResources.class.getResource("hypercache_chmod_out_expected.txt")),
    hypercacheTarOutExpected("/bin/tar.out", WordpressResources.class.getResource("hypercache_tar_out_expected.txt")),
    hypercacheUnzipOutExpected("/usr/bin/unzip.out", WordpressResources.class.getResource("hypercache_unzip_out_expected.txt")),

    static copyWordpressFiles(File parent) {
        phpConfDir.asFile parent mkdirs()
        portsConf.createFile parent
        wordpressArchive.createFile parent
        wordpressArchive_de_DE.createFile parent
        brokenLinkCheckerArchive.createFile parent
        hyphenatorArchive.createFile parent
        picochicArchive.createFile parent
        tagebuchArchive.createFile parent
    }

    static void setupWordpressProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        // commands
        // files
        entry.wordpress_archive wordpressArchive.asFile(parent)
        entry.wordpress_archive_hash "md5:9c19129546f70102cad2136276c44042"
        entry.wordpress_archive_de_DE wordpressArchive_de_DE.asFile(parent)
        entry.wordpress_archive_hash_de_DE "md5:731f86c084dc5c1ac83ca90cd73fe7aa"
        // directories
        entry.php_fcgi_php_conf_directory phpConfDir.asFile(parent)
        // others
        entry.wordpress_auth_key "auth-key"
        entry.wordpress_secure_auth_key "secure-auth-key"
        entry.wordpress_logged_in_key "logged-in-key"
        entry.wordpress_nonce_key "nonce-key"
        entry.wordpress_auth_salt "auth-salt"
        entry.wordpress_secure_auth_salt "secure-auth-salt"
        entry.wordpress_logged_in_salt "logged-in-salt"
        entry.wordpress_nonce_salt "nonce-salt"
        // plugins, themes
        entry.wordpress_broken_link_checker brokenLinkCheckerArchive.resource
        entry.wordpress_hyphenator hyphenatorArchive.resource
        entry.wordpress_hyper_cache hypercacheArchive.resource
        entry.wordpress_picochic picochicArchive.resource
        entry.wordpress_tagebuch tagebuchArchive.resource
    }

    ResourcesUtils resources

    WordpressResources(String path, URL resource) {
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
