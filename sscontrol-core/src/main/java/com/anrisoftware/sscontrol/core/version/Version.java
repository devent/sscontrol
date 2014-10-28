/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.version;

import static org.apache.commons.lang3.Validate.isTrue;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Comparable version number. Compares the major, minor and revision version
 * numbers.
 *
 * <pre>
 * 1.0 = 1.0
 * 1.0.0 < 1.0.1
 * 0.5.2 > 0.5.0
 * </pre>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Version implements Comparable<Version> {

    private static final String VERSION_POSITIVE = "Version number must be positive";

    static final String REV = "rev";

    private static final String REVISION = "revision";

    static final String MINOR = "minor";

    static final String MAJOR = "major";

    private final int major;

    private final int minor;

    private final int rev;

    /**
     * @see VersionFactory#create(int, int, int)
     */
    @Inject
    Version(@Assisted(MAJOR) int major, @Assisted(MINOR) int minor,
            @Assisted(REV) int rev) {
        isTrue(major > -1, VERSION_POSITIVE);
        isTrue(minor > -1, VERSION_POSITIVE);
        isTrue(rev > -1, VERSION_POSITIVE);
        this.major = major;
        this.minor = minor;
        this.rev = rev;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return rev;
    }

    @Override
    public int compareTo(Version o) {
        if (this.major != o.major) {
            return Integer.compare(this.major, o.major);
        }
        if (this.minor != o.minor) {
            return Integer.compare(this.minor, o.minor);
        }
        if (this.rev != o.rev) {
            return Integer.compare(this.rev, o.rev);
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Version)) {
            return false;
        }
        Version rhs = (Version) obj;
        return new EqualsBuilder().append(major, rhs.major)
                .append(minor, rhs.minor).append(rev, rhs.rev).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(major).append(minor).append(rev)
                .toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this).append(MAJOR, major);
        if (minor != Integer.MAX_VALUE) {
            b.append(MINOR, minor);
        }
        if (rev != Integer.MAX_VALUE) {
            b.append(MINOR, rev);
        }
        return b.toString();
    }
}
