/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.variables;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerActionsProvider;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerJavaPart;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.ui.Constants;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.TableModel;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.openide.util.Exceptions;

/**
 * Implementation of Variables View.
 * @author dmp
 */
public class TclVariablesModel implements TreeModel, NodeModel, TableModel {

	private String iconBaseString="org/netbeans/modules/debugger/resources/localsView/LocalVariable";
	private ArrayList<ModelListener> listeners=new ArrayList<ModelListener>();
	private final TclDebuggerJavaPart tclDebugger;
	// Properties shared with TclWatchesModel 
	private Properties variablePropertiesLocals;
	private Properties variablePropertiesGlobals;
	// Listeners
	private ModelListener watchesModelListener=null;

	public TclVariablesModel( ContextProvider lookupProvider ) {

		TclDebuggerActionsProvider tclDebuggerActionsProvider=lookupProvider.lookupFirst( null, TclDebuggerActionsProvider.class );
		tclDebugger=tclDebuggerActionsProvider.getTclDebuggerJavaPart();

		// init
		variablePropertiesGlobals=new Properties();
		variablePropertiesLocals=new Properties();
	}

	@Override
	public Object getRoot() {
		return ROOT;
	}

	@Override
	public void addModelListener( ModelListener ml ) {
		tclDebugger.addVariablesModelListener( ml );
		listeners.add( ml );
	}

	public void addWatchesModelListener( ModelListener ml ) {
		this.watchesModelListener=ml;
	}

	public void updateWatchesModel() {
		if( watchesModelListener != null ) {
			watchesModelListener.modelChanged( null );
		}
	}

	@Override
	public void removeModelListener( ModelListener ml ) {
		listeners.remove( ml );
	}

	@Override
	public boolean isReadOnly( Object o, String string ) throws UnknownTypeException {
		return true;
	}

	@Override
	public Object[] getChildren( Object o, int i, int i1 ) throws UnknownTypeException {

		ArrayList<Object> kids=new ArrayList<Object>();

		if( o == ROOT ) {
			// Locals
			variablePropertiesLocals=transformVariablesStringToProperties( kids, tclDebugger.getVariablesStringLocals(), "local" );
			// Globals
			variablePropertiesGlobals=transformVariablesStringToProperties( kids, tclDebugger.getVariablesStringGlobals(), "global" );
			// Update Watches Model
			updateWatchesModel();
		}

		if( o instanceof TclVariable ) {
			TclVariable var=( TclVariable )o;
			if( var.getType().equals( "local" ) ) {
				childrenOfArray( var.getName(), kids, variablePropertiesLocals, "local" );
			} else if( var.getType().equals( "global" ) ) {
				childrenOfArray( var.getName(), kids, variablePropertiesGlobals, "global" );
			}
		}

		return kids.toArray();
	}

	private void childrenOfArray( String arrayName, ArrayList<Object> kids, Properties vProperties, String type ) {

		String keys, value;
		String arrayKeys=arrayName + "_KEYS";

		keys=vProperties.getProperty( arrayKeys );

		for( String s:keys.split( " " ) ) {
			value=vProperties.getProperty( arrayName + "(" + s + ")" );
			kids.add( new TclVariable( s, type, value ) );
		}

	}

	private Properties transformVariablesStringToProperties( ArrayList<Object> kids, String vString, String type ) {

		String name;
		String value;
		Properties vProperties=new Properties();
		if( !vString.isEmpty() ) {
			StringReader sr=new StringReader( vString );
			try {
				vProperties.load( sr );
				for( Object object:vProperties.keySet() ) {
					name=( String )object;
					value=vProperties.getProperty( name );
					if( !name.contains( "(" ) && !name.contains( "_KEYS" ) ) { // Ommit arrays and *_KEYS
						kids.add( new TclVariable( name, type, value ) );
					}
				}
			} catch( IOException ex ) {
				Exceptions.printStackTrace( ex );
			}
		}
		return vProperties;
	}

	@Override
	public boolean isLeaf( Object o ) throws UnknownTypeException {
		if( o instanceof String ) {
			String s=( String )o;
			if( s.compareTo( ROOT ) == 0 ) {
				return false;
			}
		}
		if( o instanceof TclVariable ) {
			TclVariable v=( TclVariable )o;
			if( "<ARRAY>".equals( v.getValue() ) ) {
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
	public String getDisplayName( Object o ) throws UnknownTypeException {
		TclVariable var=( TclVariable )o;
		return var.getName();
	}

	@Override
	public String getIconBase( Object o ) throws UnknownTypeException {
		return iconBaseString;
	}

	@Override
	public String getShortDescription( Object o ) throws UnknownTypeException {
		return "Tcl Variable";
	}

	@Override
	public void setValueAt( Object o, String string, Object o1 ) throws UnknownTypeException {
		return;
	}

	@Override
	public Object getValueAt( Object o, String string ) throws UnknownTypeException {
		if( o instanceof TclVariable ) {
			TclVariable var=( TclVariable )o;
			if( string.compareTo( Constants.LOCALS_VALUE_COLUMN_ID ) == 0 ) {
				return var.getValue();
			}
			if( string.compareTo( Constants.LOCALS_TYPE_COLUMN_ID ) == 0 ) {
				return var.getType();
			}
			//if( string.compareTo( Constants.LOCALS_TO_STRING_COLUMN_ID ) == 0 ) {
			//	return var.getValue();
			//} // It's hidden column.
		}
		return "";
	}

	public Properties getVariablePropertiesGlobals() {
		return variablePropertiesGlobals;
	}

	public Properties getVariablePropertiesLocals() {
		return variablePropertiesLocals;
	}

	// TclVariablesModel is updated only by TclDebuggerJavaPart using dedicated ModelListener
	// method below unused
	//public void updateModel() {
	//	ModelListener ml;
	//	int size=listeners.size();
	//	for( int incr=0; incr < size; incr++ ) {
	//		ml=listeners.get( incr );
	//		ml.modelChanged( null );
	//	}
	//}

}
