/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.run;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.netbeans.modules.languages.tcl.optionpanel.TclPanel;
import org.netbeans.modules.languages.tcl.project.TclProject;

/*
 * Helpers classes:
 */
class InputStreamViaThread extends Thread {

	private InputStream stream;
	private String streamName;
	public Boolean isEndOfStreamReached;
	private final InputOutput io;
	private final InternalsOfOutputWindowWithProcess parent;
	private InputStreamViaThread processToLock=null;
	boolean lockThread=false;

	InputStreamViaThread( InputStream stream, String streamName, InputOutput io, InternalsOfOutputWindowWithProcess parent ) {
		this.stream=stream;
		this.streamName=streamName;
		this.io=io;
		this.parent=parent;
		isEndOfStreamReached=false;
	}

	public void setProcessToLock( InputStreamViaThread toWait ) {
		this.processToLock=toWait;
	}

	@Override
	public void run() {
		int inputInt=-1;
		char inputChar;
		BufferedInputStream bstream=new BufferedInputStream( stream );
		while( true ) {
			try {
				//System.out.println( "AVAILABLE(" + streamName + ")[" + bstream.available() + "]" );
				inputInt=bstream.read();
				if( inputInt == -1 ) {
					break;
				}
				// print to tclsh window
				inputChar=( char )inputInt;
				if( streamName.equals( "out" ) ) {
					while( true ) {
						if( lockThread == false ) {
							break;
						}
						//System.out.println( "LOCK" );
					}
					io.getOut().print( inputChar );
					if( bstream.available() == 0 ) {
						io.getOut().flush();
					}
				} else { // processToLock used only by "err" - stderr
					if( bstream.available() == 0 ) {
						Thread.sleep( 20 );
						processToLock.lockThread=false;
					} else if( processToLock.lockThread == false ) {
						Thread.sleep( 20 );
						processToLock.lockThread=true;
					}
					io.getErr().print( inputChar );
					if( bstream.available() == 0 ) {
						io.getErr().flush();
					}
				}
			} catch( Exception ex ) {
				Exceptions.printStackTrace( ex );
			}
		}

		if( streamName.equals( "out" ) ) {
			parent.endOfStreamReached();
		}
		isEndOfStreamReached=true;
	}

}

class InternalsOfOutputWindowWithProcess implements Runnable {

	public Process process;

	public final InputOutput ioOutputWindow;
	public final String processString;
	public final String tclFilePath;
	private String arg1=null;
	private String arg2=null;
	private String arg3=null;
	private String arg4=null;
	private final RunTclsh parent;

	public InternalsOfOutputWindowWithProcess( InputOutput io, RunTclsh parent, String processString ) {
		this.ioOutputWindow=io;
		this.parent = parent;
		this.processString=processString;
		this.tclFilePath=null;
	}

	public InternalsOfOutputWindowWithProcess( InputOutput io, RunTclsh parent, String processString, String tclFile ) {
		this.ioOutputWindow=io;
		this.parent = parent;
		this.processString=processString;
		this.tclFilePath=tclFile;
	}

	public InternalsOfOutputWindowWithProcess( InputOutput io, RunTclsh parent, String processString, String tclFile, String arg1 ) {
		this.ioOutputWindow=io;
		this.parent = parent;
		this.processString=processString;
		this.tclFilePath=tclFile;
		this.arg1=arg1;
	}

	public InternalsOfOutputWindowWithProcess( InputOutput io, RunTclsh parent, String processString, String tclFile, String arg1, String arg2 ) {
		this.ioOutputWindow=io;
		this.parent = parent;
		this.processString=processString;
		this.tclFilePath=tclFile;
		this.arg1=arg1;
		this.arg2=arg2;
	}

	public InternalsOfOutputWindowWithProcess( InputOutput io, RunTclsh parent, String processString, String tclFile, String arg1, String arg2, String arg3, String arg4 ) {
		this.ioOutputWindow=io;
		this.parent = parent;
		this.processString=processString;
		this.tclFilePath=tclFile;
		this.arg1=arg1;
		this.arg2=arg2;
		this.arg3=arg3;
		this.arg4=arg4;
	}

	public void endOfStreamReached() {

		parent.setInactive();
		
		int status=42;
		try {
			status=process.waitFor();
		} catch( InterruptedException ex ) {
			Exceptions.printStackTrace( ex );
		}

		ioOutputWindow.getOut().println( processString + " process exited with " + status + " status." );
		ioOutputWindow.getOut().println( "Output window INACTIVE." );
		ioOutputWindow.getOut().close();

		ioOutputWindow.getErr().close();
		try {
			ioOutputWindow.getIn().close();
		} catch( IOException ex ) {
			Exceptions.printStackTrace( ex );
		}

	}


