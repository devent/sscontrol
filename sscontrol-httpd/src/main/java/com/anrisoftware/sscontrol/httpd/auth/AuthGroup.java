package com.anrisoftware.sscontrol.httpd.auth;

import java.util.List;
import java.util.Map;

/**
 * Authentication group.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AuthGroup {

    /**
     * Returns the group name.
     *
     * <pre>
     * group "foogroupappend", {
     * }
     * </pre>
     *
     * @return the {@link String} name of the group or {@code null}.
     */
    String getName();

    /**
     * Returns the group update mode.
     *
     * <pre>
     * group "foogroupappend", update: UpdateMode.password, {
     * }
     * </pre>
     *
     * @return the update {@link UpdateMode} mode or {@code null}.
     */
    UpdateMode getUpdate();

    /**
     * Returns the group users.
     *
     * <pre>
     * group {
     *     user "foo", password: "foopass"
     *     user "bar", password: "barpass"
     * }
     * </pre>
     *
     * @return the {@link Map} of user passwords or {@code null}.
     */
    Map<String, Object> getUserPasswords();

    /**
     * Returns the group users.
     *
     * <pre>
     * group {
     *     user "foo", password: "foopass", update: UpdateMode.password
     *     user "bar", password: "barpass", update: UpdateMode.password
     * }
     * </pre>
     *
     * @return the {@link Map} of user update modes or {@code null}.
     */
    Map<String, Object> getUserUpdates();

    /**
     * Returns the require valid mode.
     *
     * <pre>
     * group {
     *     require valid: RequireValid.user
     * }
     * </pre>
     *
     * @return the {@link RequireValid} mode or {@code null}.
     */
    RequireValid getRequireValid();

    /**
     * Returns the required HTTP methods.
     *
     * <pre>
     * group {
     *     require except: "GET, OPTIONS"
     * }
     * </pre>
     *
     * @return the {@link List} of {@link String} methods or {@code null}.
     */
    List<String> getRequireExcept();

}
