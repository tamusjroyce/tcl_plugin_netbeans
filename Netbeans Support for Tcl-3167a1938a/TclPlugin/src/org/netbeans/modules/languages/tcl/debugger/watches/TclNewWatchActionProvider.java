/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.watches;

import java.util.Collections;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.spi.debugger.ActionsProviderSupport;
import org.netbeans.spi.debugger.ContextProvider;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.InputLine;

/**
 * Handle ACTION_NEW_WATCH
 * @author dmp
 */
public class TclNewWatchActionProvider extends ActionsProviderSupport {

	private final static Set ACTIONS=Collections.singleton( ActionsManager.ACTION_NEW_WATCH );
	private final ContextProvider lookupProvider;

	public TclNewWatchActionProvider( ContextProvider lookupProvider ) {

		this.lookupProvider=lookupProvider;
		setEnabled( ActionsManager.ACTION_NEW_WATCH, true );
	}

	@Override
	public void doAction( Object o ) {

		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				InputLine inputLine=new NotifyDescriptor.InputLine( "Enter the name of variable to create new watch:", "Add New Watch" );
				Object notify=DialogDisplayer.getDefault().notify( inputLine );
				if( notify == NotifyDescriptor.OK_OPTION ) {
					String inputText=inputLine.getInputText();
					if( !inputText.isEmpty() ) {
						DebuggerManager.getDebuggerManager().createWatch( inputText );
						TclWatchesModel tclWatchesModel=lookupProvider.lookupFirst( null, TclWatchesModel.class );
						if( tclWatchesModel != null ) {
							tclWatchesModel.updateModel();
						}
					}
				}

			}

		} );
	}

	@Override
	public Set getActions() {
		return ACTIONS;
	}

}
