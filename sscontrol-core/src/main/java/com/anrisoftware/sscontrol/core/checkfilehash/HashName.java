/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.checkfilehash;

/**
 * File extensions and hash algorithm names.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum HashName {

    /**
     * MD-5 algorithm.
     */
    md5("md5", "MD5"),

    /**
     * SHA-1 algorithm.
     */
    sha1("sha1", "SHA-1");

    private String fileExtention;

    private String hashName;

    private HashName(String fileExtention, String hashName) {
        this.fileExtention = fileExtention;
        this.hashName = hashName;
    }

    public String getFileExtention() {
        return fileExtention;
    }

    public String getHashName() {
        return hashName;
    }

    /**
     * Returns the hash algorithm name for the specified file extension.
     * 
     * @param ex
     *            the {@link String} file extension.
     * 
     * @return the {@link HashName} or {@code null}.
     */
    public static HashName forExtension(String ex) {
        for (HashName hash : values()) {
            if (ex.equalsIgnoreCase(hash.getFileExtention())) {
                return hash;
            }
        }
        return null;
    }
}
