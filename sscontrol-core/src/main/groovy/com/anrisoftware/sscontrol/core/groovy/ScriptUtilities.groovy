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
