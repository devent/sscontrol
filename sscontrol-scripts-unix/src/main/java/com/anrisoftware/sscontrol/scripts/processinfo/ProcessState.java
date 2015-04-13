/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.scripts.processinfo;

/**
 * Process state.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum ProcessState {

    /**
     * Uninterruptible sleep (usually IO).
     */
    UNINT_SLEEP,

    /**
     * Running or runnable (on run queue).
     */
    RUNNING,

    /**
     * Interruptible sleep (waiting for an event to complete).
     */
    INT_SLEEP,

    /**
     * Stopped, either by a job control signal or because it is being traced.
     */
    STOPPED,

    /**
     * Dead (should never be seen).
     */
    DEAD,

    /**
     * Defunct ("zombie") process, terminated but not reaped by its parent.
     */
    DEFUNCT,
}
