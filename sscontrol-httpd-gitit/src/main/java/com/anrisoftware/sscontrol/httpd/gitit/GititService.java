/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit;

import static com.anrisoftware.globalpom.format.byteformat.UnitMultiplier.KILO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.format.byteformat.ByteFormatFactory;
import com.anrisoftware.globalpom.format.duration.DurationFormatFactory;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.bindings.Address;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.bindings.BindingArgs;
import com.anrisoftware.sscontrol.core.bindings.BindingFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMap;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Gitit</i> service.
 *
 * @see <a href="http://gitit.net">http://gitit.net</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class GititService implements WebService {

    private static final String UPLOAD = "upload";
    private static final String FEEDS_REFRESH = "refresh";
    private static final String FEEDS_DURATION = "duration";
    private static final String ACCESS_ANSWER = "answer";
    private static final String ACCESS_QUESTION = "question";
    private static final String PUBLICKEY = "publickey";
    private static final String PRIVATEKEY = "privatekey";
    private static final String RESPONSES = "responses";
    private static final String MEMORY_PAGE = "page";
    private static final String MEMORY_UPLOAD = UPLOAD;
    private static final String TYPE = "type";
    private static final String METHOD = "method";
    private static final String REQUIRED = "required";
    private static final String TITLE = "title";
    private static final String GC = "gc";
    private static final String ENABLED = "enabled";
    private static final String FEEDS = "feeds";
    private static final String ACCESS = "access";
    private static final String RECAPTCHA = "recaptcha";
    private static final String COMPRESS = "compress";
    private static final String MEMORY = "memory";
    private static final String IDLE = "idle";
    private static final String CACHING = "caching";
    private static final String TABLEOFCONTENTS = "tableofcontents";
    private static final String DEFAULTSUMMARY = "defaultsummary";
    private static final String NOEDIT = "noedit";
    private static final String NODELETE = "nodelete";
    private static final String FRONTPAGE = "frontpage";
    private static final String MATH = "math";
    private static final String PAGE = "page";
    private static final String AUTH = "auth";
    private static final String LOGIN = "login";
    private static final String WIKI = "wiki";

    /**
     * The <i>Gitit</i> service name.
     */
    public static final String SERVICE_NAME = "gitit";

    private final DefaultWebService service;

    private final GititServiceLogger log;

    private final StatementsMap statementsMap;

    @Inject
    private Binding binding;

    @Inject
    private BindingArgs bindingArgs;

    @Inject
    private ByteFormatFactory byteFormatFactory;

    @Inject
    private DurationFormatFactory durationFormatFactory;

    private DebugLoggingFactory debugFactory;

    private DebugLogging debug;

    private OverrideMode overrideMode;

    private RepositoryType type;

    /**
     * @see GititServiceFactory#create(Map, Domain)
     */
    @Inject
    GititService(DefaultWebServiceFactory webServiceFactory,
            GititServiceLogger log, @Assisted Map<String, Object> args,
            @Assisted Domain domain) {
        this.log = log;
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(service.getStatementsMap());
        parseArgs(log, args);
    }

    private void parseArgs(GititServiceLogger log, Map<String, Object> args) {
        if (log.haveType(args)) {
            this.type = log.type(this, args);
        }
    }

    private void setupStatements(StatementsMap map) {
        map.addAllowed(WIKI, LOGIN, AUTH, PAGE, MATH, FRONTPAGE, NODELETE,
                NOEDIT, DEFAULTSUMMARY, TABLEOFCONTENTS, CACHING, IDLE, MEMORY,
                COMPRESS, RECAPTCHA, ACCESS, FEEDS);
        map.setAllowValue(true, MATH, FRONTPAGE, NODELETE, NOEDIT,
                DEFAULTSUMMARY, TABLEOFCONTENTS);
        map.addAllowedKeys(WIKI, TITLE);
        map.addAllowedKeys(LOGIN, REQUIRED);
        map.addAllowedKeys(AUTH, METHOD);
        map.addAllowedKeys(PAGE, TYPE);
        map.addAllowedKeys(CACHING, ENABLED);
        map.addAllowedKeys(IDLE, GC);
        map.addAllowedKeys(MEMORY, UPLOAD, PAGE);
        map.addAllowedKeys(COMPRESS, RESPONSES);
        map.addAllowedKeys(RECAPTCHA, ENABLED, PRIVATEKEY, PUBLICKEY);
        map.addAllowedKeys(ACCESS, ACCESS_QUESTION, ACCESS_ANSWER);
        map.addAllowedKeys(FEEDS, ENABLED, FEEDS_DURATION, FEEDS_REFRESH);
    }

    @Inject
    public void setDebugLoggingFactory(DebugLoggingFactory factory) {
        this.debugFactory = factory;
        this.debug = factory.createOff();
    }

    @Override
    public Domain getDomain() {
        return service.getDomain();
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    public void setAlias(String alias) throws ServiceException {
        service.setAlias(alias);
    }

    @Override
    public String getAlias() {
        return service.getAlias();
    }

    public void setId(String id) throws ServiceException {
        service.setId(id);
    }

    @Override
    public String getId() {
        return service.getId();
    }

    public void setRef(String ref) throws ServiceException {
        service.setRef(ref);
    }

    @Override
    public String getRef() {
        return service.getRef();
    }

    public void setRefDomain(String ref) throws ServiceException {
        service.setRefDomain(ref);
    }

    @Override
    public String getRefDomain() {
        return service.getRefDomain();
    }

    public void setPrefix(String prefix) throws ServiceException {
        service.setPrefix(prefix);
    }

    @Override
    public String getPrefix() {
        return service.getPrefix();
    }

    /**
     * Sets the IP addresses or host names to where to bind the <i>Gitit</i>
     * service.
     *
     * @see BindingFactory#create(Map, String...)
     */
    public void bind(Map<String, Object> args) throws ServiceException {
        List<Address> addresses = bindingArgs.createAddress(this, args);
        binding.addAddress(addresses);
        log.bindingSet(this, binding);
    }

    /**
     * Sets the IP addresses or host names to where to bind the <i>Gitit</i>
     * service.
     *
     * @see BindingFactory#create(BindingAddress)
     */
    public void bind(BindingAddress address) throws ServiceException {
        binding.addAddress(address);
        log.bindingSet(this, binding);
    }

    /**
     * Returns a list of the IP addresses where to bind the <i>Gitit</i>
     * service.
     *
     * @return the {@link Binding}.
     */
    public Binding getBinding() {
        return binding;
    }

    public void debug(boolean enabled) {
        DebugLogging logging = debugFactory.create(enabled ? 1 : 0);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public void debug(Map<String, Object> args) {
        DebugLogging logging = debugFactory.create(args);
        log.debugSet(this, logging);
        this.debug = logging;
    }

    public DebugLogging getDebugLogging() {
        if (debug == null) {
            this.debug = debugFactory.createOff();
        }
        return debug;
    }

    public void override(Map<String, Object> args) {
        OverrideMode mode = log.override(this, args);
        log.overrideModeSet(this, mode);
        this.overrideMode = mode;
    }

    public void setOverrideMode(OverrideMode mode) {
        this.overrideMode = mode;
    }

    public OverrideMode getOverrideMode() {
        return overrideMode;
    }

    public void setType(RepositoryType type) {
        this.type = type;
    }

    public RepositoryType getType() {
        return type;
    }

    public void setCaching(boolean enabled) throws ServiceException {
        statementsMap.putMapValue(CACHING, ENABLED, enabled);
    }

    public Boolean getCaching() {
        Object value = statementsMap.mapValue(CACHING, ENABLED);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    public void setWikiTitle(String title) throws ServiceException {
        statementsMap.putMapValue(WIKI, TITLE, title);
    }

    public String getWikiTitle() {
        return statementsMap.mapValue(WIKI, TITLE);
    }

    public void setLoginRequired(LoginRequired required)
            throws ServiceException {
        statementsMap.putMapValue(LOGIN, REQUIRED, required);
    }

    public LoginRequired getLoginRequired() {
        return statementsMap.mapValue(LOGIN, REQUIRED);
    }

    public void setAuthMethod(AuthMethod method) throws ServiceException {
        statementsMap.putMapValue(AUTH, METHOD, method);
    }

    public AuthMethod getAuthMethod() {
        return statementsMap.mapValue(AUTH, METHOD);
    }

    public void setPageType(String type) throws ServiceException {
        statementsMap.putMapValue(PAGE, TYPE, type);
    }

    public String getPageType() {
        return statementsMap.mapValue(PAGE, TYPE);
    }

    public void setMath(String math) throws ServiceException {
        statementsMap.putValue(MATH, math);
    }

    public String getMath() {
        return statementsMap.value(MATH);
    }

    public void setFrontPage(String page) throws ServiceException {
        statementsMap.putValue(FRONTPAGE, page);
    }

    public String getFrontPage() {
        return statementsMap.value(FRONTPAGE);
    }

    public void setNoDeletePages(List<String> pages) throws ServiceException {
        statementsMap.putValue(NODELETE, pages);
    }

    public List<String> getNoDeletePages() {
        return statementsMap.valueAsStringList(NODELETE);
    }

    public void setNoEditPages(List<String> pages) throws ServiceException {
        statementsMap.putValue(NOEDIT, pages);
    }

    public List<String> getNoEditPages() {
        return statementsMap.valueAsStringList(NOEDIT);
    }

    public void setDefaultSummary(String summary) throws ServiceException {
        statementsMap.putValue(DEFAULTSUMMARY, summary);
    }

    public String getDefaultSummary() {
        return statementsMap.value(DEFAULTSUMMARY);
    }

    public void setTableOfContents(boolean enabled) throws ServiceException {
        statementsMap.putValue(TABLEOFCONTENTS, enabled);
    }

    public Boolean getTableOfContents() {
        Object value = statementsMap.value(TABLEOFCONTENTS);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    public void setIdleGc(boolean gc) throws ServiceException {
        statementsMap.putMapValue(IDLE, GC, gc);
    }

    public Boolean getIdleGc() {
        Object value = statementsMap.mapValue(IDLE, GC);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    public void setMemoryUpload(String kb) throws ServiceException {
        statementsMap.putMapValue(MEMORY, MEMORY_UPLOAD, kb);
    }

    public Long getMemoryUpload() throws ParseException {
        String value = statementsMap.mapValue(MEMORY, MEMORY_UPLOAD);
        if (value != null) {
            return byteFormatFactory.create().parse(value, KILO);
        } else {
            return null;
        }
    }

    public void setMemoryPage(String kb) throws ServiceException {
        statementsMap.putMapValue(MEMORY, MEMORY_PAGE, kb);
    }

    public Long getMemoryPage() throws ParseException {
        String value = statementsMap.mapValue(MEMORY, MEMORY_PAGE);
        if (value != null) {
            return byteFormatFactory.create().parse(value, KILO);
        } else {
            return null;
        }
    }

    public void setCompressResponses(boolean responses) throws ServiceException {
        statementsMap.putMapValue(COMPRESS, RESPONSES, responses);
    }

    public Boolean getCompressResponses() {
        Object value = statementsMap.mapValue(COMPRESS, RESPONSES);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    public void setRecaptchaEnable(boolean enabled) throws ServiceException {
        statementsMap.putMapValue(RECAPTCHA, ENABLED, enabled);
    }

    public Boolean getRecaptchaEnable() {
        Object value = statementsMap.mapValue(RECAPTCHA, ENABLED);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    public void setRecaptchaPrivateKey(String key) throws ServiceException {
        statementsMap.putMapValue(RECAPTCHA, PRIVATEKEY, key);
    }

    public String getRecaptchaPrivateKey() {
        return statementsMap.mapValue(RECAPTCHA, PRIVATEKEY);
    }

    public void setRecaptchaPublicKey(String key) throws ServiceException {
        statementsMap.putMapValue(RECAPTCHA, PUBLICKEY, key);
    }

    public String getRecaptchaPublicKey() {
        return statementsMap.mapValue(RECAPTCHA, PUBLICKEY);
    }

    public void setAccessQuestion(String question) throws ServiceException {
        statementsMap.putMapValue(ACCESS, ACCESS_QUESTION, question);
    }

    public String getAccessQuestion() {
        return statementsMap.mapValue(ACCESS, ACCESS_QUESTION);
    }

    public void setAccessAnswers(String answers) throws ServiceException {
        statementsMap.putMapValue(ACCESS, ACCESS_ANSWER, answers);
    }

    public String getAccessAnswers() {
        return statementsMap.mapValue(ACCESS, ACCESS_ANSWER);
    }

    public void setFeedsEnabled(boolean enabled) throws ServiceException {
        statementsMap.putMapValue(FEEDS, ENABLED, enabled);
    }

    public Boolean getFeedsEnabled() {
        Object value = statementsMap.mapValue(FEEDS, ENABLED);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    public void setFeedsDuration(String days) throws ServiceException {
        statementsMap.putMapValue(FEEDS, FEEDS_DURATION, days);
    }

    public Long getFeedsDuration() throws ParseException {
        String d = statementsMap.mapValue(FEEDS, FEEDS_DURATION);
        if (d != null) {
            return durationFormatFactory.create().parse(d).getStandardDays();
        } else {
            return null;
        }
    }

    public void setFeedsRefresh(String minutes) throws ServiceException {
        statementsMap.putMapValue(FEEDS, FEEDS_REFRESH, minutes);
    }

    public Long getFeedsRefresh() throws ParseException {
        String d = statementsMap.mapValue(FEEDS, FEEDS_REFRESH);
        if (d != null) {
            return durationFormatFactory.create().parse(d).getStandardMinutes();
        } else {
            return null;
        }
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        return service.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
