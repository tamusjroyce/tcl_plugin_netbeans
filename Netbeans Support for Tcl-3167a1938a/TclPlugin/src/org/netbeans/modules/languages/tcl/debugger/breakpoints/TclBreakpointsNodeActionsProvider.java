/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.breakpoints;

import javax.swing.Action;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerUtils;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.Models.ActionPerformer;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Line;
import org.openide.util.Exceptions;

/**
 * Actions for TclBreakpoint in BreakpointsView.
 * @author dmp
 */
public class TclBreakpointsNodeActionsProvider implements NodeActionsProvider {

	private static Action ACTION_DELETE_BREAKPOINT;
	private static Action ACTION_JUMP_TO_BREAKPOINT;

	static{
		String id="Delete";
		ActionPerformer actionPerformer=new ActionPerformer() {

			@Override
			public void perform( Object[] nodes ) {
				// Must be only one because of Models.MULTISELECTION_TYPE_EXACTLY_ONE
				if( nodes[0] instanceof TclBreakpoint ) {
					TclBreakpoint bpoint=( TclBreakpoint )nodes[0];
					DebuggerManager.getDebuggerManager().removeBreakpoint( bpoint );
				}
			}

			@Override
			public boolean isEnabled( Object node ) {
				return true;
			}

		};
		ACTION_DELETE_BREAKPOINT=Models.createAction( id, actionPerformer, Models.MULTISELECTION_TYPE_EXACTLY_ONE );
	}

	static{
		String id="Jump To";
		ActionPerformer actionPerformer=new ActionPerformer() {

			@Override
			public void perform( Object[] nodes ) {
				jumpToBreakpoint( nodes[0]);
			}

			@Override
			public boolean isEnabled( Object node ) {
				return true;
			}

		};
		ACTION_JUMP_TO_BREAKPOINT=Models.createAction( id, actionPerformer, Models.MULTISELECTION_TYPE_EXACTLY_ONE );
	}


	private static void jumpToBreakpoint( Object o ) {
		if( o instanceof TclBreakpoint ) {

			TclBreakpoint bpoint=( TclBreakpoint )o;

			Line line=bpoint.getLine();
			FileObject file=bpoint.getFile();
			DataObject dataObject;
			try {
				dataObject=DataObject.find( file );
				if( dataObject == null ) {
					return;
				}
				TclDebuggerUtils.jumpToLineInFile( dataObject, line.getLineNumber() );
			} catch( DataObjectNotFoundException ex ) {
				Exceptions.printStackTrace( ex );
			}
		}
	}

	@Override
	public void performDefaultAction( Object o ) throws UnknownTypeException {
		jumpToBreakpoint(o);
	}

	@Override
	public Action[] getActions( Object o ) throws UnknownTypeException {
		Action[] actions=new Action[ 2 ];
		if( o instanceof TclBreakpoint ) {
			actions[0]=ACTION_DELETE_BREAKPOINT;
			actions[1]=ACTION_JUMP_TO_BREAKPOINT;
		}
		return actions;
	}

}