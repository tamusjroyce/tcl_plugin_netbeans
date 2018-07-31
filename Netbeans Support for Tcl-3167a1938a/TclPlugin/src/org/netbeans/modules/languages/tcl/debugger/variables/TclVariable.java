/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.variables;

/**
 * Tcl Variable - for Variables View.
 * @author dmp
 */
public class TclVariable {

	private String name;
	private String value;
	private String type;

	public TclVariable( String name, String type, String value ) {
		this.name=name;
		this.type=type;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

}
