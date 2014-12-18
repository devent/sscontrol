/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.repo.service;

import static com.anrisoftware.sscontrol.repo.service.RepoServiceFactory.NAME;
import groovy.lang.Script;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;

/**
 * Repository service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class RepoServiceImpl extends AbstractService implements RepoService {

    private static final String KEY_KEY = "key";

    private static final String SIGN_KEY = "sign";

    private static final String ENABLE_KEY = "enable";

    private static final String COMPONENTS_KEY = "components";

    private static final String DISTRIBUTION_KEY = "distribution";

    private static final String REPOSITORY_KEY = "repository";

    private static final String PROXY_KEY = "proxy";

    private StatementsMap statementsMap;

    private StatementsTable statementsTable;

    @Override
    protected Script getScript(String profileName) throws ServiceException {
        ServiceScriptFactory scriptFactory = findScriptFactory(NAME);
        return (Script) scriptFactory.getScript();
    }

    /**
     * Because we load the script from a script service the dependencies are
     * already injected.
     */
    @Override
    protected void injectScript(Script script) {
    }

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, NAME);
        map.addAllowed(PROXY_KEY, ENABLE_KEY);
        map.setAllowValue(true, PROXY_KEY);
        map.addAllowedKeys(ENABLE_KEY, COMPONENTS_KEY);
        this.statementsMap = map;
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, NAME);
        table.addAllowed(REPOSITORY_KEY, SIGN_KEY);
        table.addAllowedKeys(REPOSITORY_KEY, DISTRIBUTION_KEY, COMPONENTS_KEY);
        table.addAllowedKeys(SIGN_KEY, KEY_KEY);
        this.statementsTable = table;
    }

    /**
     * Entry point for the repository service script.
     *
     * @param statements
     *            the repository service statements.
     *
     * @return this {@link Service}.
     */
    public Service repo(Object statements) {
        return this;
    }

    @Override
    public String getProxy() {
        return statementsMap.value(PROXY_KEY);
    }

    @Override
    public Map<String, Object> getSignKeys() {
        return statementsTable.tableKeys(SIGN_KEY, KEY_KEY);
    }

    @Override
    public Set<String> getRepositories() {
        return statementsTable.tableValues(REPOSITORY_KEY);
    }

    @Override
    public List<String> getEnableComponents() {
        return statementsMap.mapValueAsStringList(ENABLE_KEY, COMPONENTS_KEY);
    }

    @Override
    public Map<String, String> getRepositoriesDistribution() {
        return statementsTable.tableKeys(REPOSITORY_KEY, DISTRIBUTION_KEY);
    }

    @Override
    public Map<String, List<String>> getRepositoriesComponents() {
        return statementsTable.tableKeysAsList(REPOSITORY_KEY, COMPONENTS_KEY);
    }

    public Object methodMissing(String name, Object args)
            throws StatementsException {
        try {
            statementsMap.methodMissing(name, args);
        } catch (StatementsException e) {
            statementsTable.methodMissing(name, args);
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
