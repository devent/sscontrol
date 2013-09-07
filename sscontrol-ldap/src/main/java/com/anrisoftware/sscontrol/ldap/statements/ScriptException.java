package com.anrisoftware.sscontrol.ldap.statements;

import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Exception thrown while reading the script resource.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ScriptException extends RuntimeException {

	private final Context<ScriptException> context;

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public ScriptException(String message, Throwable cause) {
		super(message, cause);
		this.context = new Context<ScriptException>(this);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public ScriptException(String message) {
		super(message);
		this.context = new Context<ScriptException>(this);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public ScriptException(Object message, Throwable cause) {
		super(message.toString(), cause);
		this.context = new Context<ScriptException>(this);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public ScriptException(Object message) {
		super(message.toString());
		this.context = new Context<ScriptException>(this);
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public ScriptException add(String name, Object value) {
		context.addContext(name, value);
		return this;
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public ScriptException add(Object name, Object value) {
		context.addContext(name.toString(), value);
		return this;
	}

	/**
	 * @see Context#getContext()
	 */
	public Map<String, Object> getContext() {
		return context.getContext();
	}

	@Override
	public String toString() {
		return context.toString();
	}
}
