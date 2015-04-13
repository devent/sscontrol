/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.versionlimits;

import static org.apache.commons.io.IOUtils.readLines;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.globalpom.version.VersionFormatFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Reads the version from the version file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ReadVersion {

    private final URI versionResource;

    private final Charset charset;

    @Inject
    private VersionFormatFactory versionFormatFactory;

    /**
     * @see ReadVersionFactory#create(URI, Charset)
     */
    @Inject
    ReadVersion(@Assisted URI versionResource, @Assisted Charset charset) {
        this.versionResource = versionResource;
        this.charset = charset;
    }

    /**
     * Reads the version.
     *
     * @return the {@link Version} or {@code null}.
     *
     * @throws IOException
     *             if there was an error reading the version file.
     *
     * @throws ParseException
     *             if there was an error parsing the version file.
     */
    public Version readVersion() throws IOException, ParseException {
        InputStream stream = versionResource.toURL().openStream();
        if (stream.available() > 0) {
            List<String> lines = readLines(stream, charset);
            if (lines.size() > 0) {
                return versionFormatFactory.create().parse(lines.get(0));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
