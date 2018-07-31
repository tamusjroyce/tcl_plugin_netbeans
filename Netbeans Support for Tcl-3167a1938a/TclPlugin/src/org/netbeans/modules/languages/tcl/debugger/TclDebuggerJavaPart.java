/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.Session;
import org.netbeans.modules.languages.tcl.debugger.breakpoints.TclBreakpoint;
import org.netbeans.modules.languages.tcl.debugger.callstack.TclCallStackElement;
import org.netbeans.modules.languages.tcl.optionpanel.TclPanel;
import org.netbeans.modules.languages.tcl.project.TclProject;
import org.netbeans.modules.languages.tcl.run.RunTclsh;
import org.netbeans.spi.viewmodel.ModelListener;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 * TclDebugger Java Part. Class where Java Part of Debugger communicates with debugServer.tcl.
 * @author dmp
 */
public class TclDebuggerJavaPart {

	private TclProject project=null;
	private CurrentProgramCounterModify programCounter;
	private File currentFile;
	private int currentLineNumber;
	ClientToTclPart tclPart=null;
	private TclDebuggerEngineProvider engineProvider;
	private File tclPartFile=null;
	private final String tclPartFileName="debugServer.tcl";
	//
	// preferences
	private int tclPartPortNumber;
	private String tclPartVerbose;
	//
	private String variablesStringLocals="";
	private String variablesStringGlobals="";
	//
	// listeners
	private ModelListener variablesModelListener;
	private ModelListener watchesModelListener;
	private ModelListener callStackModelListener;
	//
	private ArrayList<TclCallStackElement> callStackElements;
	private boolean lockAction;
	//
	private RunTclsh debugServerProcessAndWindow;

	public TclDebuggerJavaPart( TclDebuggerEngineProvider engineProvider ) {

		bindProject();

		// Load Preferences
		tclPartPortNumber=Integer.parseInt( NbPreferences.forModule( TclPanel.class ).get( TclPanel.preferenceTclPartPortNumber, TclPanel.getTclPartDefaultPortNumber() ) );
		tclPartVerbose=NbPreferences.forModule( TclPanel.class ).get( TclPanel.preferenceTclPartVerbose, TclPanel.getTclPartDefaultVerbose() );

		this.engineProvider=engineProvider;

		programCounter=new CurrentProgramCounterModify( this );
		//programCounter.modify();

		callStackElements=new ArrayList<TclCallStackElement>();

		inspiritDebugScriptFromResources();
		runTclPart();

	}

	public void addVariablesModelListener( ModelListener ml ) {
		this.variablesModelListener=ml;
	}

	public void addWatchesModelListener( ModelListener ml ) {
		this.watchesModelListener=ml;
	}

	public void addCallStackModelListener( ModelListener ml ) {
		this.callStackModelListener=ml;
	}

	private void bindProject() {

		DebuggerManager manager=DebuggerManager.getDebuggerManager();
		Session currentSession=manager.getCurrentSession();
		ProjectExchangeService service=currentSession.lookupFirst( null, ProjectExchangeService.class );
		if( service != null ) {
			project=service.getProject();
		}

		if( project != null ) {
			currentFile=project.getFileToRun();
		} else {
			bindProject();
			if( project == null ) {
				throw new RuntimeException( "Couldn't bindProject() within Debugger" );
			} else {
				currentFile=project.getFileToRun();
			}
		}
		currentLineNumber=0;
	}

	private void inspiritDebugScriptFromResources() {

		this.tclPartFile=new File( System.getProperty( "netbeans.user" ) + System.getProperty( "file.separator" ) + tclPartFileName );
		if( tclPartFile.exists() ) { // created every time because of common updates
			tclPartFile.delete();
		}
		try {
			String resourcesPath="resources/" + tclPartFileName;
			InputStream stream=TclDebuggerJavaPart.class.getResourceAsStream( resourcesPath );
			tclPartFile.createNewFile();
			OutputStream out=new FileOutputStream( tclPartFile );
			byte buf[]=new byte[ 1024 ];
			int len;
			while( ( len=stream.read( buf ) ) > 0 ) {
				out.write( buf, 0, len );
			}
			out.close();
			stream.close();
		} catch( IOException ex ) {
			Exceptions.printStackTrace( ex );
		}

	}

