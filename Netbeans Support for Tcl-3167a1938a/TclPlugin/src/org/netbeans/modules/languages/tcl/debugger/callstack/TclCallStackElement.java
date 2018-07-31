/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.callstack;

import java.io.File;

/**
 * Element of CallStackView.
 * @author dmp
 */
public class TclCallStackElement {
	private int id;
	private int lineNumber;
	private int infoFrame;
	private String cmdStr;
	private String fileName;

	public int getId() {
		return id;
	}

	public void setId( int id ) {
		this.id=id;
	}

	public String getCmdStr() {
		return cmdStr.replace( "\\n", "\n");
	}

	public void setCmdStr( String cmdStr ) {
		this.cmdStr=cmdStr;
	}

	public String getFileName() {
		return fileName;
	}

	public File getFile() {
		return new File(fileName);
	}
	
	public void setFileName( String fileName ) {
		this.fileName=fileName;
	}

	public int getInfoFrame() {
		return infoFrame;
	}

	public void setInfoFrame( int infoFrame ) {
		this.infoFrame=infoFrame;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber( int lineNumber ) {
		this.lineNumber=lineNumber;
	}

	@Override
	public String toString() {
		return "TclCallStackElement{" + "lineNumber=" + lineNumber + ", infoFrame=" + infoFrame + ", cmdStr=" + cmdStr + ", fileName=" + fileName + '}';
	}

	
}
