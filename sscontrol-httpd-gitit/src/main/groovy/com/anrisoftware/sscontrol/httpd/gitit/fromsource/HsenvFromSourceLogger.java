package com.anrisoftware.sscontrol.httpd.gitit.fromsource;

import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_gitit_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_gitit_info;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_gitit_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_hsenv_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_hsenv_info;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.install_packages_done_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSourceLogger._.unpack_archive_info;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging for {@link HsenvFromSource}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HsenvFromSourceLogger extends AbstractLogger {

    enum _ {

        install_packages_done_trace("Install packages done {} for {}, {}."),

        install_packages_done_debug("Install packages done {} for {}."),

        install_packages_done_info("Install packages done {} for service '{}'."),

        unpack_archive_debug("Unpack archive '{}' done for {}."),

        unpack_archive_info("Unpack Gitit archive '{}' done for service '{}'."),

        install_hsenv_debug("Install hsenv packages {} done for {}."),

        install_hsenv_info("Install hsenv packages {} done for service '{}'."),

        install_gitit_trace("Install Gitit done {} for {}, {}."),

        install_gitit_debug("Install Gitit done {} for {}."),

        install_gitit_info("Install Gitit done {} for service '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link HsenvFromSource}.
     */
    public HsenvFromSourceLogger() {
        super(HsenvFromSource.class);
    }

    void installHsenvCabalPackagesDone(HsenvFromSource config,
            ScriptCommandWorker worker, @SuppressWarnings("rawtypes") Map args) {
        if (isTraceEnabled()) {
            trace(install_packages_done_trace, args, config, worker);
        } else if (isDebugEnabled()) {
            debug(install_packages_done_debug, args, config);
        } else {
            info(install_packages_done_info, args, config.getName());
        }
    }

    void unpackArchiveDone(HsenvFromSource config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getName());
        }
    }

    @SuppressWarnings("rawtypes")
    void installHsenvDone(HsenvFromSource config, List packages) {
        if (isDebugEnabled()) {
            debug(install_hsenv_debug, packages, config);
        } else {
            info(install_hsenv_info, packages, config.getName());
        }
    }

    void installGititDone(HsenvFromSource config, ScriptCommandWorker worker,
            @SuppressWarnings("rawtypes") Map args) {
        if (isTraceEnabled()) {
            trace(install_gitit_trace, args, config, worker);
        } else if (isDebugEnabled()) {
            debug(install_gitit_debug, args, config);
        } else {
            info(install_gitit_info, args, config.getName());
        }
    }

}
