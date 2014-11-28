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
package com.anrisoftware.sscontrol.core.checkfilehash;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.IOUtils.readLines;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Checks the hash of the specified file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CheckFileHash implements Callable<CheckFileHash> {

    private static final String SEP = " ";

    private static final String HASH_RESOURCE = "hash resource";

    private static final String FILE = "file";

    private final CheckFileHashLogger log;

    private final URI file;

    private final URI hashResource;

    private final HashName hashName;

    private boolean matching;

    /**
     * @see CheckFileHashFactory#create(Map, Object)
     */
    @Inject
    CheckFileHash(CheckFileHashLogger log, @Assisted Map<String, Object> args,
            @Assisted Object script) {
        this.log = log;
        this.file = log.file(script, args);
        this.hashResource = log.hash(script, args);
        this.hashName = hashName(hashResource);
    }

    private HashName hashName(URI hashResource) {
        if (StringUtils.equals(hashResource.getScheme(), "md5")) {
            return HashName.forExtension("md5");
        } else if (StringUtils.equals(hashResource.getScheme(), "sha1")) {
            return HashName.forExtension("sha1");
        } else {
            String ex = getExtension(hashResource.getPath());
            return HashName.forExtension(ex);
        }
    }

    @Override
    public CheckFileHash call() throws Exception {
        String hashstr = readHash(hashResource, hashName);
        String expectedHashstr = readExpectedHash(hashResource);
        this.matching = hashstr.equals(expectedHashstr);
        log.hashMatching(this, expectedHashstr, hashstr, matching);
        return this;
    }

    /**
     * Returns if the hash of the file and the specified hash is matching.
     *
     * @return {@code true} if the hashes is matching.
     */
    public boolean isMatching() {
        return matching;
    }

    private String readHash(URI hashResource, HashName hashName)
            throws NoSuchAlgorithmException, IOException, MalformedURLException {
        MessageDigest md = MessageDigest.getInstance(hashName.getHashName());
        InputStream fis = file.toURL().openStream();
        byte[] mdbytes = readFile(md, fis);
        String hashstr = toHexString(mdbytes);
        return hashstr;
    }

    private String readExpectedHash(URI hashResource) throws Exception {
        if (StringUtils.equals(hashResource.getScheme(), "md5")) {
            return hashResource.getSchemeSpecificPart();
        } else if (StringUtils.equals(hashResource.getScheme(), "sha1")) {
            return hashResource.getSchemeSpecificPart();
        } else {
            String line = readLines(hashResource.toURL().openStream()).get(0);
            String hash = StringUtils.split(line, SEP)[0];
            return hash;
        }
    }

    private String toHexString(byte[] mdbytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mdbytes.length; i++) {
            String s = Integer.toString((mdbytes[i] & 0xff) + 0x100, 16);
            sb.append(s.substring(1));
        }
        return sb.toString();
    }

    private byte[] readFile(MessageDigest md, InputStream fis)
            throws IOException {
        byte[] dataBytes = new byte[1024];
        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        return md.digest();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(FILE, file)
                .append(HASH_RESOURCE, hashResource).toString();
    }
}
