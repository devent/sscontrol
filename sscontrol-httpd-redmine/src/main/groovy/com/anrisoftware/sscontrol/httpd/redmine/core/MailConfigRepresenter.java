/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.core;

import groovy.lang.GString;

import org.codehaus.groovy.runtime.GStringImpl;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import com.anrisoftware.sscontrol.httpd.redmine.AuthenticationMethod;
import com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod;

/**
 * <i>YAML</i> mail configuration representer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class MailConfigRepresenter extends Representer {

    MailConfigRepresenter() {
        representers.put(DeliveryMethod.class, new Represent() {

            @Override
            public Node representData(Object data) {
                return representDeliveryMethod((DeliveryMethod) data);
            }

        });
        representers.put(AuthenticationMethod.class, new Represent() {

            @Override
            public Node representData(Object data) {
                return representAuthenticationMethod((AuthenticationMethod) data);
            }

        });
        representers.put(GString.class, new Represent() {

            @Override
            public Node representData(Object data) {
                return representGString((GString) data);
            }

        });
        representers.put(GStringImpl.class, new Represent() {

            @Override
            public Node representData(Object data) {
                return representGString((GString) data);
            }

        });
    }

    private Node representGString(GString data) {
        return representData(data.toString());
    }

    private Node representDeliveryMethod(DeliveryMethod data) {
        switch (data) {
        case sendmail:
            return representData(":sendmail");
        case async_sendmail:
            return representData(":async_sendmail");
        case smtp:
            return representData(":smtp");
        case async_smtp:
            return representData(":async_smtp");
        default:
            return null;
        }
    }

    private Node representAuthenticationMethod(AuthenticationMethod data) {
        switch (data) {
        case none:
            return representData("nil");
        case login:
            return representData(":login");
        case plain:
            return representData(":plain");
        case cram_md5:
            return representData(":cram_md5");
        default:
            return null;
        }
    }

}