	private void runTclPart() {

		String fileToRunPath=project.getFileToRun().getAbsolutePath();
		this.debugServerProcessAndWindow=new RunTclsh( tclPartFileName, this.tclPartFile.getAbsolutePath(), fileToRunPath, TclProject.getDirectoryFromPath( fileToRunPath ), Integer.toString( this.tclPartPortNumber ), this.tclPartVerbose );
	}

	/*
	 * Returns true if connection is created
	 */
	private boolean createConnectionToTclPart() {

		if( tclPart == null ) {
			tclPart=new ClientToTclPart( this.tclPartPortNumber );
			if( tclPart == null ) {
				return false;
			}
			sendBreakpoints();
			getExecutionStatus();
			return true;
		}
		return false;
	}

	private void sendBreakpoints() {
		sendBreakpoints( false );
	}

	private void sendBreakpointsAndAddCursorPosition() {
		sendBreakpoints( true );
	}

	private void sendBreakpoints( boolean toCursor ) {


		Breakpoint[] breakpoints=DebuggerManager.getDebuggerManager().getBreakpoints();
		int iter;
		int breakpointsCount=breakpoints.length;

		if( breakpointsCount != 0 || toCursor == true ) {

			tclPart.printlnToSocket( "breakpoints" );

			// foreach breakpoint
			String filePath, lineNumber, lines;
			HashMap<String, String> bpoints=new HashMap<String, String>();

			// if RunToCursor
			if( toCursor ) {
				lineNumber=TclDebuggerUtils.getCurrentLineNumberString();
				filePath=TclDebuggerUtils.getCurrentFilePathInEditorString();
				bpoints.put( filePath, lineNumber );
			}

			for( iter=0; iter < breakpointsCount; iter++ ) {
				if( breakpoints[iter] instanceof TclBreakpoint && ( ( ( TclBreakpoint )breakpoints[iter] ).getLine() != null ) ) {
					TclBreakpoint bp=( TclBreakpoint )breakpoints[iter];

					filePath=bp.getFile().getPath();
					lineNumber=Integer.toString( bp.getLine().getLineNumber() + 1 );

					if( bpoints.containsKey( filePath ) ) {
						lines=bpoints.get( filePath );
						lines+=" " + lineNumber;
						bpoints.put( filePath, lines );
					} else {
						bpoints.put( filePath, lineNumber );
					}
				}
			}

			// send to server:
			// // number of files
			tclPart.printlnToSocket( Integer.toString( bpoints.size() ) );
			// // foreach file line numbers
			for( Map.Entry<String, String> point:bpoints.entrySet() ) {
				tclPart.printlnToSocket( point.getKey() );
				tclPart.printlnToSocket( point.getValue() );
			}
		}
	}

	private void getExecutionStatus() {
		getCurrentFileAndLine();
		getCurrentVariables();
		getCallStackUpdate();
	}

	private void getCurrentFileAndLine() {
		tclPart.printlnToSocket( "status" );
		try {
			// get first line → file localization
			String fileName=tclPart.nextLineFromFromSocket();
			currentFile=new File( fileName );

			// get second line → line in file
			currentLineNumber=Integer.parseInt( tclPart.nextLineFromFromSocket() );
			programCounter.modify();


		} catch( NoSuchElementException e ) {
			System.out.println( ">>> End of debugging…" );
			actionKill();
		}

	}

