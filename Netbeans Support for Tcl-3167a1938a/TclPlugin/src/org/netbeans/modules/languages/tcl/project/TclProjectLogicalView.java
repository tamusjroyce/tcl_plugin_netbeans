/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.project;

import java.awt.Image;
import javax.swing.Action;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Tcl Project Logical View.
 * @author dmp
 */
public class TclProjectLogicalView implements LogicalViewProvider {

	private final TclProject project;

	public TclProjectLogicalView( TclProject project ) {
		this.project=project;
	}

	// Filter - TclNode - For logical view
	private static final class TclNode extends FilterNode {

		final TclProject project;

		public TclNode( Node node, TclProject project ) throws DataObjectNotFoundException {
			super( node, new FilterNode.Children( node ),
			       new ProxyLookup( new Lookup[]{ Lookups.singleton( project ),
				       node.getLookup()
			       } ) );
			this.project=project;
		}

		@Override
		public Action[] getActions( boolean arg0 ) {	// Bind actions
			Action[] nodeActions=new Action[ 8 ];
			nodeActions[0]=CommonProjectActions.newFileAction();
			nodeActions[1]=CommonProjectActions.copyProjectAction();
			nodeActions[2]=CommonProjectActions.deleteProjectAction();
			nodeActions[5]=CommonProjectActions.setAsMainProjectAction();
			nodeActions[6]=CommonProjectActions.closeProjectAction();
			nodeActions[7]=CommonProjectActions.customizeProjectAction();
			return nodeActions;
		}

		@Override
		public Image getIcon( int type ) {
			return TclProject.getImage();
		}

		@Override
		public Image getOpenedIcon( int type ) {
			return getIcon( type );
		}

		@Override
		public String getDisplayName() {
			return project.getProjectDirectory().getName();
		}

	}

	@Override
	public Node createLogicalView() {
		try {
			// tcl/ folder, creating if deleted
			FileObject tcl=project.getTclFolder( true );
			DataFolder tclDataObject=DataFolder.findFolder( tcl );
			Node realTclFolderNode=tclDataObject.getNodeDelegate();
			return new TclNode( realTclFolderNode, project );

		} catch( DataObjectNotFoundException donfe ) {
			Exceptions.printStackTrace( donfe );
			return new AbstractNode( Children.LEAF );
		}
	}

	@Override
	public Node findPath( Node node, Object o ) {
		return null;
	}

}
