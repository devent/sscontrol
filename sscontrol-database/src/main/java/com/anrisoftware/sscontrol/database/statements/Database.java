/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-database.
 * 
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.statements;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Defines the database name, character set and collate.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Database implements Serializable {

	private static final String SCRIPTS = "scripts";
	private static final String COLLATE = "collate";
	private static final String CHARACTER_SET = "character set";
	private static final String NAME = "name";

	private final DatabaseLogger log;

	private final String name;

	@Inject
	private Scripts scripts;

	@Inject
	private ScriptFactory scriptFactory;

	private String characterSet;

	private String collate;

	/**
	 * @see DatabaseFactory#create(String)
	 */
	@Inject
	Database(DatabaseLogger logger, @Assisted String name) {
		this.log = logger;
		log.checkName(this, name);
		this.name = name;
	}

	/**
	 * Sets additional arguments for the database.
	 * 
	 * @param args
	 *            the arguments {@link Map}.
	 * 
	 * @see #setCharacterSet(String)
	 * @see #setCollate(String)
	 */
	public void setArguments(Map<String, String> args) {
		if (args.containsKey("character_set")) {
			setCharacterSet(args.get("character_set"));
		}
		if (args.containsKey(COLLATE)) {
			setCollate(args.get(COLLATE));
		}
	}

	/**
	 * Database script to execute.
	 * 
	 * @see ScriptFactory#create(Map)
	 */
	public void script(Map<String, Object> args) throws URISyntaxException {
		scripts.addScript(scriptFactory.create(args));
	}

	public Scripts getScripts() {
		return scripts;
	}

	/**
	 * Returns the name of the database.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the default character set for the database.
	 * 
	 * @param set
	 *            the character set.
	 * 
	 * @throws NullPointerException
	 *             if the specified character set is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified character set is empty.
	 */
	public void setCharacterSet(String set) {
		log.checkCharacterSet(this, set);
		this.characterSet = set;
		log.characterSetSet(this, set);
	}

	/**
	 * Returns the default character set for the database.
	 */
	public String getCharacterSet() {
		return characterSet;
	}

	/**
	 * Sets the default collate for the database.
	 * 
	 * @param set
	 *            the collate.
	 * 
	 * @throws NullPointerException
	 *             if the specified collate is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified collate is empty.
	 */
	public void setCollate(String collate) {
		log.checkCollate(this, collate);
		this.collate = collate;
		log.collateSet(this, collate);
	}

	/**
	 * Returns the default collate for the database.
	 */
	public String getCollate() {
		return collate;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(NAME, name)
				.append(CHARACTER_SET, characterSet).append(COLLATE, collate)
				.append(SCRIPTS, scripts).toString();
	}

}
