/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.scripts.findusedport;

import static java.lang.String.format;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.google.inject.assistedinject.Assisted;

/**
 * Find the services for the specified ports.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class FindUsedPort implements Callable<FindUsedPort> {

    private static Pattern SERVICES_PATTERN = Pattern.compile("\\d+\\/.*");

    private final Map<String, Object> args;

    private final Threads threads;

    private final List<Integer> ports;

    @Inject
    private FindUsedPortProcessFactory findUsedPortProcessFactory;

    private Map<Integer, String> services;

    /**
     * @see FindUsedPortFactory#create(Map, Object, Threads)
     */
    @Inject
    FindUsedPort(FindUsedPortLogger log, @Assisted Map<String, Object> args,
            @Assisted Object parent, @Assisted Threads threads) {
        this.args = args;
        this.threads = threads;
        this.ports = log.ports(args, parent);
        log.command(args, parent);
    }

    public Map<Integer, String> getServices() {
        return Collections.unmodifiableMap(services);
    }

    @Override
    public FindUsedPort call() throws Exception {
        ProcessTask task;
        task = findUsedPortProcessFactory.create(threads, args).call();
        this.services = findServices(task);
        return this;
    }

    private Map<Integer, String> findServices(ProcessTask task) {
        String out = task.getOut();
        String[] lines = StringUtils.split(out, "\n");
        Map<Integer, String> services = new HashMap<Integer, String>();
        for (int i = 0; i < lines.length; i++) {
            for (Integer port : ports) {
                if (StringUtils.contains(lines[i], format(":%d ", port))) {
                    parsePortService(services, port, lines[i]);
                }
            }
        }
        return services;
    }

    private void parsePortService(Map<Integer, String> services, int port,
            String line) {
        String[] split = StringUtils.split(line, " ");
        String service = null;
        for (int i = 0; i < split.length; i++) {
            if (SERVICES_PATTERN.matcher(split[i]).matches()) {
                service = split[i];
            }
        }
        String[] servicesplit = StringUtils.split(service, "/");
        service = servicesplit[servicesplit.length - 1];
        services.put(port, service);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
