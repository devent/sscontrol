package com.anrisoftware.sscontrol.httpd.gitit.core

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04.GititConfigFactory

/**
 * Configures <i>Gitit 0.10.</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Gitit_0_10_Config {

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript script

    /**
     * Returns the list of needed packages for <i>Gitit</i>.
     *
     * <ul>
     * <li>profile property {@code "gitit_packages"}</li>
     * </ul>
     *
     * @see #getGititProperties()
     */
    List getGititPackages() {
        profileListProperty "gitit_packages", gititProperties
    }

    /**
     * Returns the default <i>Gitit</i> properties.
     *
     * @return the <i>Gitit</i> {@link ContextProperties} properties.
     */
    abstract ContextProperties getGititProperties()

    /**
     * Returns the <i>Gitit</i> service name.
     */
    String getServiceName() {
        GititConfigFactory.SERVICE_NAME
    }

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * Delegates missing properties to {@link LinuxScript}.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to {@link LinuxScript}.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
