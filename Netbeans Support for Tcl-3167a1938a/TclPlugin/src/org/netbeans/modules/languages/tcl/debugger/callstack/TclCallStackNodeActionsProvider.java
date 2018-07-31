/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.callstack;

import java.io.IOException;
import javax.swing.Action;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerUtils;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.Models.ActionPerformer;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 * Actions for TclCallStackElement in CallStackView.
 * @author dmp
 */
public class TclCallStackNodeActionsProvider implements NodeActionsProvider {
	
	private static Action ACTION_JUMP_TO_CALLSTACK_ELEMENT;

	static{
		String id="Jump To";
		ActionPerformer actionPerformer=new ActionPerformer() {

			@Override
			public void perform( Object[] nodes ) {
				jumpToCallStackElement( nodes[0]);
			}

			@Override
			public boolean isEnabled( Object node ) {
				return true;
			}

		};
		ACTION_JUMP_TO_CALLSTACK_ELEMENT=Models.createAction( id, actionPerformer, Models.MULTISELECTION_TYPE_EXACTLY_ONE );
	}


	private static void jumpToCallStackElement( Object o ) {
		if( o instanceof TclCallStackElement ) {

			TclCallStackElement element = ( TclCallStackElement )o;

			FileObject fileObject;
			try {
				fileObject=FileUtil.createData( element.getFile());
				DataObject dataObject;
				dataObject=DataObject.find( fileObject );
				if( dataObject == null ) {
					return;
				}
				TclDebuggerUtils.jumpToLineInFile( dataObject, element.getLineNumber()-1 );
			} catch( IOException ex ) {
				Exceptions.printStackTrace( ex );
			}
		}
	}

	@Override
	public void performDefaultAction( Object o ) throws UnknownTypeException {
		jumpToCallStackElement(o);
	}

	@Override
	public Action[] getActions( Object o ) throws UnknownTypeException {
		Action[] actions=new Action[ 1 ];
		if( o instanceof TclCallStackElement ) {
			actions[0]=ACTION_JUMP_TO_CALLSTACK_ELEMENT;
		}
		return actions;
	}

}
