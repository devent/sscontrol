package com.anrisoftware.sscontrol.workers.text.tokentemplate.worker;

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
			replacement = matcher.replaceFirst(replace);
			log.replacedArgument(this);
		} else {
			replacement = text + replace;
			log.appendArgument(this);
		}
		return this;
	}

	/**
	 * Returns the formatted text.
	 * 
	 * @return the text.
	 */
	public String getText() {
		return replacement;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(template)
				.append("begin token", beginToken)
				.append("end token", endToken).toString();
	}
}
