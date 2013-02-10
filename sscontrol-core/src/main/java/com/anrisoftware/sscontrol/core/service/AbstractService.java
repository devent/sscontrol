package com.anrisoftware.sscontrol.core.service;

import groovy.lang.Script;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.Injector;

public abstract class AbstractService implements Service {

	/**
	 *
	 */
	private static final long serialVersionUID = -8580699865227701438L;

	private Injector injector;

	private ProfileService profile;

	private AbstractServiceLogger log;

	protected AbstractService() {
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
		Script script = getScript(profileName);
		script.setProperty("system", profile.getEntry("system"));
		script.setProperty("profile", profile.getEntry(getName()));
		script.setProperty("service", this);
		script.setProperty("name", getName());
		injectScript(script);
		script.run();
		return this;
	}

	/**
	 * Injects the dependencies of the script.
	 * 
	 * @param script
	 *            the {@link Script}.
	 */
	protected void injectScript(Script script) {
		injector.injectMembers(script);
	}

	/**
	 * Returns the script to the specified profile.
	 * 
	 * @param profileName
	 *            the name of the profile.
	 * 
	 * @return the {@link Script}.
	 * 
	 * @throws ServiceException
	 *             if there were some error returning the script.
	 */
	protected abstract Script getScript(String profileName)
			throws ServiceException;

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("profile",
				profile.getProfileName()).toString();
	}

}
