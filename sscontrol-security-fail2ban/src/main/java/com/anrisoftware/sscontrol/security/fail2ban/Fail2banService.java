package com.anrisoftware.sscontrol.security.fail2ban;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.security.service.SecService;

/**
 * <i>Fail2ban</i> service.
 *
 * @see <a href="http://www.fail2ban.org">http://www.fail2ban.org</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Fail2banService extends SecService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["sshd": 5]}
     * </pre>
     *
     * <pre>
     * service "fail2ban", {
     *     debug "service", level: 1
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> debugLogging(String key);

    /**
     * Returns the jails.
     * <p>
     *
     * <pre>
     * service "fail2ban", {
     *     jail "apache", notify: "root@localhost", {
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} of {@link Jail} jails.
     */
    List<Jail> getJails();

}
