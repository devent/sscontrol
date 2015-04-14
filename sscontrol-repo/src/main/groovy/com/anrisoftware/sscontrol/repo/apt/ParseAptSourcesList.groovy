/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.apt

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.sscontrol.repo.service.RepoService
import com.google.inject.assistedinject.Assisted

/**
 * Parses the <i>Apt</i> sources list.
 *
 * @see ParseAptSourcesListFactory
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseAptSourcesList {

    @Inject
    @Assisted
    RepoService service

    @Inject
    @Assisted
    Object script

    Map sources

    /**
     * Reads the current sources list.
     */
    Map readSources() {
        this.sources = [distribution: [], security: [], updates: [], backports: []]
        def file = sourcesFile
        readLines(file, charset).eachWithIndex { value, index ->
            if (!value.matches(/#.*/) && !value.empty) {
                parseRepository value, index, "distribution", "distributionName"
                parseRepository value, index, "security", "securityDistributionName"
                parseRepository value, index, "updates", "updatesDistributionName"
                parseRepository value, index, "backports", "backportsDistributionName"
            }
        }
        return sources
    }

    Map parseRepository(String value, int index, String name, String distributionName) {
        def list = parseRepository0(value, index, distributionName, [])
        if (!list.empty) {
            sources[name].addAll(list)
        }
        return sources
    }

    List parseRepository0(String value, int index, String distributionName, List sources) {
        def dist = script."$distributionName"
        def line = StringUtils.split(value, ' ')
        if (line[2] == dist) {
            sources << [line: index, type: line[0], repo: line[1], dist: line[2], comps: line[3..-1]]
        }
        return sources
    }


    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
