/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-text.
 *
 * sscontrol-workers-text is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-text is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-text. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.tokentemplate;

import java.util.regex.Matcher;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.workers.api.Worker;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.google.inject.assistedinject.Assisted;

/**
 * Replace search text with a replacement. The replacement will be surrounded by
 * a begin and end token. If the search text can not be found the replacement
 * will be appended.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class TokensTemplateWorker implements Worker {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = 3844878214916165038L;

	private final TokensTemplateWorkerLogger log;

	private final String beginToken;

	private final String endToken;

	private final TokenTemplate template;

	private final String text;

	private transient String replacement;

	            /**
     * Sets the begin and end tokens, the template and the argument.
     * 
     * @param tokenMarker
     *            the {@link TokenMarker} holding the begin and end tokens.
     * 
     * @param template
     *            the {@link TokenTemplate} containing the search text and the
     *            replacement.
     * 
     * @param text
     *            the text in which to replace.
     */
	@Inject
	TokensTemplateWorker(TokensTemplateWorkerLogger logger,
			@Assisted TokenMarker tokenMarker,
			@Assisted TokenTemplate template, @Assisted String text) {
		this.log = logger;
		this.beginToken = tokenMarker.getBeginToken();
		this.endToken = tokenMarker.getEndToken();
		this.template = template;
		this.text = text;
	}

	@Override
	public Worker call() throws WorkerException {
		Matcher matcher;
		matcher = template.toPattern(beginToken, endToken).matcher(text);
		boolean find = matcher.find();
		String replace = template.toReplace(beginToken, endToken);
		if (find) {
            replace(matcher, replace);
		} else if (template.isAppend()) {
            appendReplace(replace);
		} else {
			replacement = text;
		}
		return this;
	}

    private void appendReplace(String replace) {
        replacement = text + replace;
        log.appendArgument(this);
    }

    private void replace(Matcher matcher, String replace) {
        if (template.isEscape()) {
            replace = escapeReplace(replace);
        }
        replacement = matcher.replaceFirst(replace);
        log.replacedArgument(this);
    }

    private String escapeReplace(String replace) {
        return Matcher.quoteReplacement(replace);
    }

    /**
     * Returns the formatted text.
     * 
     * @return the text.
     */
	public String getText() {
		return replacement;
	}

	public TokenTemplate getTemplate() {
		return template;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(template)
				.append("begin token", beginToken)
				.append("end token", endToken).toString();
	}
}
