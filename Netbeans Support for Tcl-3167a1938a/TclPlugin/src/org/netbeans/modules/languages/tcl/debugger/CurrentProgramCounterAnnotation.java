/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import org.openide.text.Annotation;

/**
 * Current Program Counter Annotation Class.
 * Default it's a green highlight for actual executed line.
 * @author dmp
 */
public class CurrentProgramCounterAnnotation extends Annotation {

	@Override
	public String getAnnotationType() {
		return "CurrentPC";
	}

	@Override
	public String getShortDescription() {
		return "Current Program Counter";
	}

}
