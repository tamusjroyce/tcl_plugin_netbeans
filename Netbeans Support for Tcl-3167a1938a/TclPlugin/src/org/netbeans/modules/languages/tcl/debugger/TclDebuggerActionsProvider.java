/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.netbeans.api.debugger.DebuggerInfo;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.modules.languages.tcl.project.TclProject;
import org.netbeans.spi.debugger.ActionsProviderSupport;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.DebuggerEngineProvider;
import org.netbeans.spi.debugger.SessionProvider;

/**
 * Tcl Debugger Actions Provider.
 * Registered by META-INF/debugger/TclSession/org.netbeans.spi.debugger.ActionsProvider
 * @author dmp
 */
public class TclDebuggerActionsProvider extends ActionsProviderSupport {

	public static final Object ACTION_STEP_OVER="stepOver";
	public static final Object ACTION_STEP_INTO="stepInto";
	public static final Object ACTION_STEP_OUT="stepOut";
	public static final Object ACTION_STEP_OPERATION="stepOperation";
	public static final Object ACTION_CONTINUE="continue";
	public static final Object ACTION_REWIND="rewind";
	public static final Object ACTION_START="start";
	public static final Object ACTION_KILL="kill";
	public static final Object ACTION_MAKE_CALLER_CURRENT="makeCallerCurrent";
	public static final Object ACTION_PAUSE="pause";
	public static final Object ACTION_RUN_TO_CURSOR="runToCursor";
	public static final Object ACTION_RUN_BACK_TO_CURSOR="runBackToCursor";
	public static final Object ACTION_POP_TOPMOST_CALL="popTopmostCall";
	public static final Object ACTION_FIX="fix";
	public static final Object ACTION_RESTART="restart";
	public static final Object ACTION_TOGGLE_BREAKPOINT="toggleBreakpoint";
	public static final String TCL_DEBUGGER_INFO="TclDebuggerInfo";
	public static final String TCL_SESSION="TclSession";
	private TclDebuggerEngineProvider engineProvider=null;

	private TclDebuggerJavaPart tclDebuggerJavaPart;

	private static final HashSet<Object> actions=new HashSet<Object>();
	static{
		actions.add( ACTION_KILL );
		actions.add( ACTION_PAUSE );
		actions.add( ACTION_CONTINUE );
		actions.add( ACTION_START );
		actions.add( ACTION_STEP_INTO );
		actions.add( ACTION_STEP_OVER );
		actions.add( ACTION_RUN_TO_CURSOR );
	}
	public static boolean activeDebugger = false;

	public TclDebuggerActionsProvider( ContextProvider contextProvider ) {
		engineProvider=( TclDebuggerEngineProvider )contextProvider.lookupFirst( null, DebuggerEngineProvider.class );
		for( Iterator it=actions.iterator(); it.hasNext(); ) {
			setEnabled( it.next(), true );
		}
	}

	public static void startDebugger( final TclProject tclProject ) {
		if( TclDebuggerActionsProvider.activeDebugger == false ){
			
		DebuggerManager manager=DebuggerManager.getDebuggerManager();
		//System.out.println( ">>> startDebugger" );
		DebuggerInfo info=DebuggerInfo.create( TCL_DEBUGGER_INFO,
		       new Object[]{
			       new SessionProvider() {

				       @Override
				       public String getSessionName() {
					       return "Tcl Program";
				       }

				       @Override
				       public String getLocationName() {
					       return "localhost";
				       }

				       @Override
				       public String getTypeID() {
					       return TCL_SESSION;
				       }

				       @Override
				       public Object[] getServices() {
					       return new Object[]{
							      new ProjectExchangeService() {

								      @Override
								      public TclProject getProject() {
									      return tclProject;
								      }

							      }
						      };
				       }

			       }, null
		       } );

		manager.startDebugging( info );
		TclDebuggerActionsProvider.activeDebugger = true;
		}
	}

	@Override
	public Set getActions() {
		return actions;
	}

	@Override
	public void doAction( Object o ) {
		if( o == ACTION_START ) {

			tclDebuggerJavaPart=new TclDebuggerJavaPart( engineProvider );

		} else if( o == ACTION_KILL ) {

			tclDebuggerJavaPart.actionKill();

		} else if( o == ACTION_PAUSE ) {

			tclDebuggerJavaPart.actionPause();

		} else if( o == ACTION_CONTINUE ) {

			tclDebuggerJavaPart.actionContinue();

		} else if( o == ACTION_STEP_INTO ) {

			tclDebuggerJavaPart.actionStepInto();

		} else if( o == ACTION_STEP_OVER ) {

			tclDebuggerJavaPart.actionStepOver();

		} else if( o == ACTION_RUN_TO_CURSOR ) {

			tclDebuggerJavaPart.actionRunToCursor();

		}
	}

	public TclDebuggerJavaPart getTclDebuggerJavaPart() {
		return tclDebuggerJavaPart;
	}
}
