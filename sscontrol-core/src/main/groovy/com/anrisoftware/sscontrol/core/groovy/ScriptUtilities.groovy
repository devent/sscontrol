/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.groovy

class ScriptUtilities {

	ScriptUtilities() {
		extendString()
	}

	private void extendString() {
		def old = String.metaClass.getMetaMethod("asType", [Class] as Class[])
		String.metaClass.asType = { Class c ->
			switch (c) {
				case { c == File }:
					new File(delegate)
					break
				case { c == URI }:
					new File(delegate).toURI()
					break
				case { c == URL }:
					new File(delegate).toURI().toURL()
					break
				default:
					old.invoke(delegate, c)
			}
		}
	}
}
