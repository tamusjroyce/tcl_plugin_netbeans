/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import org.netbeans.modules.languages.tcl.project.TclProject;

/**
 * Exchange opened project between debugger classes.
 * @author dmp
 */
public abstract class ProjectExchangeService {

	private TclProject project;
	public void setProject( TclProject project ) {
		this.project=project;
	}
	abstract public TclProject getProject();
	
}
