/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import org.netbeans.api.debugger.DebuggerEngine.Destructor;
import org.netbeans.spi.debugger.DebuggerEngineProvider;

/**
 * Registered by META-INF/debugger/TclSession/org.netbeans.spi.debugger.DebuggerEngineProvider
 * @author dmp
 */
public class TclDebuggerEngineProvider extends DebuggerEngineProvider {

	private Destructor destructor;
	
	@Override
	public String[] getLanguages() {
		return new String[] {"Tcl"};
	}

	@Override
	public String getEngineTypeID() {
		return "TclDebuggerEngine";
	}

	@Override
	public Object[] getServices() {
		return new Object[]{};
	}

	@Override
	public void setDestructor( Destructor d ) {
		this.destructor = d;
	}

	public Destructor getDestructor() {
		return this.destructor;
	}
	
	
}
