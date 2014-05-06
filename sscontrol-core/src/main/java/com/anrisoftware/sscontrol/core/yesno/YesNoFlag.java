/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.yesno;

/**
 * Yes/no flag.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum YesNoFlag {

    yes(true), no(false);

    /**
     * Returns the value of the flag.
     * 
     * @param flag
     *            the {@link Object} flag.
     * 
     * @return the flag value.
     */
    public static boolean valueOf(Object flag) {
        if (flag instanceof YesNoFlag) {
            return ((YesNoFlag) flag).asBoolean();
        }
        if (flag instanceof Boolean) {
            return (Boolean) flag;
        } else {
            try {
                return YesNoFlag.valueOf(flag.toString()).asBoolean();
            } catch (IllegalArgumentException e) {
                return Boolean.valueOf(flag.toString());
            }
        }
    }

    private boolean flag;

    private YesNoFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * Returns boolean value.
     * 
     * @return {@code true} if the flag is {@link #yes}.
     */
    public boolean asBoolean() {
        return flag;
    }
}
