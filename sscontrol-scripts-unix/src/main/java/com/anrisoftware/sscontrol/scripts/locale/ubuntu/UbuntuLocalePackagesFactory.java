package com.anrisoftware.sscontrol.scripts.locale.ubuntu;

/**
 * <i>Ubuntu</i> packages factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface UbuntuLocalePackagesFactory {

    /**
     * Creates the <i>Ubuntu</i> packages.
     *
     * @param locale
     *            the <i>String</i> locale name, for example
     *            {@code "de_DE-ISO-8859-1"}.
     *
     * @return the {@link UbuntuLocalePackages}.
     */
    UbuntuLocalePackages create(String locale);
}
