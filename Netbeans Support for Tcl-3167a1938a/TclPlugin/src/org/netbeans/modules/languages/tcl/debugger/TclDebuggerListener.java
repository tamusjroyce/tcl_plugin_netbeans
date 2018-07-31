/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManagerAdapter;
import org.netbeans.api.debugger.Session;
import org.netbeans.modules.languages.tcl.debugger.breakpoints.TclBreakpointsModel;

/**
 * DebuggerManagerAdepter is an empty implementation of DebuggerManagerListener.
 * Registered by META-INF/debugger/org.netbeans.api.debugger.LazyDebuggerManagerListener 
 * Monitor some debugger events using override methods.
 * @author dmp
 */
public class TclDebuggerListener extends DebuggerManagerAdapter {

	private TclBreakpointsModel tclBreakpointsModel = null;

	@Override
	public void sessionAdded( Session session ) {
		tclBreakpointsModel = session.lookupFirst( null, TclBreakpointsModel.class );
		super.sessionAdded( session );
	}

	@Override
	public void breakpointAdded( Breakpoint breakpoint ) {
		if( tclBreakpointsModel != null ) {
			tclBreakpointsModel.updateModel();
		}
	}

	@Override
	public void breakpointRemoved( Breakpoint breakpoint ) {
		if( tclBreakpointsModel != null ) {
			tclBreakpointsModel.updateModel();
		}
	}

	

}
