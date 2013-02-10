package com.anrisoftware.sscontrol.dhclient.statements;

import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;

public class RequestDeclarations implements Serializable, Iterable<Declaration> {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 4961344687617636866L;

	private final List<Declaration> requests;

	private final DeclarationFactory declarationFactory;

	private final RequestDeclarationsLogger log;

	@Inject
	RequestDeclarations(RequestDeclarationsLogger logger,
			DeclarationFactory declarationFactory,
			@Named("dhclient-defaults-properties") ContextProperties p) {
		this.log = logger;
		this.declarationFactory = declarationFactory;
		this.requests = new ArrayList<Declaration>();
		addDefaultRequests(p);
	}

	private void addDefaultRequests(ContextProperties p) {
		for (String req : p.getListProperty("default_requests")) {
			requests.add(declarationFactory.create(req));
		}
	}

	/**
	 * Adds new request.
	 * 
	 * @param decl
	 *            the request declaration.
	 */
	public void add(String decl) {
		if (decl.startsWith("!")) {
			remove(decl.substring(1));
		} else {
			Declaration declaration = declarationFactory.create(decl);
			log.reguestAdded(this, declaration);
		}
	}

	/**
	 * Removes the specified request name.
	 * 
	 * @param decl
	 *            the request declaration.
	 */
	public void remove(String decl) {
		Declaration req = declarationFactory.create(decl);
		if (requests.remove(req)) {
			log.reguestRemoved(this, req);
		} else {
			log.noRequestFoundForRemoval(this, req);
		}
	}

	@Override
	public Iterator<Declaration> iterator() {
		return unmodifiableList(requests).iterator();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(requests).toString();
	}
}
