/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.watches;

import java.util.ArrayList;
import java.util.Properties;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.Watch;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerActionsProvider;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerJavaPart;
import org.netbeans.modules.languages.tcl.debugger.variables.TclVariablesModel;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.ui.Constants;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.TableModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;

/**
 * Implementation of Watches View.
 * @author dmp
 */
public class TclWatchesModel implements NodeModel, TreeModel, TableModel {

	private String iconBaseString = "org/netbeans/modules/debugger/resources/watchesView/Watch";
	//private String iconBaseString = "org/netbeans/modules/debugger/resources/watchesView/watch_16.png"; // diamond icon doesn't work
	private ArrayList<ModelListener> listeners=new ArrayList<ModelListener>();
	private Properties variablePropertiesLocals;
	private Properties variablePropertiesGlobals;
	private final TclDebuggerJavaPart tclDebugger;
	private final TclVariablesModel tclVariablesModel;

	public TclWatchesModel( ContextProvider lookupProvider ) {
		
		TclDebuggerActionsProvider tclDebuggerActionsProvider=lookupProvider.lookupFirst( null, TclDebuggerActionsProvider.class );
		this.tclDebugger=tclDebuggerActionsProvider.getTclDebuggerJavaPart();
		
		this.tclVariablesModel=lookupProvider.lookupFirst( null, TclVariablesModel.class );

		// initialize
		variablePropertiesLocals=new Properties();
		variablePropertiesGlobals=new Properties();
	}

	@Override
	public String getDisplayName( Object o ) throws UnknownTypeException {
		Watch watch=( Watch )o;
		return watch.getExpression();
	}

	@Override
	public String getIconBase( Object o ) throws UnknownTypeException {
		return iconBaseString;
	}

	@Override
	public String getShortDescription( Object o ) throws UnknownTypeException {
		return "Variable Name";
	}

	@Override
	public void addModelListener( ModelListener ml ) {
		tclDebugger.addWatchesModelListener( ml );
		tclVariablesModel.addWatchesModelListener( ml );
		listeners.add( ml );
	}

	@Override
	public void removeModelListener( ModelListener ml ) {
		listeners.remove( ml );
	}

	@Override
	public Object getRoot() {
		return ROOT;
	}

	@Override
	public Object[] getChildren( Object o, int i, int i1 ) throws UnknownTypeException {

		ArrayList<Object> kids=new ArrayList<Object>();
		if( o == ROOT ) {

			variablePropertiesLocals=tclVariablesModel.getVariablePropertiesLocals();
			variablePropertiesGlobals=tclVariablesModel.getVariablePropertiesGlobals();

			Watch[] watches=DebuggerManager.getDebuggerManager().getWatches();

			int iter;
			int watchesCount=watches.length;

			for( iter=0; iter < watchesCount; iter++ ) {
				if( watches[iter] instanceof Watch ) {
					kids.add( watches[iter]);
				}
			}
		}
		return kids.toArray();
	}

	@Override
	public boolean isLeaf( Object o ) throws UnknownTypeException {
		if( o instanceof String ) {
			String s=( String )o;
			if( s.compareTo( ROOT ) == 0 ) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getChildrenCount( Object o ) throws UnknownTypeException {
		return 1;
	}

	@Override
	public Object getValueAt( Object o, String string ) throws UnknownTypeException {
		String type, value, name;

		if( o instanceof Watch ) {
			Watch watch=( Watch )o;
			name=watch.getExpression();
			if( string.compareTo( Constants.WATCH_TYPE_COLUMN_ID ) == 0 ) {
				value=variablePropertiesLocals.getProperty( name, "" ); // check LOCALS
				if( value.isEmpty() ) {
					value=variablePropertiesGlobals.getProperty( name, "" ); // and then check GLOBALS
					if( value.isEmpty() ) {
						type="not defined";
					} else {
						type="global watch";
					}
				} else {
					type="local watch";
				}

				return type;
			}
			if( string.compareTo( Constants.WATCH_VALUE_COLUMN_ID ) == 0 ) {
				value=variablePropertiesLocals.getProperty( name, "" ); // check LOCALS
				if( value.isEmpty() ) {
					value=variablePropertiesGlobals.getProperty( name, "" ); // and then GLOBALS
					if( value.isEmpty() ) {
						value="variable not found";
					}
				}
				return value;
			}
		}
		return "";
	}

	@Override
	public boolean isReadOnly( Object o, String string ) throws UnknownTypeException {
		return true;
	}

	@Override
	public void setValueAt( Object o, String string, Object o1 ) throws UnknownTypeException {
		return;
	}

	public void updateModel() {
		ModelListener ml;
		int size=listeners.size();
		for( int incr=0; incr < size; incr++ ) {
			ml=listeners.get( incr );
			ml.modelChanged( null );
		}
	}
}
