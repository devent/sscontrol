/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.memory;

import java.text.ParseException;
import java.util.Map;

import javax.inject.Inject;
import javax.measure.Measure;
import javax.measure.quantity.DataAmount;
import javax.measure.unit.NonSI;

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory;

/**
 * Parses the arguments of memory directives.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MemoryArgs {

    private static final String POST = "post";

    private static final String UPLOAD = "upload";

    private static final String LIMIT = "limit";

    @Inject
    private ByteFormatFactory byteFormatFactory;

    boolean haveLimit(Map<String, Object> args) {
        return args.containsKey(LIMIT);
    }

    Measure<Long, DataAmount> limit(Map<String, Object> args)
            throws ParseException {
        Object limit = args.get(LIMIT);
        long bytes = byteFormatFactory.create().parse(limit.toString());
        return Measure.valueOf(bytes, NonSI.BYTE);
    }

    boolean haveUpload(Map<String, Object> args) {
        return args.containsKey(UPLOAD);
    }

    Measure<Long, DataAmount> upload(Map<String, Object> args)
            throws ParseException {
        Object upload = args.get(UPLOAD);
        long bytes = byteFormatFactory.create().parse(upload.toString());
        return Measure.valueOf(bytes, NonSI.BYTE);
    }

    boolean havePost(Map<String, Object> args) {
        return args.containsKey(POST);
    }

    Measure<Long, DataAmount> post(Map<String, Object> args)
            throws ParseException {
        Object post = args.get(POST);
        long bytes = byteFormatFactory.create().parse(post.toString());
        return Measure.valueOf(bytes, NonSI.BYTE);
    }

}
