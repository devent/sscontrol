/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Memory directives.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Memory {

    private static final String POST = "post";

    private static final String UPLOAD = "upload";

    private static final String LIMIT = "limit";

    private Measure<Long, DataAmount> limit;

    private Measure<Long, DataAmount> upload;

    private Measure<Long, DataAmount> post;

    /**
     * @see MemoryFactory#create(Map)
     */
    @Inject
    Memory(MemoryArgs aargs, @Assisted Map<String, Object> args)
            throws ParseException {
        if (aargs.haveLimit(args)) {
            this.limit = aargs.limit(args);
        }
        if (aargs.haveUpload(args)) {
            this.upload = aargs.upload(args);
        } else {
            this.upload = limit;
        }
        if (aargs.havePost(args)) {
            this.post = aargs.post(args);
        } else {
            this.post = limit;
        }
    }

    public Measure<Long, DataAmount> getLimit() {
        return limit;
    }

    public Measure<Long, DataAmount> getUpload() {
        return upload;
    }

    public Measure<Long, DataAmount> getPost() {
        return post;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder = limit != null ? builder.append(LIMIT, limit) : builder;
        builder = upload != null ? builder.append(UPLOAD, upload) : builder;
        builder = post != null ? builder.append(POST, post) : builder;
        return builder.toString();
    }
}