	@Override
	public void run() {
		BufferedReader userInputReader=new BufferedReader( ioOutputWindow.getIn() );
		char userInputChar;
		try {
			//String userInputLine;
			//if( tclFilePath == null ) {
			if( arg1 == null ) {
				this.process=new ProcessBuilder( processString ).start();
			} else if( arg4 != null ) {
				this.process=new ProcessBuilder( processString, tclFilePath, arg1, arg2, arg3, arg4 ).start();
			} else if( arg3 != null ) {
				this.process=new ProcessBuilder( processString, tclFilePath, arg1, arg2, arg3 ).start();
			} else if( arg2 != null ) {
				this.process=new ProcessBuilder( processString, tclFilePath, arg1, arg2 ).start();
			} else {
				this.process=new ProcessBuilder( processString, tclFilePath, arg1 ).start();
			}
			//} else {
			//	process=new ProcessBuilder( processString, tclFilePath ).start();
			//}
		} catch( IOException ex ) {
			JOptionPane.showMessageDialog( null, "Please set correct tclsh path using Tools→Options→Tcl tab.", "Information", JOptionPane.INFORMATION_MESSAGE, TclPanel.getImageIcon() );
			ioOutputWindow.closeInputOutput();
			return;
		}
		try {
			InputStreamViaThread processOut=new InputStreamViaThread( process.getInputStream(), "out", ioOutputWindow, this );
			InputStreamViaThread processErr=new InputStreamViaThread( process.getErrorStream(), "err", ioOutputWindow, this );

			processOut.start();

			processErr.setProcessToLock( processOut );
			processErr.start();

			PrintWriter processIn=new PrintWriter( process.getOutputStream() );

			/// Tcl commands // Interactive mode is activated when arg1 or arg2 are not set {
			if( arg1 == null ) {
				processIn.println( "puts \">>> " + processString + "\"" );
				processIn.println( "set ::tcl_interactive 1" );
				processIn.println( "puts \"set ::tcl_interactive 1\"" );
				if( tclFilePath != null ) {
					processIn.println( "cd "+TclProject.getDirectoryFromPath( tclFilePath) );
					processIn.println( "source \"" + tclFilePath + "\"; exit" );
				}
				processIn.flush();
			}
			/// }

			// Forward input String from user to process	
			while( !processErr.isEndOfStreamReached && !processOut.isEndOfStreamReached ) {
				//Thread.sleep( 333 ); // Delay used for tclsh's "%" emulation
				//ioOutputWindow.getOut().print( "> " );
				userInputChar=( char )userInputReader.read();
				processIn.print( userInputChar );
				processIn.flush();
			}

		} catch( Exception ex ) {
			Exceptions.printStackTrace( ex );
		}
	}

}

/**
 * Class that operates new Tclsh and its window.
 * Follow public methods to understand.
 * @author dmp
 */
public final class RunTclsh {

	private String tclFilePath=null;
	private String windowName="tclsh";
	private String arg1=null;
	private String arg2=null;
	private String arg3=null;
	private String arg4=null;
	private boolean inactive = false;

	public RunTclsh() {
		execTclsh();
	}

	public RunTclsh( String name, String path ) {
		// .replace is solution for backslashes under windows
		this.tclFilePath=path.replace( "\\", "\\\\" );

		this.windowName="tclsh " + name;
		execTclScriptViaTclshWithIntaractiveMode();
	}

	public RunTclsh( String name, String path, String arg1, String arg2, String arg3, String arg4 ) {
		// .replace is solution for backslashes under windows
		this.tclFilePath=path.replace( "\\", "\\\\" );

		this.windowName=name;
		this.arg1=arg1;
		this.arg2=arg2;
		this.arg3=arg3;
		this.arg4=arg4;
		execTclScriptViaTclshWithArg1234();
	}

	void execTclsh() {
		String tclshPath=NbPreferences.forModule( TclPanel.class ).get( TclPanel.preferenceTclshPath, TclPanel.getTclshDefaultPath() );
		InputOutput ioTclsh=IOProvider.getDefault().getIO( windowName, true );
		//IOTab.setIcon(ioTclsh, TclProject.getImageIcon());
		ioTclsh.select();
		new Thread( new InternalsOfOutputWindowWithProcess( ioTclsh, this, tclshPath ) ).start();
	}

	private void execTclScriptViaTclshWithIntaractiveMode() {
		String tclshPath=NbPreferences.forModule( TclPanel.class ).get( TclPanel.preferenceTclshPath, TclPanel.getTclshDefaultPath() );
		InputOutput ioTclsh=IOProvider.getDefault().getIO( windowName, true );
		//IOTab.setIcon(ioTclsh, TclProject.getImageIcon());
		ioTclsh.select();

		new Thread( new InternalsOfOutputWindowWithProcess( ioTclsh, this, tclshPath, tclFilePath ) ).start();
	}

	private void execTclScriptViaTclshWithArg1234() {
		String tclshPath=NbPreferences.forModule( TclPanel.class ).get( TclPanel.preferenceTclshPath, TclPanel.getTclshDefaultPath() );
		InputOutput ioTclsh=IOProvider.getDefault().getIO( windowName, true );
		//IOTab.setIcon(ioTclsh, TclProject.getImageIcon());
		ioTclsh.select();

		new Thread( new InternalsOfOutputWindowWithProcess( ioTclsh, this, tclshPath, tclFilePath, arg1, arg2, arg3, arg4 ) ).start();
	}

	public boolean isInactive() {
		return inactive;
	}
	public void setInactive() {
		this.inactive = true;
	}
}
