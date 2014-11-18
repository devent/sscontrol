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
package com.anrisoftware.sscontrol.scripts.localuserinfo;

import static org.apache.commons.lang3.StringUtils.split;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.CommandExecException;
import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Returns information about the local user.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class LocalUserInfo implements Callable<LocalUserInfo> {

    private static final String UNIX = "unix";

    private static final String USER_INFO_NAME = "userinfo";

    private static final String ID_PATTERN_NAME = "idPattern";

    private static final String TEMPLATES_NAME = "ScriptsUnixTemplates";

    private final LocalUserInfoLogger log;

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private TemplatesFactory templatesFactory;

    private TemplateResource userInfoTemplate;

    private Pattern idPattern;

    private Integer uid;

    private String userName;

    private Integer gid;

    private String groupName;

    private Map<String, Integer> groups;

    /**
     * @see LocalUserInfoFactory#create(Map, Object, Threads)
     */
    @Inject
    LocalUserInfo(LocalUserInfoLogger log, @Assisted Map<String, Object> args,
            @Assisted Object parent, @Assisted Threads threads) {
        this.log = log;
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        args.put("outString", true);
        args.put("exitCodes", new int[] { 0, 1 });
        log.userName(args, parent);
        log.command(args, parent);
    }

    @Inject
    public void setTemplatesFactory(TemplatesFactory factory) {
        Templates templates = templatesFactory.create(TEMPLATES_NAME);
        this.userInfoTemplate = templates.getResource(USER_INFO_NAME);
        this.idPattern = Pattern.compile(userInfoTemplate
                .getText(ID_PATTERN_NAME));
    }

    @Override
    public LocalUserInfo call() throws Exception {
        ProcessTask task = scriptExecFactory.create(args, parent, threads,
                userInfoTemplate, UNIX).call();
        this.groups = new HashMap<String, Integer>();
        if (task.getExitValue() == 0) {
            parseUser(task.getOut().trim());
        } else if (task.getExitValue() == 1) {
        }
        return this;
    }

    private void parseUser(String out) throws CommandExecException {
        Matcher matcher = idPattern.matcher(out);
        log.checkMatch(this, out, matcher.find());
        parserGroups(matcher.group(5));
        this.uid = Integer.valueOf(matcher.group(1));
        this.userName = matcher.group(2);
        this.gid = Integer.valueOf(matcher.group(3));
        this.groupName = matcher.group(4);
    }

    private void parserGroups(String group) {
        String[] s = split(group, "(),");
        for (int i = 0; i < s.length; i += 2) {
            groups.put(s[i + 1], Integer.valueOf(s[i]));
        }
    }

    public Pattern getIdPattern() {
        return idPattern;
    }

    public Integer getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getGid() {
        return gid;
    }

    public String getGroupName() {
        return groupName;
    }

    public Map<String, Integer> getGroups() {
        return Collections.unmodifiableMap(groups);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
