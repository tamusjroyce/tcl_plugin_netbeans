/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.watches;

import javax.swing.Action;
import org.netbeans.api.debugger.Watch;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.Models.ActionPerformer;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.netbeans.spi.viewmodel.UnknownTypeException;

/**
 * Watches actions.
 * @author dmp
 */
public class TclWatchesNodeActionsProvider implements NodeActionsProvider {

	private static ContextProvider lookupProvider;
	private static Action ACTION_REMOVE_WATCH;
	private static Action ACTION_NEW_WATCH;

	static{
		String id="Add New Watch";
		ActionPerformer actionPerformer=new ActionPerformer() {

			@Override
			public void perform( Object[] nodes ) {
				TclNewWatchActionProvider tclWatchesNodeActionsProvider=new TclNewWatchActionProvider( lookupProvider );
				tclWatchesNodeActionsProvider.doAction( nodes );
			}

			@Override
			public boolean isEnabled( Object node ) {
				return true;
			}

		};
		ACTION_NEW_WATCH=Models.createAction( id, actionPerformer, Models.MULTISELECTION_TYPE_ANY);
	}

	static{
		String id="Remove";
		ActionPerformer actionPerformer=new ActionPerformer() {

			@Override
			public void perform( Object[] nodes ) {
				// Must be only one because of Models.MULTISELECTION_TYPE_EXACTLY_ONE
				if( nodes[0] instanceof Watch ) {
					Watch watch=( Watch )nodes[0];
					watch.remove();
					TclWatchesModel tclWatchesModel=lookupProvider.lookupFirst( null, TclWatchesModel.class );
					if( tclWatchesModel != null) { 
						tclWatchesModel.updateModel();
					}
				}
			}

			@Override
			public boolean isEnabled( Object node ) {
				return true;
			}

		};
		ACTION_REMOVE_WATCH=Models.createAction( id, actionPerformer, Models.MULTISELECTION_TYPE_EXACTLY_ONE );
	}

	public TclWatchesNodeActionsProvider( final ContextProvider lookupProvider ) {
		TclWatchesNodeActionsProvider.lookupProvider=lookupProvider;
	}

	@Override
	public void performDefaultAction( Object o ) throws UnknownTypeException {
		// double click on Node…
	}

	@Override
	public Action[] getActions( Object o ) throws UnknownTypeException {
		Action[] actions;
		if( o instanceof Watch ) {
			actions=new Action[ 2 ];
			actions[0]=ACTION_NEW_WATCH;
			actions[1]=ACTION_REMOVE_WATCH;
		} else {
			actions=new Action[ 1 ];
			actions[0]=ACTION_NEW_WATCH;

		}
		return actions;
	}

}
