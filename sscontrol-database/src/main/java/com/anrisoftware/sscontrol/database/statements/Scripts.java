/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

/**
 * Script resources collection.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Scripts implements Iterable<String> {

	@Inject
	private ScriptsLogger log;

	private final List<Script> scripts;

	Scripts() {
		this.scripts = new ArrayList<Script>();
	}

	/**
	 * Adds the script resource.
	 * 
	 * @param script
	 *            the {@link Script} resource.
	 */
	public void addScript(Script script) {
		scripts.add(script);
		log.scriptAdd(script);
	}

	@Override
	public Iterator<String> iterator() {
		ErrorHandler handler = log.getScriptHandler();
		return new ImportScriptsIterator(handler);
	}

	/**
	 * Handlers errors from the import script iterator.
	 * 
	 * @author Erwin Mueller, erwin.mueller@deventm.org
	 * @since 1.0
	 */
	public static interface ErrorHandler {

		/**
		 * Called if the specified exception was thrown.
		 * 
		 * @param script
		 *            the {@link Script} resource.
		 * 
		 * @param e
		 *            the {@link Exception}.
		 */
		void errorThrown(Script script, Exception e);
	}

	private class ImportScriptsIterator implements Iterator<String> {

		int size = scripts.size();

		int index = 0;

		final ErrorHandler handler;

		boolean error;

		public ImportScriptsIterator(ErrorHandler handler) {
			this.error = false;
			this.handler = handler;
		}

		@Override
		public boolean hasNext() {
			return !error && index < size;
		}

		@Override
		public String next() {
			int i = index;
			String string = null;
			Script script = scripts.get(i++);
			try {
				string = IOUtils.toString(script.openStream());
			} catch (MalformedURLException e) {
				error = true;
				handler.errorThrown(script, e);
			} catch (IOException e) {
				error = true;
				handler.errorThrown(script, e);
			}
			index = i;
			return string;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
