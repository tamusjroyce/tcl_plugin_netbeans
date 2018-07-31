/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import org.netbeans.modules.languages.tcl.debugger.breakpoints.TclBreakpoint;
import org.netbeans.spi.debugger.ui.EditorContextDispatcher;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.EditorCookie.Observable;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.text.Line;

/**
 * Methods used by TclBreakpointActionProvider and TclDebuggerJavaPart classes
 * Used for Editor ←/→ JavaPartDebugger Interaction
 * @author dmp
 */
public class TclDebuggerUtils {

	/**
	 * Checks if file is Tcl Source.
	 */
	static public boolean isTclSource( FileObject fo ) {
		if( fo.getExt().toLowerCase().equals( "tcl" ) ) {
			return true;
		}
		return false;
	}

	/**
	 * Returns Current File in Editor.
	 */
	public static FileObject getCurrentFileInEditor() {

		EditorContextDispatcher editorContext=EditorContextDispatcher.getDefault();
		return editorContext.getCurrentFile();
	}

	/**
	 * Returns current Line in Editor.
	 */
	public static Line getCurrentLine() {

		EditorContextDispatcher editorContext=EditorContextDispatcher.getDefault();
		return editorContext.getCurrentLine();

	}

	/**
	 * Returns current line in Editor as a String.
	 */
	public static String getCurrentLineNumberString() {
		return Integer.toString( getCurrentLine().getLineNumber() + 1 );
	}

	/**
	 * Returns current file's path in Editor as a String.
	 */
	public static String getCurrentFilePathInEditorString() {
		return getCurrentFileInEditor().getPath();
	}

	/**
	 *  Returns TclBreakpoint in the Line of Tcl File in Editor.
	 */
	public static TclBreakpoint getTclBreakpointAtLine() {
		Line line=getCurrentLine();
		FileObject fileObject=getCurrentFileInEditor();

		TclBreakpoint breakpoint=new TclBreakpoint( line, fileObject );
		return breakpoint;
	}

	/**
	 * Opens DataObject in Editor if it's not opened yet.
	 */
	public static void openFileInEditor( DataObject dataObject ) {

		final Observable o=dataObject.getCookie( Observable.class );
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				if( o != null ) {
					o.open();
				}
			}

		} );
	}

	/**
	 * Jump to lineNumber in dataObject.
	 */
	public static void jumpToLineInFile( final DataObject dataObject, final int lineNumber ) {
		// do not attach counter just jump to file:line
		openFileInEditorAndAttachCounter( dataObject, lineNumber, false, null);
	}

	/**
	 * Jump to lineNumber in dataObject and attach programCounterAnnotation
	 */
	public static void openFileInEditorAndAttachCounter( final DataObject dataObject, final int lineNumber, final CurrentProgramCounterAnnotation programCounterAnnotation) {
		openFileInEditorAndAttachCounter( dataObject, lineNumber, true, programCounterAnnotation);
	}
	
	
	/**
	 * Opens DataObject in Editor if it's not opened yet. And jump to line number. Also set CurrentProgramCounter
	 */
	private static void openFileInEditorAndAttachCounter( final DataObject dataObject, final int lineNumber, final boolean attach, final CurrentProgramCounterAnnotation programCounterAnnotation ) {

		openFileInEditor( dataObject );

		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {

				int lineToJump=lineNumber;
				EditorCookie ec=dataObject.getCookie( EditorCookie.class );
				if( ec != null ) {
					JEditorPane[] p=ec.getOpenedPanes();
					if( p.length >= 1 ) {
						p[0].requestFocus();
						if( dataObject != null ) {
							LineCookie lc=dataObject.getCookie( LineCookie.class );
							if( lc == null ) {
								return;
							}
							int linesInFile=lc.getLineSet().getLines().size();
							if( linesInFile < lineToJump ) {
								lineToJump=linesInFile - 1;
							} else if( lineToJump < 0 ) {
								lineToJump=0;
							}
							Line line=lc.getLineSet().getOriginal( lineToJump );
							line.show( Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS );
							if( attach == true ) {
								programCounterAnnotation.attach( line );
							}
						}
					}
				}

			}

		} );
	}

}
