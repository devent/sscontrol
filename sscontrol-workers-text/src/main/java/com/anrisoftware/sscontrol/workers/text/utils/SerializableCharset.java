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
