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

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.repo.service.RepoService
import com.google.inject.assistedinject.Assisted

/**
 * Deploys the <i>Apt</i> sources list.
 *
 * @see DeployAptSourcesListFactory
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DeployAptSourcesList {

    @Inject
    @Assisted
    RepoService service

    @Inject
    @Assisted
    Object script

    @Inject
    @Assisted
    Map sources

    List config

    TemplateResource sourcesConfiguration

    /**
     * Deploys the sources list.
     */
    void deploySources() {
        def file = sourcesFile
        this.config = readLines(file, charset)
        deployRepositories true, "distribution", "distributionName", "defaultComponents"
        deployRepositories false, "security", "securityDistributionName", "defaultSecurityComponents"
        deployRepositories false, "updates", "updatesDistributionName", "defaultUpdatesComponents"
        deployRepositories false, "backports", "backportsDistributionName", "defaultBackportsComponents"
        writeLines file, charset.name(), config
    }

    /**
     * Deploys the set enabled components.
     */
    void deployEnableComponents() {
        def file = sourcesFile
        this.config = readLines(file, charset)
        def type = repositoryType
        def dist = distributionName
        def repo = defaultRepository
        service.enableComponents?.each { String comp ->
            def found = sources.distribution.find { it.comps.contains(comp) } != null
            if (!found) {
                deployRepositoryComment type: type, repo: repo, dist: dist, comps: comp, line: -1
                deployRepository type: type, repo: repo, dist: dist, comps: comp, line: -1
            }
        }
        writeLines file, charset.name(), config
    }

    final void deployRepositories(boolean useDefaults, String name, String distributionName, String defaultComponents) {
        def type = repositoryType
        def lines = []
        def missed = []
        service.repositories?.each { String repo ->
            def dist = null
            def comps = null
            if (service.repositoriesDistribution != null) {
                dist = service.repositoriesDistribution[repo]
            }
            if (dist == null && !useDefaults) {
                return
            }
            dist = dist == null ? script."$distributionName" : dist
            if (service.repositoriesComponents != null) {
                comps = service.repositoriesComponents[repo]
            }
            comps = comps == null ? script."$defaultComponents" : comps
            def repoinfos = sources[name]
            def skippedComps = new ArrayList(comps)
            comps.each { String comp ->
                repoinfos.findAll({ repoMatchesInfo(type, dist, comp, it, lines) }).each { Map repoinfo ->
                    lines << repoinfo.line
                    skippedComps.removeAll(repoinfo.comps)
                    deployRepository type: type, repo: repo, dist: dist, comps: repoinfo.comps, line: repoinfo.line
                }
            }
            if (!skippedComps.empty) {
                missed << [type: type, repo: repo, dist: dist, comps: skippedComps]
            }
            missed.findAll({ it.type == type && it.dist == script."$distributionName" }).each {
                deployRepositoryComment type: it.type, repo: it.repo, dist: it.dist, comps: it.comps, line: -1
                deployRepository type: it.type, repo: it.repo, dist: it.dist, comps: it.comps, line: -1
            }
        }
    }

    final boolean repoMatchesInfo(String type, String dist, String comp, Map repoinfo, List lines) {
        repoinfo.type == type && repoinfo.dist == dist && repoinfo.comps[0] == comp && !lines.contains(repoinfo.line)
    }

    final void deployRepositoryComment(Map args) {
        def str = sourcesConfiguration.getText(true, "aptRepositoryComment", "args", args)
        if (args.line == -1) {
            config.add ""
            config.add str
        } else {
            config.set args.line, str
        }
    }

    final void deployRepository(Map args) {
        def str = sourcesConfiguration.getText(true, "aptRepository", "args", args)
        if (args.line == -1) {
            config.add str
        } else {
            config.set args.line, str
        }
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "AptConfig"
        this.sourcesConfiguration = templates.getResource("sourcesconfig")
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
