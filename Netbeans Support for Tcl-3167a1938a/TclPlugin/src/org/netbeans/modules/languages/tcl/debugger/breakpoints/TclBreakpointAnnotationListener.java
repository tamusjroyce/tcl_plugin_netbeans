/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.breakpoints;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.DebuggerManagerAdapter;
import org.netbeans.spi.debugger.ui.BreakpointAnnotation;

/**
 * Bridge between Annotations and Breakpoints
 * Registered by META-INF/debugger/org.netbeans.api.debugger.LazyDebuggerManagerListener 
 * @author dmp
 */
public class TclBreakpointAnnotationListener extends DebuggerManagerAdapter implements PropertyChangeListener {

	private HashMap<Breakpoint, TclBreakpointAnnotation> breakpointToAnnotation=new HashMap<Breakpoint, TclBreakpointAnnotation>();

	@Override
	public String[] getProperties() {
		return new String[]{ DebuggerManager.PROP_BREAKPOINTS };
	}

	@Override
	public void breakpointAdded( Breakpoint b ) {
		if( !( b instanceof TclBreakpoint ) ) {
			return;
		} else {
			addAnnotation( ( TclBreakpoint )b );
		}
	}

	@Override
	public void breakpointRemoved( Breakpoint b ) {
		if( !( b instanceof TclBreakpoint ) ) {
			return;
		} else {
			removeAnnotation( b );
		}
	}

	@Override
	public void propertyChange( PropertyChangeEvent evt ) {
		if( !evt.getPropertyName().equals( Breakpoint.PROP_ENABLED ) ) {
			return;
		} else {
			removeAnnotation( ( Breakpoint )evt.getSource() );
			addAnnotation( ( TclBreakpoint )evt.getSource() );
		}
	}

	private void addAnnotation( TclBreakpoint b ) {
		if( b != null ) {
			breakpointToAnnotation.put( b, new TclBreakpointAnnotation( b.getLine(), b ) );
			b.addPropertyChangeListener(
			       Breakpoint.PROP_ENABLED,
			       this );
		}
	}

	private void removeAnnotation( Breakpoint b ) {
		BreakpointAnnotation annotation=breakpointToAnnotation.remove( b );
		if( annotation == null ) {
			return;
		} else {
			annotation.detach();
			b.removePropertyChangeListener(
			       Breakpoint.PROP_ENABLED,
			       this );
		}
	}

}
