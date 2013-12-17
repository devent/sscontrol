/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-profile.
 *
 * sscontrol-profile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-profile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-profile. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.profile.service;

import static java.lang.String.format;
import static org.apache.commons.collections.map.PredicatedMap.decorate;
import groovy.lang.GString;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.set.UnmodifiableSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.format.duration.DurationFormatFactory;
import com.anrisoftware.globalpom.format.locale.LocaleFormatFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.core.api.ProfileProperties;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * @see ProfileProperties
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class ProfilePropertiesImpl implements ProfileProperties {

    @Inject
    private ProfilePropertiesImplLogger log;

    @Inject
    private DurationFormatFactory durationFormatFactory;

    @Inject
    private LocaleFormatFactory localeFormatFactory;

    private final Map<String, Object> properties;

    @SuppressWarnings("unchecked")
    ProfilePropertiesImpl() {
        this.properties = decorate(new HashMap<String, Object>(),
                NotNullPredicate.INSTANCE, NotNullPredicate.INSTANCE);
    }

    /**
     * Returns the profile property with the specified name.
     * 
     * @param name
     *            the {@link String} name.
     * 
     * @return the property value.
     */
    public Object propertyMissing(String name) {
        return properties.get(name);
    }

    /**
     * Adds the profile property with the specified name and value.
     * 
     * @param name
     *            the profile {@link String} name.
     * 
     * @param args
     *            the profile value.
     */
    public Object methodMissing(String name, Object args) {
        @SuppressWarnings("unchecked")
        List<Object> argsList = InvokerHelper.asList(args);
        if (argsList.size() == 0) {
            putProperty(name, true);
        } else if (argsList.size() == 1 && argsList.get(0) instanceof GString) {
            putProperty(name, argsList.get(0).toString());
        } else if (argsList.size() == 1) {
            putProperty(name, argsList.get(0));
        } else {
            putProperty(name, argsList);
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    @Override
    public Object get(String key) {
        return properties.get(key);
    }

    @Override
    public Object get(String key, Object... args) {
        String value = (String) get(key);
        return value == null ? null : format(value, args);
    }

    @Override
    public List<String> getList(String key) {
        if (!containsKey(key)) {
            return null;
        }
        String value = get(key).toString();
        return Arrays.asList(StringUtils.split(value, ","));
    }

    @Override
    public void put(String key, Object property) {
        putProperty(key, property);
    }

    private void putProperty(String name, Object value) {
        properties.put(name, value);
        log.propertyAdded(this, name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getKeys() {
        return UnmodifiableSet.decorate(properties.keySet());
    }

    /**
     * Returns a profile property.
     * 
     * @param key
     *            the property {@link String} key.
     * 
     * @return the value of the profile property.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     */
    public Object profileProperty(String key) throws ServiceException {
        Object property = get(key);
        if (property != null) {
            return property;
        }
        throw log.noProfileProperty(this, key);
    }

    /**
     * Returns a profile property. If the profile property was not set return
     * the default value from the default properties.
     * 
     * @param key
     *            the property {@link String} key.
     * 
     * @param defaults
     *            default {@link ContextProperties} properties.
     * 
     * @return the value of the profile property.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     */
    public Object profileProperty(String key, ContextProperties defaults)
            throws ServiceException {
        Object property = get(key);
        if (property != null) {
            return property;
        }
        property = defaults.getProperty(key);
        if (property != null) {
            return property;
        }
        throw log.noProfileProperty(this, key);
    }

    /**
     * Returns a duration profile property. If the profile property was not set
     * return the default value from the default properties.
     * 
     * @param key
     *            the property {@link String} key.
     * 
     * @param defaults
     *            default {@link ContextProperties} properties.
     * 
     * @return the {@link Duration} value of the profile property.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     * 
     * @throws ParseException
     *             if the profile property could not be parsed to a duration.
     */
    public Duration profileDurationProperty(String key,
            ContextProperties defaults) throws ServiceException, ParseException {
        Object property = get(key);
        if (property != null) {
            return durationFormatFactory.create().parse(property.toString());
        }
        property = defaults.getProperty(key);
        if (property != null) {
            return durationFormatFactory.create().parse(property.toString());
        }
        throw log.noProfileProperty(this, key);
    }

    /**
     * Returns a profile number property. If the profile property was not set
     * return the default value from the default properties.
     * 
     * @param key
     *            the property {@link String} key.
     * 
     * @param defaults
     *            default {@link ContextProperties} properties.
     * 
     * @return the {@link Number} value of the profile property.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     * 
     * @throws ClassCastException
     *             if the profile property is not a number.
     */
    public Number profileNumberProperty(String key, ContextProperties defaults)
            throws ServiceException {
        Number property = (Number) get(key);
        if (property != null) {
            return property;
        }
        property = defaults.getNumberProperty(key);
        if (property != null) {
            return property;
        }
        throw log.noProfileProperty(this, key);
    }

    /**
     * Returns a profile boolean property. If the profile property was not set
     * return the default value from the default properties.
     * 
     * @param key
     *            the property {@link String} key.
     * 
     * @param defaults
     *            default {@link ContextProperties} properties.
     * 
     * @return the {@link Boolean} value of the profile property.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     * 
     * @throws ClassCastException
     *             if the profile property is not a number.
     */
    public Boolean profileBooleanProperty(String key, ContextProperties defaults)
            throws ServiceException {
        Boolean property = (Boolean) get(key);
        if (property != null) {
            return property;
        }
        property = defaults.getBooleanProperty(key);
        if (property != null) {
            return property;
        }
        throw log.noProfileProperty(this, key);
    }

    /**
     * Returns a list profile property. If the profile property was not set
     * return the default value from the default properties.
     * 
     * @param key
     *            the property {@link String} key.
     * 
     * @param defaults
     *            default {@link ContextProperties} properties.
     * 
     * @return the {@link List} value of the profile property.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     */
    public List<String> profileListProperty(String key,
            ContextProperties defaults) throws ServiceException {
        List<String> property = getList(key);
        if (property != null) {
            return property;
        }
        property = defaults.getListProperty(key);
        if (property != null) {
            return property;
        }
        throw log.noProfileProperty(this, key);
    }

    /**
     * Returns a list profile property. If the profile property was not set
     * return the default value from the default properties. The specified
     * format is used to create the list items.
     * 
     * @param key
     *            the property {@link String} key.
     * 
     * @param format
     *            the {@link Format} that is used to parse the string properties
     *            and create the list items.
     * 
     * @param defaults
     *            default {@link ContextProperties} properties.
     * 
     * @return the {@link List} value of the profile property.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     * 
     * @throws ParseException
     *             if the profile property could not be parsed.
     */
    public <T> List<T> profileTypedListProperty(String key, Format format,
            ContextProperties defaults) throws ServiceException, ParseException {
        List<String> values = getList(key);
        if (values != null) {
            return asTypedList(values, format);
        }
        List<T> property = defaults.getTypedListProperty(key, format, ",");
        if (property != null) {
            return property;
        }
        throw log.noProfileProperty(this, key);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> asTypedList(List<String> property, Format format)
            throws ParseException {
        List<T> list = new ArrayList<T>();
        for (String value : property) {
            list.add((T) format.parseObject(value));
        }
        return list;
    }

    /**
     * Returns the profile directory path property. If the profile property was
     * not set return the default value from the default properties.
     * 
     * @param key
     *            the key of the profile property.
     * 
     * @param p
     *            default {@link ContextProperties} properties.
     * 
     * @return the profile directory {@link File} path.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     */
    public File profileDirProperty(String key, ContextProperties defaults)
            throws ServiceException {
        Object path = profileProperty(key, defaults);
        if (path instanceof File) {
            return (File) path;
        } else {
            return new File(path.toString());
        }
    }

    /**
     * Returns the profile path property. If the profile property was not set
     * return the default value from the default properties. If the path is not
     * absolute then it is assume to be under the specified parent directory.
     * 
     * @param key
     *            the key of the profile property.
     * 
     * @param parent
     *            the parent {@link File} directory.
     * 
     * @param p
     *            default {@link ContextProperties} properties.
     * 
     * @return the profile file {@link File} path.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     */
    public File profileFileProperty(String key, File parent,
            ContextProperties defaults) throws ServiceException {
        Object path = profileProperty(key, defaults);
        if (path instanceof File) {
            return (File) path;
        } else {
            File file = new File(path.toString());
            return file.isAbsolute() ? file : new File(parent, path.toString());
        }
    }

    /**
     * Returns the profile resource URI property. If the profile property was
     * not set return the default value from the default properties.
     * 
     * @param key
     *            the key of the profile property.
     * 
     * @param p
     *            default {@link ContextProperties} properties.
     * 
     * @return the profile resource {@link URI}.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     */
    public URI profileURIProperty(String key, ContextProperties defaults)
            throws ServiceException {
        Object path = profileProperty(key, defaults);
        if (path instanceof File) {
            return ((File) path).toURI();
        }
        if (path instanceof URI) {
            return (URI) path;
        }
        try {
            return new URI(path.toString());
        } catch (URISyntaxException e) {
            return new File(path.toString()).toURI();
        }
    }

    /**
     * Returns the profile resource locale property. If the profile property was
     * not set return the default value from the default properties.
     * 
     * @param key
     *            the key of the profile property.
     * 
     * @param p
     *            default {@link ContextProperties} properties.
     * 
     * @return the profile resource {@link Locale}.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     * 
     * @throws ParseException
     *             if the locale could not be parsed.
     */
    public Locale profileLocaleProperty(String key, ContextProperties defaults)
            throws ServiceException, ParseException {
        Locale property = (Locale) get(key);
        if (property != null) {
            return property;
        }
        String tag = defaults.getProperty(key);
        if (tag != null) {
            return localeFormatFactory.create().parse(tag);
        }
        throw log.noProfileProperty(this, key);
    }

    /**
     * Returns the profile resource character set property. If the profile
     * property was not set return the default value from the default
     * properties.
     * 
     * @param key
     *            the key of the profile property.
     * 
     * @param p
     *            default {@link ContextProperties} properties.
     * 
     * @return the profile resource {@link Charset}.
     * 
     * @throws ServiceException
     *             if the profile property was not found.
     */
    public Charset profileCharsetProperty(String key, ContextProperties defaults)
            throws ServiceException {
        Charset property = (Charset) get(key);
        if (property != null) {
            return property;
        }
        property = defaults.getCharsetProperty(key);
        if (property != null) {
            return property;
        }
        throw log.noProfileProperty(this, key);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
