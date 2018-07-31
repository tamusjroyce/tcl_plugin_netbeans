/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.callstack;

import java.util.ArrayList;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerActionsProvider;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerJavaPart;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.TableModel;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;

/**
 * Implementation of CallStack View.
 * @author dmp
 */
public class TclCallStackModel implements TreeModel, TableModel, NodeModel {

	private ArrayList<ModelListener> listeners=new ArrayList<ModelListener>();
	private String iconBaseString="org/netbeans/modules/debugger/resources/callStackView/NonCurrentFrame";
	// "org/netbeans/modules/debugger/resources/callStackView/CurrentFrame"
     
	private final TclDebuggerJavaPart tclDebugger;
	private ArrayList<TclCallStackElement> callStackElements;

	public TclCallStackModel(ContextProvider lookupProvider) {

		TclDebuggerActionsProvider tclDebuggerActionsProvider=lookupProvider.lookupFirst( null, TclDebuggerActionsProvider.class );
		this.tclDebugger=tclDebuggerActionsProvider.getTclDebuggerJavaPart();
	}


	@Override
	public Object getRoot() {
		return ROOT;
	}

	@Override
	public Object[] getChildren( Object o, int i, int i1 ) throws UnknownTypeException {

		ArrayList<Object> kids=new ArrayList<Object>();
		if( o == ROOT ) {
			callStackElements = tclDebugger.getCallStackElements();
			for(int iter = callStackElements.size()-1; iter >= 0; iter--) {
				kids.add( callStackElements.get( iter ) );
			}
		}
		return kids.toArray();
	}

	@Override
	public boolean isLeaf( Object o ) throws UnknownTypeException {
		if( o == ROOT ) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int getChildrenCount( Object o ) throws UnknownTypeException {
		return 1;
	}

	@Override
	public void addModelListener( ModelListener ml ) {
		tclDebugger.addCallStackModelListener( ml );
		listeners.add( ml );
	}

	@Override
	public void removeModelListener( ModelListener ml ) {
		listeners.remove( ml );
	}

	@Override
	public Object getValueAt( Object o, String string ) throws UnknownTypeException {
		if( o instanceof TclCallStackElement ) {
			TclCallStackElement element=( TclCallStackElement )o;
			
			if( string.equals( TclCallStackModelColumns.TCLCALLSTACK_COLUMN_CMDSTR_ID ) ) {
				return element.getCmdStr();
			}
			if( string.equals( TclCallStackModelColumns.TCLCALLSTACK_COLUMN_FILEANDLINE_ID ) ) {
				return element.getFile().getName()+":"+element.getLineNumber();
			}
			if( string.equals( TclCallStackModelColumns.TCLCALLSTACK_COLUMN_LEVEL_ID) ) {
				return element.getInfoFrame();
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

	@Override
	public String getDisplayName( Object o ) throws UnknownTypeException {
			TclCallStackElement element=( TclCallStackElement )o;
			return Integer.toString( element.getId());
	}

	@Override
	public String getIconBase( Object o ) throws UnknownTypeException {
		return iconBaseString;
	}

	@Override
	public String getShortDescription( Object o ) throws UnknownTypeException {
		return "Tcl CallStack Element";
	}

}
