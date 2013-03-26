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
package com.anrisoftware.sscontrol.workers.text.utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.charset.Charset;

/**
 * Serializable wrapper for a character set.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SerializableCharset implements Externalizable, Comparable<Charset> {

	private Charset charset;

	/**
	 * @deprecated for serialization.
	 */
	@Deprecated
	public SerializableCharset() {
	}

	/**
	 * Sets the character set.
	 * 
	 * @param charset
	 *            the {@link Charset}.
	 */
	public SerializableCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Returns the character set of this wrapper.
	 * 
	 * @return the {@link Charset}.
	 */
	public Charset getCharset() {
		return charset;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(charset.name());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		String charsetName = in.readUTF();
		charset = Charset.forName(charsetName);
	}

	@Override
	public String toString() {
		return charset.toString();
	}

	@Override
	public int hashCode() {
		return charset.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return charset.equals(obj);
	}

	@Override
	public int compareTo(Charset o) {
		return charset.compareTo(o);
	}
}
