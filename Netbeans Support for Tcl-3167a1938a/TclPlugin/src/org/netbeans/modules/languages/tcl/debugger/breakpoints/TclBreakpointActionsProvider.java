/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.breakpoints;

import java.util.Collections;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerUtils;
import org.netbeans.spi.debugger.ActionsProviderSupport;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;

/**
 * Handle ACTION_TOGGLE_BREAKPOINT
 * Action for "Toggle Line Breakpoint" invoked by editor.
 * Bind by META-INF/debugger/org.netbeans.spi.debugger.ActionsProvider and layer.xml 
 * @author dmp
 */
public class TclBreakpointActionsProvider extends ActionsProviderSupport {

	private final static Set ACTIONS=Collections.singleton( ActionsManager.ACTION_TOGGLE_BREAKPOINT );

	public TclBreakpointActionsProvider() {
		setEnabled( ActionsManager.ACTION_TOGGLE_BREAKPOINT, true );
	}

	@Override
	public void doAction( Object o ) {

		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {

				FileObject fo=TclDebuggerUtils.getCurrentFileInEditor();
				if( !TclDebuggerUtils.isTclSource( fo ) ) {
					return;
				}

				Line line=TclDebuggerUtils.getCurrentLine();

				Breakpoint[] breakpoints=DebuggerManager.getDebuggerManager().getBreakpoints();
				int iter;
				int k=breakpoints.length;

				for( iter=0; iter < k; iter++ ) {
					if( breakpoints[iter] instanceof TclBreakpoint
					       && ( ( ( TclBreakpoint )breakpoints[iter] ).getLine() != null )
					       && ( ( TclBreakpoint )breakpoints[iter] ).getLine().equals( line ) ) {
						//remove a breakpoint
						TclBreakpoint bp=TclDebuggerUtils.getTclBreakpointAtLine();
						DebuggerManager.getDebuggerManager().removeBreakpoint( breakpoints[iter] );
						break;
					}
				}
				if( iter == k ) { //add a breakpoint
					TclBreakpoint bp=TclDebuggerUtils.getTclBreakpointAtLine();
					DebuggerManager.getDebuggerManager().addBreakpoint( bp );
				}
			}

		} );

	}

	@Override
	public Set getActions() {
		return ACTIONS;
	}

}
