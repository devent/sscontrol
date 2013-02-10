package com.anrisoftware.sscontrol.core.service;

import groovy.lang.Script;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.Injector;
import com.google.inject.Provider;

public abstract class AbstractService implements Service {

	/**
	 *
	 */
	private static final long serialVersionUID = -8580699865227701438L;

	private Injector injector;

	private final Map<String, Provider<Script>> scripts;

	private ProfileService profile;

	private AbstractServiceLogger log;

	protected AbstractService(Map<String, Provider<Script>> scripts) {
		this.scripts = scripts;
	}

	@Inject
	void setAbstractServiceLogger(AbstractServiceLogger logger) {
		this.log = logger;
	}

	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	/**
	 * Sets the profile for the service.
	 * 
	 * @param newProfile
	 *            the {@link ProfileService}.
	 */
	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	/**
	 * Returns the profile of the service.
	 * 
	 * @return the {@link ProfileService}.
	 */
	public ProfileService getProfile() {
		return profile;
	}

	@Override
	public Service call() throws ServiceException {
		String profileName = profile.getProfileName();
		Script script = scripts.get(profileName).get();
		script.setProperty("system", profile.getEntry("system"));
		script.setProperty("profile", profile.getEntry(getName()));
		script.setProperty("service", this);
		script.setProperty("name", getName());
		injector.injectMembers(script);
		script.run();
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("profile",
				profile.getProfileName()).toString();
	}

}
