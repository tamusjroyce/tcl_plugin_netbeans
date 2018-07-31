/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.callstack;

import org.netbeans.spi.viewmodel.ColumnModel;

/**
 * Level column for TCL CallStackView.
 * @author dmp
 */
public class TclCallStackModelColumnLevel extends ColumnModel {
	
	@Override
	public int getCurrentOrderNumber() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_LEVEL_ORDER;
	}
	
	@Override
	public String getID() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_LEVEL_ID;
	}

	@Override
	public String getDisplayName() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_LEVEL_DISPLAY;
	}

	@Override
	public Class getType() {
		return Integer.class;
	}
}
