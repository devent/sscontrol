/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.gitit.AuthMethod
import com.anrisoftware.sscontrol.httpd.gitit.LoginRequired
import com.anrisoftware.sscontrol.httpd.gitit.RepositoryType

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "gitit", id: "gititid", alias: "/", type: RepositoryType.git, prefix: "gitit", {
            bind address: "127.0.0.1", port: 9999
            debug level: 2
            override mode: update
            wiki title: "Wiki Foo"
            login required: LoginRequired.modify
            auth method: AuthMethod.form
            page type: "Markdown"
            math "MathML"
            frontpage "Front Page"
            nodelete "Front Page, Help"
            noedit "Help"
            defaultsummary "Default"
            tableofcontents yes
            caching enabled: yes
            idle gc: yes
            memory upload: "100 kB", page: "100 kB"
            compress responses: yes
            recaptcha enabled: yes, privatekey: "private.key", publickey: "public.key"
            access question: "Foo?", answer: "Bar"
            feeds enabled: yes, duration: "P5D", refresh: "PT10M"
        }
    }
    domain "www.test1.com", address: "192.168.0.51", {
        setup "gitit", ref: "gititid", refdomain: "testid"
    }
    domain "test2.com", address: "192.168.0.52", {
        setup "gitit", id: "gititid", alias: "/", type: RepositoryType.git, prefix: "gitit", { //.
            wiki title: "Wiki Foo" //.
        }
    }
}
