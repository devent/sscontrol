/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.statements;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Domain components.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DomainComponent implements List<String> {

	private List<String> components;

	DomainComponent() {
		this.components = new ArrayList<String>();
	}

	public void setDomain(String domain) {
		this.components = asList(split(domain, '.'));
	}

	@Override
	public int size() {
		return components.size();
	}

	@Override
	public boolean isEmpty() {
		return components.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return components.contains(o);
	}

	@Override
	public Iterator<String> iterator() {
		return components.iterator();
	}

	@Override
	public Object[] toArray() {
		return components.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return components.toArray(a);
	}

	@Override
	public boolean add(String e) {
		return components.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return components.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return components.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		return components.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends String> c) {
		return components.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return components.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return components.retainAll(c);
	}

	@Override
	public void clear() {
		components.clear();
	}

	@Override
	public boolean equals(Object o) {
		return components.equals(o);
	}

	@Override
	public int hashCode() {
		return components.hashCode();
	}

	@Override
	public String get(int index) {
		return components.get(index);
	}

	@Override
	public String set(int index, String element) {
		return components.set(index, element);
	}

	@Override
	public void add(int index, String element) {
		components.add(index, element);
	}

	@Override
	public String remove(int index) {
		return components.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return components.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return components.lastIndexOf(o);
	}

	@Override
	public ListIterator<String> listIterator() {
		return components.listIterator();
	}

	@Override
	public ListIterator<String> listIterator(int index) {
		return components.listIterator(index);
	}

	@Override
	public List<String> subList(int fromIndex, int toIndex) {
		return components.subList(fromIndex, toIndex);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(components.toString())
				.toString();
	}
}
