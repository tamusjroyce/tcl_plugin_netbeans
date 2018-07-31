/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.callstack;

/**
 * Constants for columns description definition.
 * @author dmp
 */
public class TclCallStackModelColumns {
	
	public static String TCLCALLSTACK_COLUMN_CMDSTR_ID = "CALLSTACK_CMDSTR";
	public static String TCLCALLSTACK_COLUMN_CMDSTR_DISPLAY = "Command String";
	public static int TCLCALLSTACK_COLUMN_CMDSTR_ORDER = 1;
	
	public static String TCLCALLSTACK_COLUMN_LEVEL_ID = "CALLSTACK_LEVEL";
	public static String TCLCALLSTACK_COLUMN_LEVEL_DISPLAY = "Relative [info level]";
	public static int TCLCALLSTACK_COLUMN_LEVEL_ORDER = 3;

	public static String TCLCALLSTACK_COLUMN_FILEANDLINE_ID = "CALLSTACK_FILEANDLINE";
	public static String TCLCALLSTACK_COLUMN_FILEANDLINE_DISPLAY = "FileName:LineNumber";
	public static int TCLCALLSTACK_COLUMN_FILEANDLINE_ORDER = 2;
	
}
