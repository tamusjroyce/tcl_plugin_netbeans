/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.callstack;

import org.netbeans.spi.viewmodel.ColumnModel;

/**
 * File name and line number column for TCL CallStackView.
 * @author dmp
 */
public class TclCallStackModelColumnFileNameAndLineNumber extends ColumnModel{
	
	@Override
	public int getCurrentOrderNumber() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_FILEANDLINE_ORDER;
	}
	
	@Override
	public String getID() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_FILEANDLINE_ID;
	}

	@Override
	public String getDisplayName() {
		return TclCallStackModelColumns.TCLCALLSTACK_COLUMN_FILEANDLINE_DISPLAY;
	}

	@Override
	public Class getType() {
		return String.class;
	}
}
