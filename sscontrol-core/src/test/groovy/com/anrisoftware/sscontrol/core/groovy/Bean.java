package com.anrisoftware.sscontrol.core.groovy;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * @see StatementsMapTest
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Bean {

    final StatementsMap map;

    @Inject
    Bean(StatementsMapFactory factory) {
        this.map = factory.create(this, "bean");
    }

    Object methodMissing(String name, Object args) throws ServiceException {
        map.methodMissing(name, args);
        return null;
    }
}
