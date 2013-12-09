package com.anrisoftware.sscontrol.core.debuglogging;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;

/**
 * Parses arguments for debug logging.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DebugLoggingArgs {

    private static final String MODULE = "module";

    private static final String LEVEL = "logging";

    private static final String MODULES = "modules";

    @Inject
    private DebugLoggingLogger log;

    @Inject
    private StringToListFactory toListFactory;

    int level(Map<String, Object> args) {
        Object level = args.get(LEVEL);
        log.checkLevel(level);
        return (Integer) level;
    }

    boolean haveModule(Map<String, Object> args) {
        return args.containsKey(MODULE);
    }

    String module(Map<String, Object> args) {
        Object module = args.get(MODULE);
        log.checkModule(module);
        return module.toString();
    }

    boolean haveModules(Map<String, Object> args) {
        return args.containsKey(MODULES);
    }

    List<String> modules(Map<String, Object> args) {
        Object modules = args.get(MODULES);
        log.checkModules(modules);
        return toListFactory.create(modules.toString()).getList();
    }

}