	private void getCurrentVariables() {
		if( tclPart.isClosed() ) {
			return;
		}
		tclPart.printlnToSocket( "variables" );
		try {
			// get first line → number of variablesStringLocals
			int numberOfVariables=Integer.parseInt( tclPart.nextLineFromFromSocket() );
			variablesStringLocals="";

			// every following numberOfVariables line contains name nad value of variable
			for( int i=1; i <= numberOfVariables; i++ ) {
				variablesStringLocals+=tclPart.nextLineFromFromSocket() + "\n";
			}

			// get next line → number of variablesStringGlobals
			numberOfVariables=Integer.parseInt( tclPart.nextLineFromFromSocket() );
			variablesStringGlobals="";
			// every following numberOfVariables line contains name nad value of variable
			for( int i=1; i <= numberOfVariables; i++ ) {
				variablesStringGlobals+=tclPart.nextLineFromFromSocket() + "\n";
			}

			updateTclVariablesAndWatchesModels();

		} catch( NoSuchElementException e ) {
			actionKill();
		}
	}

// ArrayList version
	private void getCallStackUpdate() {
		if( tclPart.isClosed() ) {
			return;
		}
		tclPart.printlnToSocket( "callstack" );
		try {
			int oldSize=callStackElements.size();
			tclPart.printlnToSocket( Integer.toString( oldSize ) );
			int numberOfElements=Integer.parseInt( tclPart.nextLineFromFromSocket() );
			if( numberOfElements == 0 ) {
				return;
			}

			TclCallStackElement newCallStackElement;
			// every following element contains four lines of description for each callstack element
			for( int iter=0; iter < numberOfElements; iter++ ) {
				newCallStackElement=new TclCallStackElement();
				newCallStackElement.setCmdStr( tclPart.nextLineFromFromSocket() );
				newCallStackElement.setInfoFrame( Integer.parseInt( tclPart.nextLineFromFromSocket() ) );
				newCallStackElement.setFileName( tclPart.nextLineFromFromSocket() );
				newCallStackElement.setLineNumber( Integer.parseInt( tclPart.nextLineFromFromSocket() ) );
				newCallStackElement.setId( oldSize++ );
				callStackElements.add( newCallStackElement );
			}
			updateTclCallStackModel();

		} catch( NoSuchElementException e ) {
			actionKill();
		}

	}

	private void updateTclVariablesAndWatchesModels() {
		if( variablesModelListener != null ) {
			variablesModelListener.modelChanged( null );
		}
		if( watchesModelListener != null ) {
			watchesModelListener.modelChanged( null );
		}
	}

	private void updateTclCallStackModel() {
		// update CallStack View
		if( callStackModelListener != null ) {
			callStackModelListener.modelChanged( null );
		}

	}

	/**
	 * Lock actions.
	 * Returns true if action could be done. False if there is a lock. 
	 */
	private boolean lockActions() {

		if( this.lockAction == true ) {
			// lock is on
			return false;
		} else {
			// create lock
			this.lockAction=true;
			return true;
		}
	}

	/**
	 * Unlocks actions.
	 */
	private void unlockActions() {
		this.lockAction=false;
	}

	// Actual actions below
	public void actionStepOver() {
		if( !lockActions() ) {
			return;
		}
		createConnectionToTclPart();
		tclPart.printlnToSocket( "stepover" );
		getExecutionStatus();
		unlockActions();
	}

	public void actionStepInto() {
		if( !lockActions() ) {
			return;
		}
		createConnectionToTclPart();
		tclPart.printlnToSocket( "step" );
		getExecutionStatus();
		unlockActions();
	}

	public void actionPause() {
		if( !lockActions() ) {
			return;
		}
		createConnectionToTclPart();
		unlockActions();
	}

	public void actionContinue() {
		if( !lockActions() ) {
			return;
		}
		if( !createConnectionToTclPart() ) { // for next runs (not first one)
			sendBreakpoints();
			tclPart.printlnToSocket( "continue" );
			getExecutionStatus();
		}
		unlockActions();
	}

	public void actionRunToCursor() {
		if( !lockActions() ) {
			return;
		}
		if( !createConnectionToTclPart() ) { // for next runs (not first one)
			sendBreakpointsAndAddCursorPosition();
			tclPart.printlnToSocket( "continue" );
			getExecutionStatus();
		}
		unlockActions();
	}

	public void actionKill() {
		// kill action doesn't need a lock
		//
		if( !debugServerProcessAndWindow.isInactive() ) {
			createConnectionToTclPart();
			tclPart.printlnToSocket( "exit" );
			tclPart.close();
		}
		//
		programCounter.detach();
		engineProvider.getDestructor().killEngine();
		//
		// Unlock Session
		TclDebuggerActionsProvider.activeDebugger=false;
	}

	// Getters below:
	//
	public File getCurrentFile() {
		return currentFile;
	}

	public int getCurrentLineNumber() {
		return currentLineNumber;
	}

	public String getVariablesStringLocals() {
		return variablesStringLocals;
	}

	public String getVariablesStringGlobals() {
		return variablesStringGlobals;
	}

	public ArrayList<TclCallStackElement> getCallStackElements() {
		return callStackElements;
	}

}
