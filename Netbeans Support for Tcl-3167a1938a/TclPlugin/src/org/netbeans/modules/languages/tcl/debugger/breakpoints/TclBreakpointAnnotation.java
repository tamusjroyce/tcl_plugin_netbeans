/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.breakpoints;

import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.spi.debugger.ui.BreakpointAnnotation;
import org.openide.text.Annotatable;

/**
 * Breakpoint Annotation implementation.
 * @author dmp
 */
public class TclBreakpointAnnotation extends BreakpointAnnotation {

	private final TclBreakpoint breakpoint;

	public TclBreakpointAnnotation( final Annotatable a, final TclBreakpoint b ) {
		breakpoint=b;
		if( a != null ) {
			attach( a );
		}
	}

	@Override
	public Breakpoint getBreakpoint() {
		return breakpoint;
	}

	@Override
	public String getAnnotationType() {
		return "Breakpoint";
	}

	/*
	 * ShortDescription displays when mouse stays on red debug squere
	 */
	@Override
	public String getShortDescription() {
		return "Breakpoint";
	}

}
