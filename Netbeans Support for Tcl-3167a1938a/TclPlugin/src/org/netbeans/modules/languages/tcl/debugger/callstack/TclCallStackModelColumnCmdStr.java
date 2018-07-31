/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.callstack;

import org.netbeans.spi.viewmodel.ColumnModel;

/**
 * Command String column for TCL CallStackView.
 * @author dmp
 */
public class TclCallStackModelColumnCmdStr extends ColumnModel {

	@Override
	public int getCurrentOrderNumber() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_CMDSTR_ORDER;
	}

	@Override
	public String getID() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_CMDSTR_ID;
	}

	@Override
	public String getDisplayName() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_CMDSTR_DISPLAY;
	}

	@Override
	public Class getType() {
		return String.class;
	}
	
}
