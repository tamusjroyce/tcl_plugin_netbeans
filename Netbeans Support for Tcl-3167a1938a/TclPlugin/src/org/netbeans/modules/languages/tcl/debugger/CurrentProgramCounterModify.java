/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import java.io.File;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 * Actions for Program Counter Annotation. Operated by TclDebuggerJavaPart class.
 * @author dmp
 */
public class CurrentProgramCounterModify {

	private CurrentProgramCounterAnnotation programCounterAnnotation;
	private final TclDebuggerJavaPart tclDebuggerJavaPart;

	public CurrentProgramCounterModify( TclDebuggerJavaPart tclDebuggerJavaPart ) {
		this.tclDebuggerJavaPart=tclDebuggerJavaPart;
		programCounterAnnotation=new CurrentProgramCounterAnnotation();
	}

	public void modify() {

		File runFile=tclDebuggerJavaPart.getCurrentFile();
		int lineNumber=tclDebuggerJavaPart.getCurrentLineNumber();
		lineNumber--;

		DataObject dataObject=null;
		try {
			dataObject=DataObject.find( FileUtil.toFileObject( runFile ) );
			if( dataObject == null ) {
				return;
			}
			TclDebuggerUtils.openFileInEditorAndAttachCounter( dataObject, lineNumber, this.programCounterAnnotation);

		} catch( Exception e ) {
			return;
		}
	}

	public void detach() {
		programCounterAnnotation.detach();
	}

}
