package com.anrisoftware.sscontrol.core.service;

import static com.google.inject.Guice.createInjector;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Creates the script from Guice modules.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractScriptFactory implements ServiceScriptFactory {

	private static AbstractScriptFactoryLogger log = new AbstractScriptFactoryLogger();

	private Injector parent;

	@Override
	public Object getScript() throws ServiceException {
		try {
			return getInjector(parent).getInstance(getScriptClass());
		} catch (Exception e) {
			throw log.errorCreateScript(this, e);
		}
	}

	private Injector getInjector(Injector parent) {
		return parent == null ? createInjector(getModules()) : parent
				.createChildInjector(getModules());
	}

	/**
	 * Returns the needed Guice modules to create a script.
	 * <p>
	 * <h2>Example</h2>
	 * <p>
	 * 
	 * <pre>
	 * &#064;Override
	 * protected Iterable&lt;? extends Module&gt; getModules() {
	 * 	return Arrays.asList(new Module[] { new PostfixModule() });
	 * }
	 * </pre>
	 * 
	 * @return an {@link Iterable} with the {@link Module} modules.
	 */
	protected abstract Iterable<? extends Module> getModules();

	/**
	 * Returns the class of the script to create.
	 * 
	 * @return the {@link Class} of the script.
	 */
	protected abstract Class<?> getScriptClass();

	@Override
	public void setParent(Object parent) {
		this.parent = (Injector) parent;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getInfo()).toString();
	}
}
