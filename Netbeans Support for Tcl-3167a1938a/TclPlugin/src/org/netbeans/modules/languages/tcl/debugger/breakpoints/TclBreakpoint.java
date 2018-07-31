/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.breakpoints;

import org.netbeans.api.debugger.Breakpoint;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;

/**
 * TclBreakpoint type to handle general (and only available) line breakpoints.
 * @author dmp
 */
public class TclBreakpoint extends Breakpoint {

	private Line line;
	private FileObject file;

	public TclBreakpoint( Line l, FileObject fo ) {
		this.file=fo;
		this.line=l;
	}

	public FileObject getFile() {
		return file;
	}

	public Line getLine() {
		return line;
	}

	@Override
	public String getGroupName() {
		if( line == null || file == null ) {
			return "";
		} else {
			return file.getNameExt() + ":" + ( line.getLineNumber() + 1 );
		}
	}

	public String getDisplayName() {
		return getGroupName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void disable() {
		return;
	}

	@Override
	public void enable() {
		return;
	}

	/*
	@Override
	public String toString() {
	return  file.getNameExt() + ":" + line.getLineNumber();
	}*/
}
