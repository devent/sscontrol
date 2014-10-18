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
package com.anrisoftware.sscontrol.scripts.processinfo;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * Parses the process states.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 * @see ProcessState
 */
@SuppressWarnings("serial")
public class ProcessStateFormat extends Format {

    @Inject
    private ProcessStateFormatLogger log;

    /**
     * Formats the specified process states.
     *
     * @param obj
     *            the {@link Set} of the {@link ProcessState}.
     */
    @SuppressWarnings("unchecked")
    @Override
    public StringBuffer format(Object obj, StringBuffer buff, FieldPosition pos) {
        if (obj instanceof Set) {
            formatStates(buff, (Set<ProcessState>) obj);
        }
        return buff;
    }

    private void formatStates(StringBuffer buff, Set<ProcessState> states) {
        for (ProcessState state : states) {
            switch (state) {
            case UNINT_SLEEP:
                buff.append("D");
                break;
            case RUNNING:
                buff.append("R");
                break;
            case INT_SLEEP:
                buff.append("S");
                break;
            case STOPPED:
                buff.append("T");
                break;
            case DEAD:
                buff.append("X");
                break;
            case DEFUNCT:
                buff.append("Z");
                break;
            }
        }
    }

    /**
     * Parses the specified string to process states.
     * <p>
     * <h2>Format</h2>
     * <p>
     * <ul>
     * <li>{@code "[DRSTXZ]"}
     * </ul>
     *
     * @return the parsed {@link Set} of {@link ProcessState} states.
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        try {
            return parse(source, pos);
        } catch (ParseException e) {
            pos.setErrorIndex(pos.getIndex() + e.getErrorOffset());
            return null;
        }
    }

    /**
     * @see #parse(String, ParsePosition)
     */
    public Set<ProcessState> parse(String source) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Set<ProcessState> result = parse(source, pos);
        if (pos.getIndex() == 0) {
            throw log.errorParse(source, pos);
        }
        return result;
    }

    /**
     * @see #parseObject(String)
     *
     * @param pos
     *            the index {@link ParsePosition} position from where to start
     *            parsing.
     *
     * @throws ParseException
     *             if the string is not in the correct format.
     */
    public Set<ProcessState> parse(String source, ParsePosition pos)
            throws ParseException {
        Set<ProcessState> states = new HashSet<ProcessState>();
        source = source.trim();
        for (char c : source.toCharArray()) {
            switch (c) {
            case 'D':
                states.add(ProcessState.UNINT_SLEEP);
                break;
            case 'R':
                states.add(ProcessState.RUNNING);
                break;
            case 'S':
                states.add(ProcessState.INT_SLEEP);
                break;
            case 'T':
                states.add(ProcessState.STOPPED);
                break;
            case 'X':
                states.add(ProcessState.DEAD);
                break;
            case 'Z':
                states.add(ProcessState.DEFUNCT);
                break;
            }
        }
        pos.setErrorIndex(-1);
        pos.setIndex(pos.getIndex() + source.length());
        return states;
    }

}
