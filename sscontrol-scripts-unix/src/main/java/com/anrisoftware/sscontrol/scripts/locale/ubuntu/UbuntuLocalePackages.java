package com.anrisoftware.sscontrol.scripts.locale.ubuntu;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Ubuntu</i> packages needed for the specified locale.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UbuntuLocalePackages {

    private final static Pattern LOCALE_PATTERN = Pattern
            .compile("^([a-z]+)(?:_([a-z]+))?(?:-((?:utf.*)|(?:iso.*)))?$");

    private final String locale;

    @Inject
    private UbuntuLocalePackagesLogger log;

    /**
     * @see UbuntuLocalePackagesFactory#create(String)
     */
    @Inject
    UbuntuLocalePackages(@Assisted String locale) {
        this.locale = locale;
    }

    public List<String> getPackages() throws ScriptException {
        Matcher matcher = LOCALE_PATTERN.matcher(locale);
        log.localeMatches(this, matcher.matches());
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(locale).toString();
    }
}
