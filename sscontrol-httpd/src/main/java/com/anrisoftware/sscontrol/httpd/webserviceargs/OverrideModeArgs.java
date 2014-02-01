package com.anrisoftware.sscontrol.httpd.webserviceargs;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Parses override mode arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class OverrideModeArgs {

    private static final String OVERRIDE_MODE = "mode";

    @Inject
    private OverrideModeArgsLogger log;

    /**
     * Parses the override mode.
     * 
     * @param service
     *            the {@link WebService}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link OverrideMode}.
     * 
     * @throws NullPointerException
     *             if the override mode is {@code null}.
     * 
     * @throws IllegalArgumentException
     *             if the override mode is not {@link YesNoFlag} or
     *             {@link OverrideMode}.
     */
    public OverrideMode override(WebService service, Map<String, Object> args) {
        Object mode = args.get(OVERRIDE_MODE);
        log.checkOverrideMode(service, mode);
        if (mode instanceof String) {
            return OverrideMode.valueOf(mode.toString());
        }
        if (mode instanceof YesNoFlag) {
            YesNoFlag flag = (YesNoFlag) mode;
            if (flag == YesNoFlag.no) {
                return OverrideMode.no;
            }
        }
        if (mode instanceof OverrideMode) {
            return (OverrideMode) mode;
        }
        throw log.invalidOverrideMode(service, mode);
    }

}
