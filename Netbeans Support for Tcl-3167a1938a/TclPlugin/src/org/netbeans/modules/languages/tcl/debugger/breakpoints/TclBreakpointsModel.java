/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.breakpoints;

import java.util.ArrayList;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;

/**
 * Implementation of Breakpoints View.
 * @author dmp
 */
public class TclBreakpointsModel implements NodeModel, TreeModel {

	private String iconBaseString="org/netbeans/modules/debugger/resources/breakpointsView/Breakpoint";
	private ArrayList<ModelListener> listeners=new ArrayList<ModelListener>();

	@Override
	public String getDisplayName( Object o ) throws UnknownTypeException {
		if( o instanceof TclBreakpoint ) {
			TclBreakpoint bpoint=( TclBreakpoint )o;
			return bpoint.getDisplayName();
		}
		return "";
	}

	@Override
	public String getIconBase( Object o ) throws UnknownTypeException {
		return iconBaseString;
	}

	@Override
	public String getShortDescription( Object o ) throws UnknownTypeException {
		return "Tcl Breakpoint";
	}

	@Override
	public void addModelListener( ModelListener ml ) {
		this.listeners.add( ml );
	}

	@Override
	public void removeModelListener( ModelListener ml ) {
		this.listeners.remove( ml );
	}

	@Override
	public Object getRoot() {
		return ROOT;
	}

	@Override
	public Object[] getChildren( Object o, int i, int i1 ) throws UnknownTypeException {
		ArrayList<Object> kids=new ArrayList<Object>();

		if( o == ROOT ) {
			Breakpoint[] bpoints=DebuggerManager.getDebuggerManager().getBreakpoints();
			int incr;
			int size=bpoints.length;
			for( incr=0; incr < size; incr++ ) {
				if( bpoints[incr] instanceof TclBreakpoint ) {
					kids.add( bpoints[incr] );
				}
			}
		}

		return kids.toArray();
	}

	@Override
	public boolean isLeaf( Object o ) throws UnknownTypeException {
		if( o == ROOT ) {
			return false;
		}
		return true;
	}

	@Override
	public int getChildrenCount( Object o ) throws UnknownTypeException {
		return 1;
	}

	public void updateModel() {
		ModelListener ml;
		int size=listeners.size();
		for( int incr=0; incr < size; incr++ ) {
			ml=listeners.get( incr );
			ml.modelChanged( null );
		}
	}

}
